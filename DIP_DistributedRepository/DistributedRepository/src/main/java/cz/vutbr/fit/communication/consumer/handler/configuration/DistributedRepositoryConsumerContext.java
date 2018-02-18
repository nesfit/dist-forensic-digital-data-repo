package cz.vutbr.fit.communication.consumer.handler.configuration;

import communication.KafkaRequest;
import communication.command.Command;
import communication.consumer.handler.HandlerManager;
import cz.vutbr.fit.communication.consumer.handler.StorePcapHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DistributedRepositoryConsumerContext {

    @Bean
    public HandlerManager<KafkaRequest, byte[]> handlerManager() {
        HandlerManager handlerManager = new HandlerManager<>();
        handlerManager.attachHandler(Command.STORE_PCAP, new StorePcapHandler());
        return handlerManager;
    }

}
