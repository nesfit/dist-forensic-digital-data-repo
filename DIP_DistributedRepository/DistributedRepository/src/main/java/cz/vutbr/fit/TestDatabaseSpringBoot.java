package cz.vutbr.fit;

import com.datastax.driver.core.utils.UUIDs;
import cz.vutbr.fit.cassandra.entity.Packet;
import cz.vutbr.fit.cassandra.repository.PacketRepository;
import cz.vutbr.fit.mongodb.entity.PacketMetadata;
import cz.vutbr.fit.mongodb.repository.PacketMetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.nio.ByteBuffer;
import java.util.UUID;

//@SpringBootApplication
//@ComponentScan(basePackages = {"cz.vutbr.fit.cassandra.repository", "cz.vutbr.fit.mongodb.repository"})
//@EnableJpaRepositories(basePackages = {"cz.vutbr.fit.cassandra.repository", "cz.vutbr.fit.mongodb.repository"})
//@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class TestDatabaseSpringBoot implements CommandLineRunner {

    @Autowired
    PacketRepository packetRepository;

    @Autowired
    PacketMetadataRepository packetMetadataRepository;

    public void testCassandra() {
        Packet packet = new Packet();
        packet.setId(UUIDs.timeBased());
        packet.setPacket(ByteBuffer.wrap("237283278".getBytes()));
        packetRepository.save(packet);

        Iterable<Packet> packetList = packetRepository.findAll();
        System.out.println("Packet List : ");
        packetList.forEach(System.out::println);
    }

    public void testAsyncCassandra() {
        Packet packet = new Packet();
        packet.setId(UUIDs.timeBased());
        packet.setPacket(ByteBuffer.wrap("237283278".getBytes()));
        packetRepository.insertAsync(packet);

        packet = packetRepository.findByPacketId(packet.getId());
        System.out.println("Packet : ");
        System.out.println(packet);
    }

    public void testMongoDB() {
        PacketMetadata packetMetadata = new PacketMetadata();
        packetMetadata.setRefId(UUID.randomUUID());
        packetMetadata.setSrcIpAddress("124.23.04.11");
        packetMetadata.setDstIpAddress("145.95.72.88");
        packetMetadataRepository.save(packetMetadata);

        Iterable<PacketMetadata> packetMetadataList = packetMetadataRepository.findAll();
        System.out.println("PacketMetadata List : ");
        packetMetadataList.forEach(System.out::println);
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
        testAsyncCassandra();
        testMongoDB();
    }

}