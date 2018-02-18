package cz.vutbr.fit.communication.consumer;

import communication.KafkaResponse;
import communication.command.Command;
import communication.command.builder.ICommandBuilder;
import communication.consumer.handler.HandlerManager;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AcknowledgementConsumer {

    @Autowired
    private HandlerManager<KafkaResponse, byte[]> handlerManager;

    @KafkaListener(topics = "${output.topic}")
    public void listen(ConsumerRecord<KafkaResponse, byte[]> record) {
        consume(record);
    }

    private void consume(ConsumerRecord<KafkaResponse, byte[]> record) {
        // TODO: Remove hardcoded Command
        Command command = Command.HANDLE_RESPONSE;
        handlerManager.handle(command, record.key(), record.value());
    }

    public void setHandlerManager(HandlerManager<KafkaResponse, byte[]> handlerManager) {
        this.handlerManager = handlerManager;
    }

}
