package model.adts;

import java.util.Map;

public interface IHeap<K, V> {
    boolean containsKey(K key);

    V get(K key);

    V put(K key, V value);

    V remove(K key);

    Map<K, V> getContent();

    void setContent(Map<K, V> newContent);

}
