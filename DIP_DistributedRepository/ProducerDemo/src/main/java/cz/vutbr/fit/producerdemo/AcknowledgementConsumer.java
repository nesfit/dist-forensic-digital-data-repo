package cz.vutbr.fit.producerdemo;

import cz.vutbr.fit.communication.KafkaResponse;
import cz.vutbr.fit.communication.command.Command;
import cz.vutbr.fit.communication.consumer.handler.HandlerManager;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AcknowledgementConsumer {

    @Autowired
    private HandlerManager<KafkaResponse, byte[]> handlerManager;

    @Autowired
    private Command responseCommand;

    @KafkaListener(topics = {"${output.topic}", "${error.topic}"})
    public void listen(ConsumerRecord<KafkaResponse, byte[]> record) {
        consume(record);
    }

    private void consume(ConsumerRecord<KafkaResponse, byte[]> record) {
        handlerManager.handle(responseCommand, record.key(), record.value());
    }

}
