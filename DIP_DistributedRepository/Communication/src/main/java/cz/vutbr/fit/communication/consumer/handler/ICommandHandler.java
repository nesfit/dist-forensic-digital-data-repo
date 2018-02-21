package cz.vutbr.fit.communication.consumer.handler;

public interface ICommandHandler<K, V> {

    public void handleRequest(K key, V value);

}
