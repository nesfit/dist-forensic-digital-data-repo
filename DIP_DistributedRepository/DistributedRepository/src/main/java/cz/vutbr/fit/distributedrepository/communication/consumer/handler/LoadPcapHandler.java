package cz.vutbr.fit.distributedrepository.communication.consumer.handler;

import cz.vutbr.fit.communication.KafkaCriteria;
import cz.vutbr.fit.communication.KafkaRequest;
import cz.vutbr.fit.distributedrepository.service.pcap.dumper.PcapDumper;
import cz.vutbr.fit.persistence.cassandra.entity.CassandraPacket;
import cz.vutbr.fit.persistence.cassandra.repository.PacketRepository;
import cz.vutbr.fit.persistence.mongodb.entity.PacketMetadata;
import cz.vutbr.fit.persistence.mongodb.repository.PacketMetadataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;

import java.time.Instant;
import java.util.List;

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

    private int packetsToLoad;
    private int packetsLoaded;
    private PacketMetadata lastPacketToLoad;

    @Override
    public void handleRequest(KafkaRequest request, byte[] bytes) {
        try {

            bufferRequest(request);
            loadPacketsByCriteria();

            // TODO: Store response PCAP payload into HDFS instead of local drive

        } catch (Exception exception) {
            handleFailure(exception);
        }
    }

    private void loadPacketsByCriteria() {
        packetsToLoad = 0;
        packetsLoaded = 0;
        lastPacketToLoad = null;

        pcapDumper.initDumper(request.getDataSource().getUri(), this::handleFailure);

        Criteria criteria = prepareCriteria(request.getCriterias());
        //lastPacketToLoad =
        packetsToLoad = packetMetadataRepository.findByDynamicCriteria(criteria)
                .doOnError(this::handleFailure)
                .doOnNext(packetMetadata -> {
                    //packetsToLoad++;
                    loadPacket(packetMetadata);
                })
                .count().block().intValue();
                //.blockLast();
        /* The call blockLast() is very important - it waits until all items in Flux are emitted
         (we can find out last record which needs to be loaded from Cassandra). */

        LOGGER.info(String.format("LastPacketToLoad %s", lastPacketToLoad));
        if (packetsToLoad == 0) {
            LOGGER.info("Zero packets loaded, closing dumper.");
            //pcapDumper.closeDumper();
        }
    }

    private void loadPacket(PacketMetadata packetMetadata) {
        packetRepository.selectAsync(packetMetadata.getRefId(),
                cassandraPacket -> {
                    dumpPacket(cassandraPacket, packetMetadata.getTimestamp());
                    packetsLoaded++;

                    // TODO: Wrap into another callback... e.g. onSuccessLoad()
                    //if (packetMetadata.equals(lastPacketToLoad)) {
                    if (packetsToLoad != 0 && packetsToLoad == packetsLoaded) {
                        LOGGER.info(String.format("Packets to load: %d, " +
                                "successfully loaded %d packets, " +
                                "PacketMetadata: %s, " +
                                "closing dumper.", packetsToLoad, packetsLoaded, packetMetadata.toString()));
                        pcapDumper.closeDumper();

                        acknowledge();
                        //System.exit(0);
                    }
                });
    }

    private void dumpPacket(CassandraPacket packet, Instant timestamp) {
        pcapDumper.dumpOutput(packet.getPacket().array(), timestamp, this::handleFailure);
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
