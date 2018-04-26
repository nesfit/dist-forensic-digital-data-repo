package cz.vutbr.fit.producerdemo.beans;

import cz.vutbr.fit.communication.KafkaResponse;
import cz.vutbr.fit.communication.command.Command;
import cz.vutbr.fit.communication.service.handler.HandlerManager;
import cz.vutbr.fit.producerdemo.service.handler.AcknowledgementConsumerHandler;
import cz.vutbr.fit.producerdemo.service.handler.ErrorConsumerHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandlerManagerBeans {

    @Autowired
    private AcknowledgementConsumerHandler ackConsumerHandler;

    @Autowired
    private ErrorConsumerHandler errorConsumerHandler;

    @Bean
    public HandlerManager<KafkaResponse, byte[]> handlerManager() {
        HandlerManager handlerManager = new HandlerManager<>();
        handlerManager.attachHandler(Command.HANDLE_SUCCESS_RESPONSE, ackConsumerHandler);
        handlerManager.attachHandler(Command.HANDLE_FAILURE_RESPONSE, errorConsumerHandler);
        return handlerManager;
    }

    @Bean
    public Command responseSuccessCommand() {
        return Command.HANDLE_SUCCESS_RESPONSE;
    }

    @Bean
    public Command responseFailureCommand() {
        return Command.HANDLE_FAILURE_RESPONSE;
    }

}
