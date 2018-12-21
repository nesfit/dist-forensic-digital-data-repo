package cz.vutbr.fit.communication.serialization;

import cz.vutbr.fit.communication.KafkaRequest;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class KafkaRequestDeserializer implements Deserializer<KafkaRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaRequestDeserializer.class);

    @Override
    public void configure(Map<String, ?> map, boolean b) {
    }

    @Override
    public KafkaRequest deserialize(String topic, byte[] data) {
        KafkaRequest kafkaRequest = null;
        try {
            kafkaRequest = BaseDeserializer.Deserialize(data, KafkaRequest.class);
        } catch (IOException exception) {
            LOGGER.error(exception.getMessage(), exception);
        }
        return kafkaRequest;
    }

    @Override
    public void close() {
    }

}
