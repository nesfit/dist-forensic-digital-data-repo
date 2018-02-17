package communication.command.builder;

import communication.KafkaResponse;
import communication.command.Command;

public class KafkaResponseCommandBuilder implements ICommandBuilder<KafkaResponse> {

    @Override
    public Command buildCommand(KafkaResponse kafkaRequest) {
        return Command.HANDLE_RESPONSE;
    }

}
