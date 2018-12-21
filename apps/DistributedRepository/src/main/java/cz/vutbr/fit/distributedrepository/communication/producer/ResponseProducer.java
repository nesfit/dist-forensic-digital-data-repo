package cz.vutbr.fit.distributedrepository.communication.producer;

import cz.vutbr.fit.communication.KafkaResponse;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.util.concurrent.SuccessCallback;

@Service
public class ResponseProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseProducer.class);

    @Autowired
    private KafkaTemplate<KafkaResponse, byte[]> kafkaTemplate;

    public void produce(String topic, KafkaResponse response, byte[] value) {
        ListenableFuture<SendResult<KafkaResponse, byte[]>> future = kafkaTemplate.send(new ProducerRecord<>(topic, response, value));
        future.addCallback(ResponseProducer::onSuccess, ResponseProducer::onFailure);
    }

    public void produce(String topic, KafkaResponse response, byte[] value,
                        SuccessCallback<SendResult<KafkaResponse, byte[]>> successCallback, FailureCallback failureCallback) {
        ListenableFuture<SendResult<KafkaResponse, byte[]>> future = kafkaTemplate.send(new ProducerRecord<>(topic, response, value));
        future.addCallback(successCallback, failureCallback);
    }

    public void produce(String topic, KafkaResponse response, byte[] value,
                        ListenableFutureCallback<SendResult<KafkaResponse, byte[]>> listenableFutureCallback) {
        ListenableFuture<SendResult<KafkaResponse, byte[]>> future = kafkaTemplate.send(new ProducerRecord<>(topic, response, value));
        future.addCallback(listenableFutureCallback);
    }

    private static void onSuccess(SendResult<KafkaResponse, byte[]> result) {
        LOGGER.info("Acknowledgement sent successfully.");
    }

    private static void onFailure(Throwable throwable) {
        LOGGER.error(throwable.getMessage(), throwable);
    }

}
