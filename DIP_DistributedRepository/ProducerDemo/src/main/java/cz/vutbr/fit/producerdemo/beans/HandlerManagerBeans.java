package cz.vutbr.fit.producerdemo.beans;

import cz.vutbr.fit.communication.KafkaResponse;
import cz.vutbr.fit.communication.command.Command;
import cz.vutbr.fit.communication.consumer.handler.HandlerManager;
import cz.vutbr.fit.producerdemo.communication.consumer.handler.AcknowledgementConsumerHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandlerManagerBeans {

    @Autowired
    private AcknowledgementConsumerHandler ackConsumerHandler;

    @Bean
    public HandlerManager<KafkaResponse, byte[]> handlerManager() {
        HandlerManager handlerManager = new HandlerManager<>();
        handlerManager.attachHandler(Command.HANDLE_RESPONSE, ackConsumerHandler);
        return handlerManager;
    }

    @Bean
    public Command responseCommand() {
        return Command.HANDLE_RESPONSE;
    }

}
