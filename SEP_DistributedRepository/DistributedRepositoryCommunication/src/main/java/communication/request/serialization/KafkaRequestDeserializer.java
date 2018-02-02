package communication.request.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import communication.request.KafkaRequest;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class KafkaRequestDeserializer implements Deserializer<KafkaRequest> {

    @Override
    public void configure(Map map, boolean b) {
    }

    @Override
    public KafkaRequest deserialize(String s, byte[] bytes) {
        ObjectMapper mapper = new ObjectMapper();
        KafkaRequest kafkaRequest = null;
        try {
            kafkaRequest = mapper.readValue(bytes, KafkaRequest.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return kafkaRequest;
    }

    @Override
    public void close() {
    }

}
