package communication.consumer.handler;

import communication.request.KafkaRequest;

public class ConsumerHandlerFactory implements IConsumerHandlerFactory<KafkaRequest> {

    public IConsumerHandler getConsumerHandler(KafkaRequest request) {
        return new AcknowledgementConsumerHandler();
    }

}
