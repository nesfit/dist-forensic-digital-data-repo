package cz.vutbr.fit.distributedrepository;

import cz.vutbr.fit.communication.KafkaCriteria;
import cz.vutbr.fit.communication.MetadataOperation;
import cz.vutbr.fit.distributedrepository.service.pcap.dumper.PcapDumper;
import cz.vutbr.fit.distributedrepository.service.pcap.dumper.pcap4j.DumperImpl;
import cz.vutbr.fit.distributedrepository.util.JavaEnvironment;
import cz.vutbr.fit.persistence.cassandra.entity.CassandraPacket;
import cz.vutbr.fit.persistence.cassandra.repository.PacketRepository;
import cz.vutbr.fit.persistence.mongodb.entity.PacketMetadata;
import cz.vutbr.fit.persistence.mongodb.repository.PacketMetadataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.mongodb.core.query.Criteria;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Semaphore;

//@SpringBootApplication
//@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class TestDatabaseSpringBoot implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestDatabaseSpringBoot.class);

    static {
        JavaEnvironment.SetUp();
    }

    @Autowired
    PacketRepository packetRepository;

    @Autowired
    PacketMetadataRepository packetMetadataRepository;

    private PcapDumper<byte[]> pcapDumper = new DumperImpl();

    private int packetMetadataLoadedCount;
    private int packetLoadedCount;

    private Semaphore semaphore = new Semaphore(0);

    public static void main(String[] args) {
        new SpringApplicationBuilder(TestDatabaseSpringBoot.class)
                .web(WebApplicationType.NONE)
                .bannerMode(Banner.Mode.OFF)
                .build()
                .run(args);
    }

    private void handleFailure(Throwable throwable) {
        LOGGER.error(throwable.getMessage(), throwable);
    }

    private Criteria prepareCriteria(List<KafkaCriteria> kafkaCriterias) {
        Criteria criteria = new Criteria();
        kafkaCriterias.forEach(
                kafkaCriteria ->
                        packetMetadataRepository.appendCriteria(
                                criteria,
                                kafkaCriteria.getField(),
                                kafkaCriteria.getOperation().getOperationAsString(),
                                kafkaCriteria.getOperation().isArrayRequired(),
                                kafkaCriteria.getValue(),
                                kafkaCriteria.getValues(),
                                this::handleFailure)
        );
        return criteria;
    }

    public void loadPacketsByCriteria(Criteria criteria) {
        packetMetadataLoadedCount = 0;
        packetLoadedCount = 0;

        pcapDumper.initDumper("file.pcap", this::handleFailure);

        packetMetadataRepository.findByDynamicCriteria(criteria)
                .doOnError(this::handleFailure)
                .doOnNext(this::loadPacket)
                .doOnComplete(() -> blockIfNecessaryUntilPacketsAreLoaded())
                .subscribe();
    }

    private void loadPacket(PacketMetadata packetMetadata) {
        packetMetadataLoadedCount++;
        LOGGER.info("Loading packet with id " + packetMetadata.getRefId());
        packetRepository.findById(packetMetadata.getRefId())
                .doOnNext(packet -> dumpPacket(packet, packetMetadata.getTimestamp()))
                .subscribe();
    }

    private void dumpPacket(CassandraPacket packet, Instant timestamp) {
        packetLoadedCount++;
        pcapDumper.dumpOutput(packet.getPacket().array(), timestamp, this::handleFailure);

        if (loadFinished()) {
            pcapDumper.closeDumper();
            unblockIfNecessaryToSendAck();
        }
    }

    private boolean loadFinished() {
        return (packetMetadataLoadedCount == packetLoadedCount);
    }

    private void blockIfNecessaryUntilPacketsAreLoaded() {
        if (zeroRecords()) {
            return;
        }
        LOGGER.debug("BEFORE BLOCKING");
        try {
            semaphore.acquire();
        } catch (InterruptedException exception) {
            LOGGER.error(exception.getMessage(), exception);
        }
        LOGGER.debug("CONTINUE");
    }

    private void unblockIfNecessaryToSendAck() {
        if (zeroRecords()) {
            return;
        }
        LOGGER.debug("BEFORE RELEASING");
        semaphore.release();
        LOGGER.debug("AFTER RELEASING");
    }

    private boolean zeroRecords() {
        return (packetMetadataLoadedCount == 0 && packetLoadedCount == 0);
    }

    private Criteria ipv6Criteria() {
        List<KafkaCriteria> criteria = new ArrayList<>();
        KafkaCriteria ipVersionName = new KafkaCriteria.Builder()
                .field("ipVersionName").operation(MetadataOperation.EQ).value("IPv6").build();
        KafkaCriteria dstIpAddress = new KafkaCriteria.Builder()
                .field("dstIpAddress").operation(MetadataOperation.EQ).value("ff02:0:0:0:0:0:0:c").build();
        criteria.add(ipVersionName);
        criteria.add(dstIpAddress);
        Criteria mongoCriteria = prepareCriteria(criteria);
        return mongoCriteria;
    }

    private Criteria tcpAndPortCriteria() {
        List<KafkaCriteria> criteria = new ArrayList<>();
        KafkaCriteria tcp = new KafkaCriteria.Builder()
                .field("ipProtocolName").operation(MetadataOperation.EQ).value("TCP").build();
        KafkaCriteria portLte443 = new KafkaCriteria.Builder()
                .field("dstPort").operation(MetadataOperation.IN).values(Arrays.asList("443", "80")).build();
        criteria.add(tcp);
        criteria.add(portLte443);
        Criteria mongoCriteria = prepareCriteria(criteria);
        return mongoCriteria;
    }

    @Override
    public void run(String... strings) throws Exception {
        Criteria mongoCriteria = ipv6Criteria();
        loadPacketsByCriteria(mongoCriteria);
        LOGGER.debug(mongoCriteria.getCriteriaObject().toJson());
    }

}