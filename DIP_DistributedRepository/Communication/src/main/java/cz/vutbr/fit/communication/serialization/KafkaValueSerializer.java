package cz.vutbr.fit.communication.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import cz.vutbr.fit.communication.KafkaValue;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class KafkaValueSerializer implements Serializer<KafkaValue> {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaValueSerializer.class);

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, KafkaValue kafkaValue) {
        byte[] data = null;
        try {
            data = BaseSerializer.Serialize(kafkaValue);
        } catch (JsonProcessingException exception) {
            LOGGER.error(exception.getMessage(), exception);
        }
        return data;
    }

    @Override
    public void close() {
    }

}
