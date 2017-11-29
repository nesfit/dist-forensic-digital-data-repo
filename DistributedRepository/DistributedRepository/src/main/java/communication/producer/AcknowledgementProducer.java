package communication.producer;

import communication.request.KafkaRequest;
import communication.request.serialization.KafkaRequestSerializer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class AcknowledgementProducer {

    private KafkaRequest request;
    private String value;

    public AcknowledgementProducer(KafkaRequest request, String value) {
        this.request = request;
        this.value = value;
    }

    public void acknowledge() {
        KafkaRequest response = new KafkaRequest.Builder().build();
        Producer<KafkaRequest, String> producer = new Producer<>(KafkaRequestSerializer.class, StringSerializer.class);
        producer.produce(new ProducerRecord<>(request.getResponseTopic(), response, value));
    }

}
