package cz.vutbr.fit.communication.consumer.handler;

import communication.KafkaResponse;
import communication.consumer.handler.ICommandHandler;
import cz.vutbr.fit.stats.CollectStats;

import java.util.Date;

public class AcknowledgementConsumerHandler implements ICommandHandler<KafkaResponse, byte[]> {

    @Override
    public void handleRequest(KafkaResponse response, byte[] s) {
        CollectStats.getInstance().setEndTime(response.getId(), new Date());
        System.out.println(response);
        CollectStats.getInstance().finalStats();
    }

}
