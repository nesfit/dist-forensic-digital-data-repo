package cz.vutbr.fit.communication.consumer.handler;

import cz.vutbr.fit.communication.KafkaResponse;
import cz.vutbr.fit.stats.CollectStats;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class AcknowledgementConsumerHandler implements ICommandHandler<KafkaResponse, byte[]> {

    @Override
    public void handleRequest(KafkaResponse response, byte[] s) {
        CollectStats.getInstance().setEndTime(response.getId(), new Date());
        System.out.println(response);
        CollectStats.getInstance().finalStats();
    }

}
