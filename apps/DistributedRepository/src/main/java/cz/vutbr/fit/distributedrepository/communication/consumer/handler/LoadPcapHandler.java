package cz.vutbr.fit.distributedrepository.communication.consumer.handler;

import cz.vutbr.fit.communication.KafkaCriteria;
import cz.vutbr.fit.communication.KafkaRequest;
import cz.vutbr.fit.distributedrepository.service.pcap.dumper.PcapDumper;
import cz.vutbr.fit.distributedrepository.util.FileManager;
import cz.vutbr.fit.persistence.cassandra.entity.CassandraPacket;
import cz.vutbr.fit.persistence.cassandra.repository.PacketRepository;
import cz.vutbr.fit.persistence.mongodb.entity.PacketMetadata;
import cz.vutbr.fit.persistence.mongodb.repository.PacketMetadataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class LoadPcapHandler extends BaseHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoadPcapHandler.class);

    // Repository
    @Autowired
    private PacketRepository packetRepository;
    @Autowired
    private PacketMetadataRepository packetMetadataRepository;

    // Dumper
    @Autowired
    private PcapDumper<byte[]> pcapDumper;

    private String resultingPcapFile;

    private long packetsToLoad;
    private long packetsLoaded;

    @Override
    public void handleRequest(KafkaRequest request, byte[] bytes) {
        try {

            bufferRequest(request);
            loadPacketsByCriteria();

        } catch (Exception exception) {
            handleFailure(exception);
        }
    }

    private void loadPacketsByCriteria() {
        packetsToLoad = 0;
        packetsLoaded = 0;

        resultingPcapFile = request.getDataSource().getUri();
        pcapDumper.initDumper(resultingPcapFile, this::handleFailure);

        Criteria criteria = prepareCriteria(request.getCriterias());
        packetsToLoad = packetMetadataRepository.findByDynamicCriteria(criteria)
                .doOnError(this::handleFailure)
                .doOnNext(this::loadPacket)
                .count().block();

        if (packetsToLoad == 0) {
            onFinishLoaded();
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

        String localFile, dstFile;
        localFile = dstFile = resultingPcapFile;
        storePayloadIntoHDFS(localFile, dstFile);

        removeTmpFile(localFile);

        if (request.getAwaitsResponse()) {
            acknowledge();
        }
    }

    private void storePayloadIntoHDFS(String localFile, String dstFile) {
        hdfsShell.put(localFile, dstFile);
        LOGGER.debug("Result PCAP file stored into HDFS.");
    }

    private void removeTmpFile(String localFile) {
        FileManager.RemoveFile(localFile);
        LOGGER.debug("Tmp file removed.");
    }

    private void acknowledge() {
        String detailMessage = String.format("Successfully loaded %d packets.", packetsLoaded);
        sendAcknowledgement(buildSuccessResponse(request, detailMessage), new byte[]{});
    }

    private void handleFailure(Throwable throwable) {
        LOGGER.error(throwable.getMessage(), throwable);
        sendAcknowledgement(buildFailureResponse(request, throwable.getMessage()), new byte[]{});
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

}
