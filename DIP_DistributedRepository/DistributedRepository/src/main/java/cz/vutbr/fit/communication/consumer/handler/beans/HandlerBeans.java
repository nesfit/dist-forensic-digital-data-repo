package cz.vutbr.fit.communication.consumer.handler.beans;

import cz.vutbr.fit.communication.consumer.handler.LoadPcapHandler;
import cz.vutbr.fit.communication.consumer.handler.StorePcapHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandlerBeans {

    @Bean
    public LoadPcapHandler loadPcapHandler() {
        return new LoadPcapHandler();
    }

    @Bean
    public StorePcapHandler storePcapHandler() {
        return new StorePcapHandler();
    }

}
