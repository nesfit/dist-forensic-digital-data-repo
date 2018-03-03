package cz.vutbr.fit.communication.consumer.handler;

import com.datastax.driver.core.utils.UUIDs;
import cz.vutbr.fit.DatabaseType;
import cz.vutbr.fit.cassandra.entity.CassandraPacket;
import cz.vutbr.fit.cassandra.repository.PacketRepository;
import cz.vutbr.fit.communication.KafkaRequest;
import cz.vutbr.fit.communication.command.DataSource;
import cz.vutbr.fit.communication.command.DataSourceStorage;
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
public class StorePcapHandler extends BaseHandler implements ICommandHandler<KafkaRequest, byte[]> {

    // Parser
    @Autowired
    private PcapParser<Packet> pcapParser;

    // Repository
    @Autowired
    private PacketRepository packetRepository;
    @Autowired
    private PacketMetadataRepository packetMetadataRepository;

    // Packet metadata extractor
    @Autowired
    private List<PacketExtractor<Packet, PacketMetadata.Builder>> packetExtractor;

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

            storePayload(request, value);
            processPackets();
            removePayload(request);

            String detailMessage = "Successfully stored " + count + " packets";
            sendAcknowledgement(buildSuccessResponse(request, detailMessage), new byte[]{});

        } catch (Exception e) {
            sendAcknowledgement(buildFailureResponse(request, e.getMessage()), new byte[]{});
        }
    }

    private void storePayload(KafkaRequest request, byte[] value) throws IOException {
        processedTmpFile = FileManager.GenerateTmpPath(tmpDirectory);

        switch (request.getDataSource().getDataSourceStorage()) {
            case HADOOP:
                hdfsShell.get(request.getDataSource().getUri(), processedTmpFile);
                break;
            case KAFKA:
                FileManager.SaveContent(processedTmpFile, value);
                break;
        }
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

    private void removePayload(KafkaRequest request) {
        DataSource dataSource = request.getDataSource();
        boolean isHadoop = DataSourceStorage.HADOOP == dataSource.getDataSourceStorage();
        boolean removeFromHadoop = dataSource.getRemoveAfterUse();
        if (isHadoop && removeFromHadoop) {
            hdfsShell.rm(dataSource.getUri());
        }
        FileManager.RemoveFile(processedTmpFile);
    }

}
