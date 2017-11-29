package communication.consumer.handler;

import communication.request.KafkaRequest;

public class AcknowledgementConsumerHandler implements IConsumerHandler<KafkaRequest, String> {

    @Override
    public void handleRequest(KafkaRequest request, String s) {
        System.out.println("Packets saved and confirmed: " + s);
    }

}
