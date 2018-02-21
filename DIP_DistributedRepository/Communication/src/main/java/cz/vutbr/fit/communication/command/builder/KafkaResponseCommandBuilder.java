package cz.vutbr.fit.communication.command.builder;

import cz.vutbr.fit.communication.KafkaResponse;
import cz.vutbr.fit.communication.command.Command;

public class KafkaResponseCommandBuilder implements ICommandBuilder<KafkaResponse> {

    @Override
    public Command buildCommand(KafkaResponse kafkaRequest) {
        return Command.HANDLE_RESPONSE;
    }

}
