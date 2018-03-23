package cz.vutbr.fit;

import cz.vutbr.fit.cassandra.repository.PacketRepository;
import cz.vutbr.fit.communication.KafkaCriteria;
import cz.vutbr.fit.communication.MetadataOperation;
import cz.vutbr.fit.mongodb.entity.PacketMetadata;
import cz.vutbr.fit.mongodb.repository.PacketMetadataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class TestDatabaseSpringBoot implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestDatabaseSpringBoot.class);

    private static final String JAVA_VERSION = "java.version";
    private static final String JAVA_VERSION_PROPERTY_VALUE_DEFAULT = "9";
    private static final String JAVA_VERSION_PROPERTY_VALUE_CUSTOM = "1.9";

    static {
        if (JAVA_VERSION_PROPERTY_VALUE_DEFAULT.equals(System.getProperty(JAVA_VERSION))) {
            System.setProperty(JAVA_VERSION, JAVA_VERSION_PROPERTY_VALUE_CUSTOM);
            LOGGER.info(JAVA_VERSION + "=" + System.getProperty(JAVA_VERSION));
        }
    }

    @Autowired
    PacketRepository packetRepository;

    @Autowired
    PacketMetadataRepository packetMetadataRepository;

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

    public void testDynamicCriteriaMongoDB(Criteria criteria) {
        List<PacketMetadata> list = packetMetadataRepository.findByDynamicCriteria(criteria)
                .doOnError(this::handleFailure)
                .doOnNext(packetMetadata -> LOGGER.debug(packetMetadata.toString()))
                .collectList().block();
        LOGGER.debug("List size: " + list.size());
    }

    public void packetMetadataAndPacketSelectReactive(Criteria criteria) {
        packetMetadataRepository.findByDynamicCriteria(criteria)
                .doOnError(this::handleFailure)
                .doOnNext(this::loadPacket)
                .subscribe();
    }

    private void loadPacket(PacketMetadata packetMetadata) {
        packetRepository.findById(packetMetadata.getRefId())
                .doOnNext(cassandraPacket -> LOGGER.info("Raw packet length: " + cassandraPacket.getPacket().array().length))
                .subscribe();
    }

    public void packetMetadataSelectReactive(Criteria criteria) {
        List<PacketMetadata> packetMetadataList = new ArrayList<>();
        packetMetadataRepository.findByDynamicCriteria(criteria)
                .doOnError(this::handleFailure)
                .doOnNext(packetMetadataList::add)
                .doOnComplete(() -> {
                    LOGGER.info("MongoDB records list size: " + packetMetadataList.size());
                    packetSelectReactive(packetMetadataList);
                })
                .subscribe();
    }

    public void packetSelectReactive(List<PacketMetadata> packetMetadataList) {
        packetMetadataList
                .forEach(
                        packetMetadata -> packetRepository.findById(packetMetadata.getRefId())
                                .doOnError(this::handleFailure)
                                .doOnNext(cassandraPacket -> LOGGER.info("Raw packet length: " + cassandraPacket.getPacket().array().length))
                                .subscribe()
                );
        /*packetMetadataList
                .forEach(
                        packetMetadata -> {
                            ResultSetFuture future = packetRepository.selectAsync(packetMetadata.getRefId());
                            Futures.addCallback(future,
                                    new FutureCallback<ResultSet>() {
                                        @Override
                                        public void onSuccess(ResultSet result) {
                                            ByteBuffer rawPacket = result.one().get("packet", ByteBuffer.class);
                                            LOGGER.info("Loaded: " + rawPacket.array());
                                            System.out.println("Loaded - " + rawPacket);
                                        }

                                        @Override
                                        public void onFailure(Throwable throwable) {
                                            handleFailure(throwable);
                                        }
                                    },
                                    MoreExecutors.newDirectExecutorService());
                        }
                );*/

        /*List<Row> cassandraPackets = packetMetadataList.stream()
                .map(packetMetadata -> packetRepository.selectAsync(packetMetadata.getRefId()))
                .collect(Collectors.toList())
                .stream()
                .map(ResultSetFuture::getUninterruptibly)
                .map(ResultSet::all)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        LOGGER.info("Cassandra records list size: " + cassandraPackets.size());*/
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
        //testDynamicCriteriaMongoDB(mongoCriteria);
        packetMetadataAndPacketSelectReactive(mongoCriteria);
        LOGGER.debug(mongoCriteria.getCriteriaObject().toJson());
    }

}