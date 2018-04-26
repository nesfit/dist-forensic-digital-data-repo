package cz.vutbr.fit.producerdemo.service.handler.beans;

import cz.vutbr.fit.producerdemo.service.handler.AcknowledgementConsumerHandler;
import cz.vutbr.fit.producerdemo.service.handler.ErrorConsumerHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandlerBeans {

    @Bean
    public AcknowledgementConsumerHandler ackConsumerHandler() {
        return new AcknowledgementConsumerHandler();
    }

    @Bean
    public ErrorConsumerHandler errorConsumerHandler() {
        return new ErrorConsumerHandler();
    }

}
