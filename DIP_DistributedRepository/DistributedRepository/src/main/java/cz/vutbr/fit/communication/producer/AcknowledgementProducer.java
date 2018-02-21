package cz.vutbr.fit.communication.producer;

import cz.vutbr.fit.communication.KafkaResponse;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

@Service
public class AcknowledgementProducer {

    @Autowired
    private KafkaTemplate<KafkaResponse, byte[]> kafkaTemplate;

    public void produce(String topic, KafkaResponse response, byte[] value) {
        ListenableFuture<SendResult<KafkaResponse, byte[]>> future = kafkaTemplate.send(new ProducerRecord<>(topic, response, value));
        future.addCallback(AcknowledgementProducer::onSuccess, AcknowledgementProducer::onFailure);
    }

    private static void onSuccess(SendResult<KafkaResponse, byte[]> result) {

    }

    private static void onFailure(Throwable throwable) {
        throwable.printStackTrace();
    }

}
