package cz.vutbr.fit.communication.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import cz.vutbr.fit.communication.KafkaRequest;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class KafkaRequestSerializer implements Serializer<KafkaRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaRequestSerializer.class);

    @Override
    public void configure(Map<String, ?> map, boolean b) {
    }

    @Override
    public byte[] serialize(String topic, KafkaRequest kafkaRequest) {
        byte[] data = null;
        try {
            data = BaseSerializer.Serialize(kafkaRequest);
        } catch (JsonProcessingException exception) {
            LOGGER.error(exception.getMessage(), exception);
        }
        return data;
    }

    @Override
    public void close() {
    }

}
