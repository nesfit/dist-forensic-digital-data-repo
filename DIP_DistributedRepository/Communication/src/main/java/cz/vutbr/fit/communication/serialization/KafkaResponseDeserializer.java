package cz.vutbr.fit.communication.serialization;

import cz.vutbr.fit.communication.KafkaResponse;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.util.Map;

public class KafkaResponseDeserializer implements Deserializer<KafkaResponse> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public KafkaResponse deserialize(String topic, byte[] data) {
        KafkaResponse kafkaResponse = null;
        try {
            kafkaResponse = BaseDeserializer.Deserialize(data, KafkaResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return kafkaResponse;
    }

    @Override
    public void close() {
    }

}
