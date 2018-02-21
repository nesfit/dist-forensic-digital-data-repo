package cz.vutbr.fit;

import cz.vutbr.fit.communication.KafkaRequest;
import cz.vutbr.fit.communication.command.Command;
import cz.vutbr.fit.communication.consumer.handler.HandlerManager;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class DistributedRepositoryConsumer {

    @Autowired
    private HandlerManager<KafkaRequest, byte[]> handlerManager;

    @KafkaListener(topics = "${input.topic}")
    public void listen(ConsumerRecord<KafkaRequest, byte[]> record) {
        consume(record);
    }

    private void consume(ConsumerRecord<KafkaRequest, byte[]> record) {
        System.out.println("Record -> " + record.key());
        Command command = record.key().getCommand();
        handlerManager.handle(command, record.key(), record.value());
    }

}
