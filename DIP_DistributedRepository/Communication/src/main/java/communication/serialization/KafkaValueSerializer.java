package communication.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import communication.KafkaValue;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class KafkaValueSerializer implements Serializer<KafkaValue> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, KafkaValue kafkaValue) {
        byte[] data = null;
        try {
            data = BaseSerializer.Serialize(kafkaValue);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    public void close() {
    }

}
