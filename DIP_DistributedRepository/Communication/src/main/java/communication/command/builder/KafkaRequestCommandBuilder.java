package communication.command.builder;

import communication.KafkaRequest;
import communication.command.Command;

public class KafkaRequestCommandBuilder implements ICommandBuilder<KafkaRequest> {

    @Override
    public Command buildCommand(KafkaRequest kafkaRequest) {
        return Command.getCommandByOperationAndDataType(kafkaRequest.getOperation(), kafkaRequest.getDataType());
    }

}
