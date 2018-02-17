package cz.vutbr.fit.communication.consumer.handler.configuration;

import communication.KafkaResponse;
import communication.command.Command;
import communication.command.builder.KafkaResponseCommandBuilder;
import communication.consumer.handler.HandlerManager;
import cz.vutbr.fit.communication.consumer.handler.AcknowledgementConsumerHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AcknowledgementConsumerContext {

    @Bean
    public KafkaResponseCommandBuilder commandBuilder() {
        return new KafkaResponseCommandBuilder();
    }

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
