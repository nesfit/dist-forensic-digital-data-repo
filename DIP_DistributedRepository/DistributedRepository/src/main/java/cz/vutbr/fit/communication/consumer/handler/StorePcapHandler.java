package cz.vutbr.fit.communication.consumer.handler;

import com.datastax.driver.core.utils.UUIDs;
import cz.vutbr.fit.DatabaseType;
import cz.vutbr.fit.cassandra.entity.CassandraPacket;
import cz.vutbr.fit.cassandra.repository.PacketRepository;
import cz.vutbr.fit.communication.KafkaRequest;
import cz.vutbr.fit.communication.KafkaResponse;
import cz.vutbr.fit.communication.ResponseCode;
import cz.vutbr.fit.communication.producer.AcknowledgementProducer;
import cz.vutbr.fit.mongodb.entity.PacketMetadata;
import cz.vutbr.fit.mongodb.repository.PacketMetadataRepository;
import cz.vutbr.fit.service.pcap.extractor.PacketExtractor;
import cz.vutbr.fit.service.pcap.parser.PcapParser;
import cz.vutbr.fit.util.FileManager;
import org.pcap4j.packet.Packet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class StorePcapHandler implements ICommandHandler<KafkaRequest, byte[]> {

    // Repository
    @Autowired
    private PacketRepository packetRepository;
    @Autowired
    private PacketMetadataRepository packetMetadataRepository;

    // Parser
    @Autowired
    private PcapParser<Packet> pcapParser;

    // Packet metadata extractor
    @Autowired
    private List<PacketExtractor<Packet, PacketMetadata.Builder>> packetExtractor;

    // Response producer
    @Autowired
    private AcknowledgementProducer acknowledgementProducer;

    // Batch metadata
    @Value("${packet.metadata.max.list.size}")
    private int maxListSize;
    private List<PacketMetadata> packetMetadataList;

    // Pcap4J workaround
    @Value("${tmp.directory}")
    private String tmpDirectory;
    private String processedTmpFile;

    private int count;

    @PostConstruct
    public void init() {
        postConstructValidation();
    }

    private void postConstructValidation() {
        Assert.isTrue(!(maxListSize == 0), "packet.metadata.max.list.size property is not set");
        Assert.notNull(tmpDirectory, "tmp.directory property is not set");
    }

    @Override
    public void handleRequest(KafkaRequest request, byte[] value) {
        try {

            storePayload(value);
            processPackets();
            removePayload();

            // TODO: Remove hardcoded values
            sendAcknowledgement(buildResponse(request), "Response OK".getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void storePayload(byte[] value) throws IOException {
        processedTmpFile = FileManager.GenerateTmpPath(tmpDirectory);
        FileManager.SaveContent(processedTmpFile, value);
    }

    private void processPackets() throws IOException {
        count = 0;
        Date startTime = new Date();

        packetMetadataList = new ArrayList<>(maxListSize);
        pcapParser.parseInput(processedTmpFile, this::processPacket, this::storeMetadata);

        // Saving the whole list of unknown count of packets demands lot of memory
        //packetMetadataRepository.saveAll(packetMetadataList).doOnError(Throwable::printStackTrace).subscribe();

        Date endTime = new Date();
        System.out.println(count + " packets processed in " + ((endTime.getTime() - startTime.getTime()) / 1000) + " seconds");
    }

    private void processPacket(Packet packet) {
        count++;

        UUID id = UUIDs.timeBased();
        CassandraPacket cassandraPacket = new CassandraPacket.Builder().id(id).packet(ByteBuffer.wrap(packet.getRawData())).build();
        packetRepository.insertAsync(cassandraPacket);

        PacketMetadata.Builder builder = new PacketMetadata.Builder();
        packetExtractor.forEach(l -> l.extractMetadata(packet, builder));

        PacketMetadata packetMetadata = builder.refId(id).databaseType(DatabaseType.Cassandra).build();
        packetMetadataList.add(packetMetadata);

        if (shouldStoreMetadata()) {
            storeMetadata();
            packetMetadataList = new ArrayList<>(maxListSize);
        }

        // Saving one by one is very slow
        //packetMetadataRepository.save(packetMetadata).doOnError(Throwable::printStackTrace).subscribe();
    }

    private boolean shouldStoreMetadata() {
        return (packetMetadataList.size() == maxListSize);
    }

    private void storeMetadata() {
        List<PacketMetadata> metadataList = packetMetadataList;
        packetMetadataRepository
                .saveAll(metadataList)
                .doOnError(Throwable::printStackTrace)
                .doOnComplete(metadataList::clear)
                .subscribe();
    }

    private void removePayload() {
        FileManager.RemoveFile(processedTmpFile);
    }

    private KafkaResponse buildResponse(KafkaRequest request) {
        return new KafkaResponse.Builder().id(request.getId())
                .responseTopic(request.getResponseTopic()).responseCode(ResponseCode.OK)
                .status("OK").detailMessage("Successfully stored " + count + " packets").build();
    }

    private void sendAcknowledgement(KafkaResponse response, byte[] value) {
        acknowledgementProducer.produce(response.getResponseTopic(), response, value);
    }

}
