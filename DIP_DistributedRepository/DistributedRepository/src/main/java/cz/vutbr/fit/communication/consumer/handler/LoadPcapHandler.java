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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

    private void loadPacket(PacketMetadata packetMetadata) {
        // TODO: Think about async loading or batch reactive.
        packetRepository.findById(packetMetadata.getRefId())
                .doOnNext(this::dumpPacket);
    }

    private void dumpPacket(CassandraPacket packet) {
        pcapDumper.dumpOutput(packet.getPacket().array(), this::handleFailure);
    }

    private void handleFailure(Throwable throwable) {
        LOGGER.error(throwable.getMessage(), throwable);
        sendAcknowledgement(buildFailureResponse(request, throwable.getMessage()), new byte[]{});
    }

    // TODO: Move to separated class (can't be inside Persistence module because of KafkaCriteria parameter).
    private Criteria prepareCriteria(List<KafkaCriteria> kafkaCriterias) {
        Criteria criteria = new Criteria();
        kafkaCriterias.stream().forEach(kafkaCriteria -> appendCriteria(criteria, kafkaCriteria));
        return criteria;
    }

    private Criteria appendCriteria(Criteria criteriaBuilder, KafkaCriteria kafkaCriteria) {
        String and = "and";
        try {
            Method whichField = criteriaBuilder.getClass().getMethod(and, String.class);
            criteriaBuilder = (Criteria) whichField.invoke(criteriaBuilder, kafkaCriteria.getField());
            Method operation = getMethodOperation(criteriaBuilder, kafkaCriteria);
            criteriaBuilder = invokeMethodOperation(operation, criteriaBuilder, kafkaCriteria);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException exception) {
            handleFailure(exception);
        }
        return criteriaBuilder;
    }

    private Method getMethodOperation(Criteria criteriaBuilder, KafkaCriteria kafkaCriteria) throws NoSuchMethodException {
        Class<?> paramType = kafkaCriteria.getOperation().isArrayRequired() ? Object[].class : Object.class;
        return criteriaBuilder.getClass().getMethod(kafkaCriteria.getOperation().getOperationAsString(), paramType);
    }

    private Criteria invokeMethodOperation(Method operation, Criteria criteriaBuilder, KafkaCriteria kafkaCriteria) throws InvocationTargetException, IllegalAccessException {
        if (kafkaCriteria.getOperation().isArrayRequired()) {
            return (Criteria) operation.invoke(criteriaBuilder, new Object[]{kafkaCriteria.getValues().toArray()});
        } else {
            return (Criteria) operation.invoke(criteriaBuilder, kafkaCriteria.getValue());
        }
    }

}
