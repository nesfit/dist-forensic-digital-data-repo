package cz.vutbr.fit.communication.command.builder;

import cz.vutbr.fit.communication.command.Command;

public interface ICommandBuilder<K> {

    public Command buildCommand(K key);

}
