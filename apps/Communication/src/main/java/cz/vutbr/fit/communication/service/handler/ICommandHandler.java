package cz.vutbr.fit.communication.service.handler;

public interface ICommandHandler<K, V> {

    public void handleRequest(K key, V value);

}
