package cz.vutbr.fit.producerdemo.communication.consumer.handler;

import cz.vutbr.fit.communication.KafkaResponse;
import cz.vutbr.fit.communication.consumer.handler.ICommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ErrorConsumerHandler implements ICommandHandler<KafkaResponse, byte[]> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorConsumerHandler.class);

    @Override
    public void handleRequest(KafkaResponse response, byte[] bytes) {
        LOGGER.error(response.toString());
    }

}
