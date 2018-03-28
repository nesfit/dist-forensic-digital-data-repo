package cz.vutbr.fit.producerdemo.communication.consumer.handler;

import cz.vutbr.fit.communication.KafkaResponse;
import cz.vutbr.fit.communication.consumer.handler.ICommandHandler;
import cz.vutbr.fit.producerdemo.stats.CollectStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class AcknowledgementConsumerHandler implements ICommandHandler<KafkaResponse, byte[]> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AcknowledgementConsumerHandler.class);

    @Override
    public void handleRequest(KafkaResponse response, byte[] bytes) {
        CollectStats.getInstance().setEndTime(response.getId(), new Date());
        LOGGER.debug(response.toString());
        CollectStats.getInstance().finalStats();
    }

}
