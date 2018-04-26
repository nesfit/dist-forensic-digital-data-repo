package cz.vutbr.fit.communication.service.handler;

import cz.vutbr.fit.communication.command.Command;
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
            handleError(command, key);
        } else {
            handler.handleRequest(key, value);
        }
    }

    public void attachHandler(Command command, ICommandHandler<K, V> handler) {
        handlers.put(command, handler);
    }

    public void detachHandler(Command command, ICommandHandler<K, V> handler) {
        handlers.remove(command, handler);
    }

    private void handleError(Command command, K request) {
        String errorMsg = String.format("Command %s is not supported, request will not be handled %s", command, request);
        LOGGER.error(errorMsg);
    }

}
