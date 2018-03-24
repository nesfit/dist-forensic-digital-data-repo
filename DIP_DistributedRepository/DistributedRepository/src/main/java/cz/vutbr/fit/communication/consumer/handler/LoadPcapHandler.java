package cz.vutbr.fit.communication.consumer.handler;

import cz.vutbr.fit.cassandra.entity.CassandraPacket;
import cz.vutbr.fit.cassandra.repository.PacketRepository;
import cz.vutbr.fit.communication.KafkaCriteria;
import cz.vutbr.fit.communication.KafkaRequest;
import cz.vutbr.fit.mongodb.entity.PacketMetadata;
import cz.vutbr.fit.mongodb.repository.PacketMetadataRepository;
import cz.vutbr.fit.service.pcap.dumper.PcapDumper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.Semaphore;

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

    private int packetMetadataLoadedCount;
    private int packetLoadedCount;

    private Semaphore beforeAckSemaphore;

    @Override
    public void handleRequest(KafkaRequest request, byte[] bytes) {
        try {

            initializeSemaphore();

            bufferRequest(request);
            loadPacketsByCriteria();

            // TODO: Store response PCAP payload into HDFS instead of local drive

        } catch (Exception exception) {
            handleFailure(exception);
        }
    }

    private void initializeSemaphore() {
        beforeAckSemaphore = new Semaphore(0);
    }

    private void loadPacketsByCriteria() {
        packetMetadataLoadedCount = 0;
        packetLoadedCount = 0;
        pcapDumper.initDumper(request.getDataSource().getUri(), this::handleFailure);

        Criteria metadataCriteria = prepareCriteria(request.getCriterias());
        packetMetadataRepository.findByDynamicCriteria(metadataCriteria)
                .doOnError(this::handleFailure)
                .doOnNext(this::loadPacket)
                .doOnComplete(() -> acknowledge())
                .subscribe();
    }

    private void loadPacket(PacketMetadata packetMetadata) {
        packetMetadataLoadedCount++;
        packetRepository.findById(packetMetadata.getRefId())
                .doOnNext(packet -> dumpPacket(packet, packetMetadata.getTimestamp()))
                .subscribe();
    }

    private void dumpPacket(CassandraPacket packet, Instant timestamp) {
        packetLoadedCount++;
        pcapDumper.dumpOutput(packet.getPacket().array(), timestamp, this::handleFailure);

        // TODO: Is this safe?
        if (loadFinished()) {
            pcapDumper.closeDumper();
            unblockIfNecessaryToSendAck();
        }
    }

    private boolean loadFinished() {
        return (packetMetadataLoadedCount == packetLoadedCount);
    }

    private void acknowledge() {
        blockIfNecessaryUntilPacketsAreLoaded();

        String detailMessage = "Successfully loaded " + packetMetadataLoadedCount + " packets";
        sendAcknowledgement(buildSuccessResponse(request, detailMessage), new byte[]{});
    }

    private void blockIfNecessaryUntilPacketsAreLoaded() {
        if (zeroRecords()) {
            return;
        }
        try {
            beforeAckSemaphore.acquire();
        } catch (InterruptedException exception) {
            LOGGER.error(exception.getMessage(), exception);
        }
    }

    private void unblockIfNecessaryToSendAck() {
        if (zeroRecords()) {
            return;
        }
        beforeAckSemaphore.release();
    }

    private boolean zeroRecords() {
        return (packetMetadataLoadedCount == 0 && packetLoadedCount == 0);
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
