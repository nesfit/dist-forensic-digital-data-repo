package communication.consumer.handler;

import communication.command.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class HandlerManager<K, V> {

    private static final Logger LOGGER = LoggerFactory.getLogger(HandlerManager.class);

    private Map<Command, ICommandHandler<K, V>> handlers = new HashMap<>();

    public void handle(Command command, K key, V value) {
        ICommandHandler<K, V> handler = handlers.get(command);
        if (handler == null) {
            String errorMsg = "Command " + command + " not supported!";
            LOGGER.error(errorMsg);
            throw new RuntimeException(errorMsg);
        }
        handler.handleRequest(key, value);
    }

    public void attachHandler(Command command, ICommandHandler<K, V> handler) {
        handlers.put(command, handler);
    }

    public void detachHandler(Command command, ICommandHandler<K, V> handler) {
        handlers.remove(command, handler);
    }

}
