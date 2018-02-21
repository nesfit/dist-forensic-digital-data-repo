package cz.vutbr.fit.communication.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import cz.vutbr.fit.communication.KafkaRequest;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class KafkaRequestSerializer implements Serializer<KafkaRequest> {

    @Override
    public void configure(Map<String, ?> map, boolean b) {
    }

    @Override
    public byte[] serialize(String topic, KafkaRequest kafkaRequest) {
        byte[] data = null;
        try {
            data = BaseSerializer.Serialize(kafkaRequest);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    public void close() {
    }

}
