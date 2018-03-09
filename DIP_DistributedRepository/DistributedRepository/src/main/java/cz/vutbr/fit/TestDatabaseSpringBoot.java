package cz.vutbr.fit;

import com.datastax.driver.core.utils.UUIDs;
import cz.vutbr.fit.cassandra.entity.CassandraPacket;
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
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.mongodb.core.query.Criteria;
import reactor.core.publisher.Flux;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.*;

//@SpringBootApplication
//@ComponentScan(basePackages = {"cz.vutbr.fit.cassandra.repository", "cz.vutbr.fit.mongodb.repository"})
//@EnableJpaRepositories(basePackages = {"cz.vutbr.fit.cassandra.repository", "cz.vutbr.fit.mongodb.repository"})
//@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class TestDatabaseSpringBoot implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestDatabaseSpringBoot.class);

    private static final String JAVA_VERSION = "java.version";

    static {
        if ("9".equals(System.getProperty(JAVA_VERSION))) {
            System.setProperty(JAVA_VERSION, "1.9");
            LOGGER.info(System.getProperty(JAVA_VERSION));
        }
    }

    @Autowired
    PacketRepository packetRepository;

    @Autowired
    PacketMetadataRepository packetMetadataRepository;

    public void testInsertAsyncCassandra() {
        Date start = new Date();
        for (int i = 0; i < 5000; i++) {
            CassandraPacket packet = new CassandraPacket();
            packet.setId(UUIDs.timeBased());
            packet.setPacket(ByteBuffer.wrap("237283278".getBytes()));
            packetRepository.insertAsync(packet);
        }
        Date end = new Date();

        LOGGER.debug("Time consumed: " + (end.getTime() - start.getTime()) / 1000);
    }

    public void testSaveAndLoadMongoDB() {
        LOGGER.debug("Save operation");
        String someSrcIpAddress = "124.23.04.11";
        String someDstIpAddress = "145.95.72.88";
        PacketMetadata packetMetadata = new PacketMetadata.Builder().refId(UUID.randomUUID())
                .databaseType(DatabaseType.Cassandra).srcIpAddress(someSrcIpAddress)
                .dstIpAddress(someDstIpAddress).build();
        packetMetadataRepository.save(packetMetadata).block();

        LOGGER.debug("Load operation");
        List<PacketMetadata> list = packetMetadataRepository.findAll().collectList().block();
        list.forEach(record -> LOGGER.debug(record.toString()));
    }

    public void testReactiveMongoDB() {
        packetMetadataRepository.findAll()
                .doOnNext(packetMetadata -> LOGGER.debug(packetMetadata.toString()))
                .subscribe();
    }

    private Criteria prepareCriteria(List<KafkaCriteria> kafkaCriterias) {
        Criteria criteria = new Criteria();
        kafkaCriterias.stream().forEach(kafkaCriteria -> appendCriteria(criteria, kafkaCriteria));
        return criteria;
    }

    private Criteria appendCriteria(Criteria criteriaBuilder, KafkaCriteria kafkaCriteria) {
        String and = "and";
        try {
            Method whichField = criteriaBuilder.getClass().getMethod(and, String.class);
            criteriaBuilder = (Criteria) whichField.invoke(criteriaBuilder, kafkaCriteria.getField());
            Method operation = getMethodOperation(criteriaBuilder, kafkaCriteria);
            criteriaBuilder = invokeMethodOperation(operation, criteriaBuilder, kafkaCriteria);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException exception) {
            LOGGER.error(exception.getMessage(), exception);
        }
        return criteriaBuilder;
    }

    private Method getMethodOperation(Criteria criteriaBuilder, KafkaCriteria kafkaCriteria) throws NoSuchMethodException {
        Class<?> paramType = kafkaCriteria.getOperation().isArrayRequired() ? Object[].class : Object.class;
        return criteriaBuilder.getClass().getMethod(kafkaCriteria.getOperation().getOperationAsString(), paramType);
    }

    private Criteria invokeMethodOperation(Method operation, Criteria criteriaBuilder, KafkaCriteria kafkaCriteria) throws InvocationTargetException, IllegalAccessException {
        if (kafkaCriteria.getOperation().isArrayRequired()) {
            return (Criteria) operation.invoke(criteriaBuilder, new Object[]{kafkaCriteria.getValues().toArray()});
        } else {
            return (Criteria) operation.invoke(criteriaBuilder, kafkaCriteria.getValue());
        }
    }

    public void testDynamicCriteriaMongoDB(Criteria criteria) {
        Flux<PacketMetadata> packetMetadataFlux = packetMetadataRepository.findByDynamicCriteria(criteria);
        List<PacketMetadata> list = packetMetadataFlux
                .doOnEach(packetMetadata -> LOGGER.debug(packetMetadata.toString()))
                .collectList().block();
        LOGGER.debug("List size: " + list.size());
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(TestDatabaseSpringBoot.class)
                .web(WebApplicationType.NONE)
                .bannerMode(Banner.Mode.OFF)
                .build()
                .run(args);
    }

    private Criteria ipv6Criteria() {
        List<KafkaCriteria> criteria = new ArrayList<>();
        KafkaCriteria ipVersionName = new KafkaCriteria.Builder()
                .field("ipVersionName").operation(MetadataOperation.EQ).values(Arrays.asList("IPv6")).build();
        KafkaCriteria dstIpAddress = new KafkaCriteria.Builder()
                .field("dstIpAddress").operation(MetadataOperation.EQ).values(Arrays.asList("ff02:0:0:0:0:0:0:c")).build();
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
        Criteria mongoCriteria = tcpAndPortCriteria();
        testDynamicCriteriaMongoDB(mongoCriteria);
        LOGGER.debug(mongoCriteria.getCriteriaObject().toJson());

        /*Criteria criteriaBuilder = Criteria.where("nevim");
        Method operation = criteriaBuilder.getClass().getMethod("in", Object[].class);
        criteriaBuilder = (Criteria) operation.invoke(criteriaBuilder, new Object[] {Arrays.asList("443", "80").toArray()});
        LOGGER.debug(criteriaBuilder.getCriteriaObject().toJson());*/
    }

}