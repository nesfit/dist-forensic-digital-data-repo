package cz.vutbr.fit.communication.consumer.handler.beans;

import cz.vutbr.fit.communication.KafkaRequest;
import cz.vutbr.fit.communication.command.Command;
import cz.vutbr.fit.communication.consumer.handler.HandlerManager;
import cz.vutbr.fit.communication.consumer.handler.StorePcapHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandlerManagerBeans {

    @Autowired
    private StorePcapHandler storePcapHandler;

    @Bean
    public HandlerManager<KafkaRequest, byte[]> handlerManager() {
        HandlerManager<KafkaRequest, byte[]> handlerManager = new HandlerManager<>();
        handlerManager.attachHandler(Command.STORE_PCAP, storePcapHandler);
        return handlerManager;
    }

    // TODO: Should be in different class
    @Bean
    public StorePcapHandler storePcapHandler() {
        return new StorePcapHandler();
    }

}
