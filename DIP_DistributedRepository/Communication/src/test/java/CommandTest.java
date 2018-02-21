import cz.vutbr.fit.communication.command.Command;
import cz.vutbr.fit.communication.command.DataType;
import cz.vutbr.fit.communication.command.Operation;

public class CommandTest {

    public static void main(String[] args) {
        Command c = Command.getCommandByOperationAndDataType(Operation.STORE, DataType.PCAP);
        System.out.println(c);
        c = Command.getCommandByOperationAndDataType(Operation.STORE, DataType.UNSTRUCTURED);
        System.out.println(c);
    }

}
