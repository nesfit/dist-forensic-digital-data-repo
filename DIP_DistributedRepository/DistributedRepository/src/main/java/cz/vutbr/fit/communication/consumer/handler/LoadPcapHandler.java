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

    @Override
    public void handleRequest(KafkaRequest kafkaRequest, byte[] bytes) {
        try {

            bufferRequest(request);
            loadPacketsByCriteria();

        } catch (Exception exception) {
            handleFailure(exception);
        }
    }

    private void loadPacketsByCriteria() {
        // TODO: Think about how to hand over path of result PCAP, maybe datasource in request can be reused.
        pcapDumper.initDumper(request.getDataSource().getUri(), this::handleFailure);

        Criteria metadataCriteria = prepareCriteria(request.getCriterias());
        packetMetadataRepository.findByDynamicCriteria(metadataCriteria)
                .doOnError(this::handleFailure)
                .doOnNext(this::loadPacket)
                .subscribe();
    }

    public void loadPackets(List<PacketMetadata> packetMetadataList) {
        packetMetadataList.forEach(
                packetMetadata -> packetRepository.findById(packetMetadata.getRefId())
                        .doOnError(this::handleFailure)
                        .doOnNext(cassandraPacket -> dumpPacket(cassandraPacket, packetMetadata.getTimestamp()))
                        .subscribe()
        );
    }

    private void loadPacket(PacketMetadata packetMetadata) {
        // TODO: Think about async loading or batch reactive.
        packetRepository.findById(packetMetadata.getRefId())
                .doOnNext(packet -> dumpPacket(packet, packetMetadata.getTimestamp()))
                .subscribe();
    }

    private void dumpPacket(CassandraPacket packet, Instant timestamp) {
        pcapDumper.dumpOutput(packet.getPacket().array(), timestamp, this::handleFailure);
    }

    private void handleFailure(Throwable throwable) {
        LOGGER.error(throwable.getMessage(), throwable);
        sendAcknowledgement(buildFailureResponse(request, throwable.getMessage()), new byte[]{});
    }

    private Criteria prepareCriteria(List<KafkaCriteria> kafkaCriterias) {
        Criteria criteria = new Criteria();
        kafkaCriterias.stream().forEach(
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
