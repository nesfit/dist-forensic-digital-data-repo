package communication.serialization;

import communication.KafkaRequest;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.util.Map;

public class KafkaRequestDeserializer implements Deserializer<KafkaRequest> {

    @Override
    public void configure(Map<String, ?> map, boolean b) {
    }

    @Override
    public KafkaRequest deserialize(String topic, byte[] data) {
        KafkaRequest kafkaRequest = null;
        try {
            kafkaRequest = BaseDeserializer.Deserialize(data, KafkaRequest.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return kafkaRequest;
    }

    @Override
    public void close() {
    }

}
