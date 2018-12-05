package br.com.teclibrary.system.impls;

public class ModelPair<K, V> {

    private final K key;
    private final V value;

    public ModelPair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}