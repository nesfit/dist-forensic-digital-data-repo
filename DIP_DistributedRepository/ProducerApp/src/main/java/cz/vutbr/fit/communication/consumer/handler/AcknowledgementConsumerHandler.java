package cz.vutbr.fit.communication.consumer.handler;

import communication.KafkaResponse;
import communication.consumer.handler.ICommandHandler;

import java.util.Date;

public class AcknowledgementConsumerHandler implements ICommandHandler<KafkaResponse, byte[]> {

    @Override
    public void handleRequest(KafkaResponse response, byte[] s) {
        System.out.println("Receive time" + new Date());
        System.out.println(response);
        System.out.println(s);
    }

}