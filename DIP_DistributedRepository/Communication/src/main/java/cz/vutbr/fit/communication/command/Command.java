package cz.vutbr.fit.communication.command;

import java.util.Arrays;

public enum Command {

    STORE_PCAP(Operation.STORE, DataType.PCAP),
    STORE_PACKET(Operation.STORE, DataType.PACKET),
    LOAD_PCAP(Operation.LOAD, DataType.PCAP),
    LOAD_PACKET(Operation.LOAD, DataType.PACKET),

    HANDLE_RESPONSE;

    private Operation operation;
    private DataType dataType;

    Command(Operation operation, DataType dataType) {
        this.operation = operation;
        this.dataType = dataType;
    }

    Command() {

    }

    public Operation getOperation() {
        return operation;
    }

    public DataType getDataType() {
        return dataType;
    }

    public static Command getCommandByOperationAndDataType(Operation operation, DataType dataType) {
        return Arrays.stream(Command.values())
                .filter(command -> (command.getOperation() == operation && command.getDataType() == dataType))
                .findFirst().orElse(null);
    }

}