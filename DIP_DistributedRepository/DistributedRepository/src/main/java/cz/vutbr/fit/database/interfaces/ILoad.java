package cz.vutbr.fit.database.interfaces;

public interface ILoad<K, V> {

    public V load(K key);

}
