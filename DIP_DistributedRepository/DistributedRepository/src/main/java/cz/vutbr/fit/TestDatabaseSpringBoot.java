package cz.vutbr.fit;

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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    private long packetsToLoad;
    private long packetsLoaded;

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
        packetsToLoad = 0;
        packetsLoaded = 0;

        pcapDumper.initDumper("file.pcap", this::handleFailure);

        packetsToLoad = packetMetadataRepository.findByDynamicCriteria(criteria)
                .doOnError(this::handleFailure)
                .doOnNext(this::loadPacket)
                .count().block();

        if (packetsToLoad == 0) {
            LOGGER.debug("Zero packets loaded, closing dumper.");
            pcapDumper.closeDumper();
        }
    }

    private void loadPacket(PacketMetadata packetMetadata) {
        packetRepository.selectAsync(packetMetadata.getRefId(),
                cassandraPacket -> {
                    dumpPacket(cassandraPacket, packetMetadata.getTimestamp());
                    packetsLoaded++;

                    if (loadingFinished()) {
                        onFinishLoaded();
                    }
                });
    }

    private void dumpPacket(CassandraPacket packet, Instant timestamp) {
        pcapDumper.dumpOutput(packet.getPacket().array(), timestamp, this::handleFailure);
    }

    private boolean loadingFinished() {
        return packetsToLoad != 0 && packetsToLoad == packetsLoaded;
    }

    private void onFinishLoaded() {
        LOGGER.debug(String.format("Packets to load: %d, successfully loaded %d packets, " +
                "closing dumper.", packetsToLoad, packetsLoaded));
        pcapDumper.closeDumper();
    }

    private Criteria ipv4Criteria() {
        List<KafkaCriteria> criteria = new ArrayList<>();
        KafkaCriteria ipVersionName = new KafkaCriteria.Builder()
                .field("ipVersionName")
                .operation(MetadataOperation.EQ)
                .value("IPv4")
                .build();
        KafkaCriteria dstIpAddress = null;
        try {
            dstIpAddress = new KafkaCriteria.Builder()
                    .field("dstIpAddress")
                    .operation(MetadataOperation.EQ)
                    .value(InetAddress.getByName("192.168.1.1").toString())
                    .build();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        criteria.add(ipVersionName);
        criteria.add(dstIpAddress);
        Criteria mongoCriteria = prepareCriteria(criteria);
        return mongoCriteria;
    }

    private Criteria ipv6Criteria() {
        List<KafkaCriteria> criteria = new ArrayList<>();
        KafkaCriteria ipVersionName = new KafkaCriteria.Builder()
                .field("ipVersionName")
                .operation(MetadataOperation.EQ)
                .value("IPv6")
                .build();
        KafkaCriteria dstIpAddress = null;
        try {
            dstIpAddress = new KafkaCriteria.Builder()
                    .field("dstIpAddress")
                    .operation(MetadataOperation.EQ)
                    .value(InetAddress.getByName("ff02::c").toString())
                    .build();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        criteria.add(ipVersionName);
        criteria.add(dstIpAddress);
        Criteria mongoCriteria = prepareCriteria(criteria);
        return mongoCriteria;
    }

    private Criteria tcpAndPortCriteria() {
        List<KafkaCriteria> criteria = new ArrayList<>();
        KafkaCriteria tcp = new KafkaCriteria.Builder()
                .field("ipProtocolName").operation(MetadataOperation.EQ).value("TCP").build();
        KafkaCriteria ports = new KafkaCriteria.Builder()
                .field("dstPort").operation(MetadataOperation.IN).values(Arrays.asList(443, 80)).build();
        criteria.add(tcp);
        criteria.add(ports);
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