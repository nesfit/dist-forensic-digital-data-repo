package cz.vutbr.fit.communication.producer;

import cz.vutbr.fit.communication.KafkaResponse;
import cz.vutbr.fit.communication.serialization.KafkaResponseSerializer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;

public class AcknowledgementProducer {

    private Producer<KafkaResponse, byte[]> producer;
    private KafkaResponse response;
    private byte[] value;

    public AcknowledgementProducer(KafkaResponse response, byte[] value) {
        this.response = response;
        this.value = value;
        producer = new Producer.Builder<KafkaResponse, byte[]>(KafkaResponseSerializer.class, ByteArraySerializer.class).debug(false).build();
    }

    public void acknowledge() {
        producer.produce(new ProducerRecord<>(response.getResponseTopic(), response, value));
    }

}
