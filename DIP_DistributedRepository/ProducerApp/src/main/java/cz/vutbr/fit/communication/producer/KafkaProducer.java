package cz.vutbr.fit.communication.producer;

import cz.vutbr.fit.communication.KafkaRequest;
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
public class KafkaProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);

    @Autowired
    private KafkaTemplate<KafkaRequest, byte[]> kafkaTemplate;

    public void produce(String topic, KafkaRequest request, byte[] value) {
        ListenableFuture<SendResult<KafkaRequest, byte[]>> future = kafkaTemplate.send(new ProducerRecord<>(topic, request, value));
        future.addCallback(KafkaProducer::onSuccess, KafkaProducer::onFailure);
    }

    public void produce(String topic, KafkaRequest request, byte[] value,
                        SuccessCallback<SendResult<KafkaRequest, byte[]>> successCallback, FailureCallback failureCallback) {
        ListenableFuture<SendResult<KafkaRequest, byte[]>> future = kafkaTemplate.send(new ProducerRecord<>(topic, request, value));
        future.addCallback(successCallback, failureCallback);
    }

    public void produce(String topic, KafkaRequest request, byte[] value,
                        ListenableFutureCallback<SendResult<KafkaRequest, byte[]>> listenableFutureCallback) {
        ListenableFuture<SendResult<KafkaRequest, byte[]>> future = kafkaTemplate.send(new ProducerRecord<>(topic, request, value));
        future.addCallback(listenableFutureCallback);
    }

    private static void onSuccess(SendResult<KafkaRequest, byte[]> result) {

    }

    private static void onFailure(Throwable throwable) {
        LOGGER.error(throwable.getMessage(), throwable);
    }

}
