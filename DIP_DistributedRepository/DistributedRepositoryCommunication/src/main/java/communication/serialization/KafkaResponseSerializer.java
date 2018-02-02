package communication.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import communication.KafkaResponse;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class KafkaResponseSerializer implements Serializer<KafkaResponse> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, KafkaResponse kafkaResponse) {
        byte[] data = null;
        try {
            data = BaseSerializer.Serialize(kafkaResponse);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    public void close() {
    }

}
