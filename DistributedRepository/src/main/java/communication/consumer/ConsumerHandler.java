package communication.consumer;

public interface ConsumerHandler<K, V> {

    public void handleRequest(K key, V value);

}
