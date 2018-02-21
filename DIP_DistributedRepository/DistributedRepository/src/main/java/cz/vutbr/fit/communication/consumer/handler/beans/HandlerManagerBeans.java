package cz.vutbr.fit.communication.consumer.handler.beans;

import cz.vutbr.fit.cassandra.repository.PacketRepository;
import cz.vutbr.fit.communication.KafkaRequest;
import cz.vutbr.fit.communication.command.Command;
import cz.vutbr.fit.communication.consumer.handler.HandlerManager;
import cz.vutbr.fit.communication.consumer.handler.StorePcapHandler;
import cz.vutbr.fit.mongodb.repository.PacketMetadataRepository;
import cz.vutbr.fit.service.pcap.IPcapParser;
import cz.vutbr.fit.service.pcap.org.pcap4j.PcapParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandlerManagerBeans {

    @Autowired
    private PacketRepository packetRepository;
    @Autowired
    private PacketMetadataRepository packetMetadataRepository;
    @Autowired
    private StorePcapHandler storePcapHandler;
    @Autowired
    private IPcapParser pcapParser;

    @Bean
    public HandlerManager<KafkaRequest, byte[]> handlerManager() {
        HandlerManager handlerManager = new HandlerManager<>();
        handlerManager.attachHandler(Command.STORE_PCAP, storePcapHandler);
        return handlerManager;
    }

    @Bean
    public StorePcapHandler storePcapHandler() {
        StorePcapHandler storePcapHandler = new StorePcapHandler();
        storePcapHandler.setPacketRepository(packetRepository);
        storePcapHandler.setPacketMetadataRepository(packetMetadataRepository);
        storePcapHandler.setPcapParser(pcapParser);
        return storePcapHandler;
    }

    @Bean
    public IPcapParser pcapParser() {
        return new PcapParser();
    }

}
