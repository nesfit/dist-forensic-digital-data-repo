package cz.vutbr.fit.distributedrepository;

import cz.vutbr.fit.communication.KafkaRequest;
import cz.vutbr.fit.communication.command.Command;
import cz.vutbr.fit.communication.service.handler.HandlerManager;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class DistributedRepositoryConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DistributedRepositoryConsumer.class);

    @Autowired
    private HandlerManager<KafkaRequest, byte[]> handlerManager;

    @KafkaListener(topics = "${input.topic}")
    public void listen(ConsumerRecord<KafkaRequest, byte[]> record) {
        consume(record);
    }

    private void consume(ConsumerRecord<KafkaRequest, byte[]> record) {
        LOGGER.debug("Record -> " + record.key());
        Command command = record.key().getCommand();
        handlerManager.handle(command, record.key(), record.value());
    }

}
