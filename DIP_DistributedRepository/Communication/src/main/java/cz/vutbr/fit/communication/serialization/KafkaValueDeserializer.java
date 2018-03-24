package cz.vutbr.fit.communication.serialization;

import cz.vutbr.fit.communication.KafkaValue;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class KafkaValueDeserializer implements Deserializer<KafkaValue> {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaValueDeserializer.class);

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public KafkaValue deserialize(String topic, byte[] data) {
        KafkaValue kafkaValue = null;
        try {
            kafkaValue = BaseDeserializer.Deserialize(data, KafkaValue.class);
        } catch (IOException exception) {
            LOGGER.error(exception.getMessage(), exception);
        }
        return kafkaValue;
    }

    @Override
    public void close() {
    }

}
