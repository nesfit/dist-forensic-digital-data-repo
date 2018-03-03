package cz.vutbr.fit.communication.command.builder;

import cz.vutbr.fit.communication.KafkaRequest;
import cz.vutbr.fit.communication.command.Command;

@Deprecated
public class KafkaRequestCommandBuilder implements ICommandBuilder<KafkaRequest> {

    @Override
    public Command buildCommand(KafkaRequest kafkaRequest) {
        return Command.getCommandByOperationAndDataType(kafkaRequest.getOperation(), kafkaRequest.getDataType());
    }

}
