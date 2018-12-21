package cz.vutbr.fit.producerdemo.service.handler;

import cz.vutbr.fit.communication.KafkaResponse;
import cz.vutbr.fit.communication.service.handler.ICommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AcknowledgementConsumerHandler implements ICommandHandler<KafkaResponse, byte[]> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AcknowledgementConsumerHandler.class);

    @Override
    public void handleRequest(KafkaResponse response, byte[] bytes) {
        LOGGER.debug(response.toString());
        // Uncomment these two lines to enable time-duration statistics
        //CollectStats.getInstance().setEndTime(response.getId(), new Date());
        //CollectStats.getInstance().finalStats();
    }

}
