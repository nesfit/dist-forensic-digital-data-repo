import cz.vutbr.fit.communication.command.Command;
import cz.vutbr.fit.communication.command.DataType;
import cz.vutbr.fit.communication.command.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandTest.class);

    public static void main(String[] args) {
        Command c = Command.getCommandByOperationAndDataType(Operation.STORE, DataType.PCAP);
        LOGGER.debug(c.toString());
        c = Command.getCommandByOperationAndDataType(Operation.STORE, DataType.UNSTRUCTURED);
        LOGGER.debug(c.toString());
    }

}
