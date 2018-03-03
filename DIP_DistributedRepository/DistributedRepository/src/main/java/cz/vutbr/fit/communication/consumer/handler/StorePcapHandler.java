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
import java.util.List;
import java.util.UUID;

@Component
public class StorePcapHandler extends BaseHandler {

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

    private KafkaRequest request;
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

            bufferRequest(request);
            storePayload(value);
            processPackets();
            removePayload();

            String detailMessage = "Successfully stored " + count + " packets";
            sendAcknowledgement(buildSuccessResponse(request, detailMessage), new byte[]{});

        } catch (Exception exception) {
            handleFailure(exception);
        }
    }

    private void bufferRequest(KafkaRequest request) {
        this.request = request;
    }

    private void storePayload(byte[] value) throws IOException {
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

        packetMetadataList = new ArrayList<>(maxListSize);
        pcapParser.parseInput(processedTmpFile, this::processPacket, this::storeMetadata, this::handleFailure);
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
    }

    private boolean shouldStoreMetadata() {
        return (packetMetadataList.size() == maxListSize);
    }

    private void storeMetadata() {
        List<PacketMetadata> metadataList = packetMetadataList;
        packetMetadataRepository
                .saveAll(metadataList)
                .doOnError(this::handleFailure)
                .doOnComplete(metadataList::clear)
                .subscribe();
    }

    private void handleFailure(Throwable throwable) {
        sendAcknowledgement(buildFailureResponse(request, throwable.getMessage()), new byte[]{});
    }

    private void removePayload() {
        DataSource dataSource = request.getDataSource();
        boolean isHadoopDataSource = DataSourceStorage.HADOOP == dataSource.getDataSourceStorage();
        boolean shouldRemoveFromHadoop = dataSource.getRemoveAfterUse();
        if (isHadoopDataSource && shouldRemoveFromHadoop) {
            hdfsShell.rm(dataSource.getUri());
        }
        FileManager.RemoveFile(processedTmpFile);
    }

}
