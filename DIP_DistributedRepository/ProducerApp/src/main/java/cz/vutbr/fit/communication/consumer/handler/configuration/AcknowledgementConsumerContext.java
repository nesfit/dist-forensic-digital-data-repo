package cz.vutbr.fit.communication.consumer.handler.configuration;

import cz.vutbr.fit.communication.KafkaResponse;
import cz.vutbr.fit.communication.command.Command;
import cz.vutbr.fit.communication.consumer.handler.AcknowledgementConsumerHandler;
import cz.vutbr.fit.communication.consumer.handler.HandlerManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AcknowledgementConsumerContext {

    @Bean
    public AcknowledgementConsumerHandler ackConsumerHandler() {
        return new AcknowledgementConsumerHandler();
    }

    @Bean
    public HandlerManager<KafkaResponse, byte[]> handlerManager() {
        HandlerManager handlerManager = new HandlerManager<>();
        handlerManager.attachHandler(Command.HANDLE_RESPONSE, new AcknowledgementConsumerHandler());
        return handlerManager;
    }

}
