package cz.vutbr.fit.distributedrepository.beans;

import cz.vutbr.fit.communication.KafkaRequest;
import cz.vutbr.fit.communication.command.Command;
import cz.vutbr.fit.communication.consumer.handler.HandlerManager;
import cz.vutbr.fit.distributedrepository.communication.consumer.handler.LoadPcapHandler;
import cz.vutbr.fit.distributedrepository.communication.consumer.handler.StorePcapHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandlerManagerBeans {

    @Autowired
    private LoadPcapHandler loadPcapHandler;

    @Autowired
    private StorePcapHandler storePcapHandler;

    @Bean
    public HandlerManager<KafkaRequest, byte[]> handlerManager() {
        HandlerManager<KafkaRequest, byte[]> handlerManager = new HandlerManager<>();
        handlerManager.attachHandler(Command.LOAD_PCAP, loadPcapHandler);
        handlerManager.attachHandler(Command.STORE_PCAP, storePcapHandler);
        return handlerManager;
    }

}
