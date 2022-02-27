package model.adts;

import java.util.Map;

public interface IDictionary<K, V> {
    boolean isDefined(K key);

    V lookUp(K key);

    void update(K key, V value);

    Map<K, V> getContent();

    IDictionary<K, V> deepcopy();
}
