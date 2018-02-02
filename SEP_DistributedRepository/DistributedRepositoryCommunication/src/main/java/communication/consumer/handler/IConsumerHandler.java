package communication.consumer.handler;

public interface IConsumerHandler<K, V> {

    public void handleRequest(K key, V value);

}
