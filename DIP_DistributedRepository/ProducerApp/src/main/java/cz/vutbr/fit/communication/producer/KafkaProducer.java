package cz.vutbr.fit.communication.producer;

import cz.vutbr.fit.communication.KafkaRequest;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

@Service
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<KafkaRequest, byte[]> kafkaTemplate;

    public void produce(ProducerRecord<KafkaRequest, byte[]> record) {
        ListenableFuture<SendResult<KafkaRequest, byte[]>> future = kafkaTemplate.send(record);
        future.addCallback(KafkaProducer::onSuccess, KafkaProducer::onFailure);
    }

    private static void onSuccess(SendResult<KafkaRequest, byte[]> result) {

    }

    private static void onFailure(Throwable throwable) {
        throwable.printStackTrace();
    }

}
