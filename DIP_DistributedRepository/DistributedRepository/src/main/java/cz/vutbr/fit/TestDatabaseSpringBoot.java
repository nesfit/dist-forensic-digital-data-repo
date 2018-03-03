package cz.vutbr.fit;

import com.datastax.driver.core.utils.UUIDs;
import cz.vutbr.fit.cassandra.entity.CassandraPacket;
import cz.vutbr.fit.cassandra.repository.PacketRepository;
import cz.vutbr.fit.mongodb.entity.PacketMetadata;
import cz.vutbr.fit.mongodb.repository.PacketMetadataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.nio.ByteBuffer;
import java.util.Date;
import java.util.List;
import java.util.UUID;

//@SpringBootApplication
//@ComponentScan(basePackages = {"cz.vutbr.fit.cassandra.repository", "cz.vutbr.fit.mongodb.repository"})
//@EnableJpaRepositories(basePackages = {"cz.vutbr.fit.cassandra.repository", "cz.vutbr.fit.mongodb.repository"})
//@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class TestDatabaseSpringBoot implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestDatabaseSpringBoot.class);

    @Autowired
    PacketRepository packetRepository;

    @Autowired
    PacketMetadataRepository packetMetadataRepository;

    public void testCassandra() {
        CassandraPacket packet = new CassandraPacket();
        packet.setId(UUIDs.timeBased());
        packet.setPacket(ByteBuffer.wrap("237283278".getBytes()));
        packetRepository.save(packet);

        Iterable<CassandraPacket> packetList = packetRepository.findAll();
        LOGGER.debug("Packet List : ");
        packetList.forEach(record -> LOGGER.debug(record.toString()));
    }

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

    public void testMongoDB() {
        LOGGER.debug("Save operation");
        String someSrcIpAddress = "124.23.04.11";
        String someDstIpAddress = "145.95.72.88";
        PacketMetadata packetMetadata = new PacketMetadata.Builder().refId(UUID.randomUUID())
                .databaseType(DatabaseType.Cassandra).srcIpAddress(someSrcIpAddress).dstIpAddress(someDstIpAddress).build();
        packetMetadataRepository.save(packetMetadata).block();

        LOGGER.debug("Load operation");
        List<PacketMetadata> list = packetMetadataRepository.findAll().collectList().block();
        list.forEach(record -> LOGGER.debug(record.toString()));
        //Flux<PacketMetadata> packetMetadataList = packetMetadataRepository.findAll();
        //packetMetadataList.doOnEach(flux -> LOGGER.debug(flux.get())).subscribe();
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(TestDatabaseSpringBoot.class)
                .web(WebApplicationType.NONE)
                .bannerMode(Banner.Mode.OFF)
                .build()
                .run(args);
    }

    @Override
    public void run(String... strings) throws Exception {
        //testInsertAsyncCassandra();
        testMongoDB();
    }

}