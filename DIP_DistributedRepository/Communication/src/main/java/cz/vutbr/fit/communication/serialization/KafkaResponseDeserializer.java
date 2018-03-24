package cz.vutbr.fit.communication.serialization;

import cz.vutbr.fit.communication.KafkaResponse;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class KafkaResponseDeserializer implements Deserializer<KafkaResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaResponseDeserializer.class);

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public KafkaResponse deserialize(String topic, byte[] data) {
        KafkaResponse kafkaResponse = null;
        try {
            kafkaResponse = BaseDeserializer.Deserialize(data, KafkaResponse.class);
        } catch (IOException exception) {
            LOGGER.error(exception.getMessage(), exception);
        }
        return kafkaResponse;
    }

    @Override
    public void close() {
    }

}
