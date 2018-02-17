package communication.command.builder;

import communication.command.Command;

public interface ICommandBuilder<K> {

    public Command buildCommand(K key);

}
