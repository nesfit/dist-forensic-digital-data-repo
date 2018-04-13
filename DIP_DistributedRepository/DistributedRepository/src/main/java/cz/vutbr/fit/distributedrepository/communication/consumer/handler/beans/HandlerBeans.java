package cz.vutbr.fit.distributedrepository.communication.consumer.handler.beans;

import cz.vutbr.fit.distributedrepository.communication.consumer.handler.LoadPcapHandler;
import cz.vutbr.fit.distributedrepository.communication.consumer.handler.StorePcapHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandlerBeans {

    @Bean
    public StorePcapHandler storePcapHandler() {
        return new StorePcapHandler();
    }

    @Bean
    public LoadPcapHandler loadPcapHandler() {
        return new LoadPcapHandler();
    }

}
