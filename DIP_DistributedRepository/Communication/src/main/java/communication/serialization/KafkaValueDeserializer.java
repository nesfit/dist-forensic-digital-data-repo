package communication.serialization;

import communication.KafkaValue;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.util.Map;

public class KafkaValueDeserializer implements Deserializer<KafkaValue> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public KafkaValue deserialize(String topic, byte[] data) {
        KafkaValue kafkaValue = null;
        try {
            kafkaValue = BaseDeserializer.Deserialize(data, KafkaValue.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return kafkaValue;
    }

    @Override
    public void close() {
    }

}
