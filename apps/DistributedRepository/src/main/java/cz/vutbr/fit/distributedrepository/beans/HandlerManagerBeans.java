package cz.vutbr.fit.distributedrepository.beans;

import cz.vutbr.fit.communication.KafkaRequest;
import cz.vutbr.fit.communication.command.Command;
import cz.vutbr.fit.communication.service.handler.HandlerManager;
import cz.vutbr.fit.distributedrepository.communication.consumer.handler.LoadPcapHandler;
import cz.vutbr.fit.distributedrepository.communication.consumer.handler.StorePcapHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandlerManagerBeans {

    @Autowired
    private StorePcapHandler storePcapHandler;

    @Autowired
    private LoadPcapHandler loadPcapHandler;

    @Bean
    public HandlerManager<KafkaRequest, byte[]> handlerManager() {
        HandlerManager<KafkaRequest, byte[]> handlerManager = new HandlerManager<>();
        handlerManager.attachHandler(Command.STORE_PCAP, storePcapHandler);
        handlerManager.attachHandler(Command.LOAD_PCAP, loadPcapHandler);
        return handlerManager;
    }

}
