package cz.vutbr.fit.communication.consumer.handler;

import cz.vutbr.fit.communication.KafkaRequest;
import cz.vutbr.fit.communication.KafkaResponse;
import cz.vutbr.fit.communication.ResponseCode;
import cz.vutbr.fit.communication.producer.ResponseProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.fs.FsShell;

public abstract class BaseHandler implements ICommandHandler<KafkaRequest, byte[]> {

    // Response producer
    @Autowired
    protected ResponseProducer responseProducer;

    // HDFS shell
    @Autowired
    protected FsShell hdfsShell;

    protected KafkaRequest request;

    protected void bufferRequest(KafkaRequest request) {
        this.request = request;
    }

    protected void sendAcknowledgement(KafkaResponse response, byte[] value) {
        responseProducer.produce(response.getResponseTopic(), response, value);
    }

    protected KafkaResponse buildSuccessResponse(KafkaRequest request, String detailMessage) {
        return buildResponse(request, request.getResponseTopic(), ResponseCode.OK, detailMessage);
    }

    protected KafkaResponse buildFailureResponse(KafkaRequest request, String detailMessage) {
        return buildResponse(request, request.getErrorTopic(), ResponseCode.INTERNAL_SERVER_ERROR, detailMessage);
    }

    private static KafkaResponse buildResponse(KafkaRequest request, String topic, ResponseCode responseCode, String detailMessage) {
        return new KafkaResponse.Builder().id(request.getId()).responseTopic(topic)
                .responseCode(responseCode).detailMessage(detailMessage).build();
    }

}
