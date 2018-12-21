package cz.vutbr.fit.communication.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import cz.vutbr.fit.communication.KafkaResponse;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class KafkaResponseSerializer implements Serializer<KafkaResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaResponseSerializer.class);

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, KafkaResponse kafkaResponse) {
        byte[] data = null;
        try {
            data = BaseSerializer.Serialize(kafkaResponse);
        } catch (JsonProcessingException exception) {
            LOGGER.error(exception.getMessage(), exception);
        }
        return data;
    }

    @Override
    public void close() {
    }

}
