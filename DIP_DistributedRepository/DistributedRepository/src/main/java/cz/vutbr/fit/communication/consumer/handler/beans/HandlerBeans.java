package cz.vutbr.fit.communication.consumer.handler.beans;

import cz.vutbr.fit.communication.consumer.handler.StorePcapHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandlerBeans {

    @Bean
    public StorePcapHandler storePcapHandler() {
        return new StorePcapHandler();
    }

}
