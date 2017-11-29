package communication.consumer.handler;

import communication.command.DataType;
import communication.command.Operation;
import communication.request.KafkaRequest;

public class ConsumerHandlerFactory implements IConsumerHandlerFactory<KafkaRequest> {

    public IConsumerHandler getConsumerHandler(KafkaRequest request) {
        if (request.getOperation() == Operation.STORE && request.getDataType() == DataType.PCAP) {
            return new StorePacketConsumerHandler();
        } else {
            throw new IllegalArgumentException("Unknown request");
        }
    }

}
