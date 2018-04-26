package cz.vutbr.fit.distributedrepository.communication.consumer.handler;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.core.utils.MoreFutures;
import com.datastax.driver.core.utils.UUIDs;
import com.google.common.util.concurrent.Futures;
import cz.vutbr.fit.communication.KafkaRequest;
import cz.vutbr.fit.communication.command.DataSource;
import cz.vutbr.fit.communication.command.DataSourceStorage;
import cz.vutbr.fit.distributedrepository.service.pcap.extractor.PacketExtractor;
import cz.vutbr.fit.distributedrepository.service.pcap.parser.PcapParser;
import cz.vutbr.fit.distributedrepository.util.FileManager;
import cz.vutbr.fit.persistence.DatabaseType;
import cz.vutbr.fit.persistence.cassandra.entity.CassandraPacket;
import cz.vutbr.fit.persistence.cassandra.repository.PacketRepository;
import cz.vutbr.fit.persistence.mongodb.entity.PacketMetadata;
import cz.vutbr.fit.persistence.mongodb.repository.PacketMetadataRepository;
import org.pcap4j.core.PcapPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(StorePcapHandler.class);

    // Repository
    @Autowired
    private PacketRepository packetRepository;
    @Autowired
    private PacketMetadataRepository packetMetadataRepository;

    // Parser
    @Autowired
    private PcapParser<PcapPacket> pcapParser;

    // Packet metadata extractor
    @Autowired
    private List<PacketExtractor<PcapPacket, PacketMetadata.Builder>> packetExtractor;

    // Batch metadata
    @Value("${packet.metadata.max.list.size}")
    private int maxListSize;
    private List<PacketMetadata> packetMetadataList;

    // Pcap4J parsing workaround
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

            bufferRequest(request);
            storePayload(value);
            processPackets();

            // Packets are processed, PCAP file can be removed
            removePayload();

        } catch (Exception exception) {
            handleFailure(exception);
        }
    }

    private void storePayload(byte[] value) throws IOException {
        processedTmpFile = FileManager.GenerateTmpPath(tmpDirectory);

        switch (request.getDataSource().getDataSourceStorage()) {
            case HDFS:
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
        pcapParser.parseInput(processedTmpFile, this::processPacket, this::packetsProcessed, this::handleFailure);

        LOGGER.debug("Packets processed successfully.");
    }

    private void processPacket(PcapPacket packet) {
        count++;

        UUID id = UUIDs.timeBased();
        CassandraPacket cassandraPacket = new CassandraPacket.Builder().id(id).packet(ByteBuffer.wrap(packet.getRawData())).build();
        ResultSetFuture resultSetFuture = packetRepository.insertAsync(cassandraPacket);
        Futures.addCallback(resultSetFuture, new MoreFutures.FailureCallback<ResultSet>() {
            @Override
            public void onFailure(Throwable throwable) {
                handleFailure(throwable);
            }
        });

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
        LOGGER.error(throwable.getMessage(), throwable);
        sendAcknowledgement(buildFailureResponse(request, throwable.getMessage()), new byte[]{});
    }

    private void packetsProcessed() {
        // Last bulk of metadata records
        storeMetadata();

        // If client awaits response, it will be sent
        if (request.getAwaitsResponse()) {
            String detailMessage = String.format("Successfully stored %d packets", count);
            sendAcknowledgement(buildSuccessResponse(request, detailMessage), new byte[]{});
        }
    }

    private void removePayload() {
        DataSource dataSource = request.getDataSource();
        boolean isHadoopDataSource = DataSourceStorage.HDFS == dataSource.getDataSourceStorage();
        boolean shouldRemoveFromHadoop = dataSource.getRemoveAfterUse();
        if (isHadoopDataSource && shouldRemoveFromHadoop) {
            hdfsShell.rm(dataSource.getUri());
        }
        FileManager.RemoveFile(processedTmpFile);
    }

}
