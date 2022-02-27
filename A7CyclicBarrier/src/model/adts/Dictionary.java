package model.adts;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Dictionary<K, V> implements IDictionary<K, V> {
    private final Map<K, V> hashMap;

    public Dictionary() {
        hashMap = new ConcurrentHashMap<>();
    }

    @Override
    public boolean isDefined(K key) {
        return hashMap.containsKey(key);
    }

    @Override
    public V lookUp(K key) {
        return hashMap.get(key);
    }

    @Override
    public void update(K key, V value) {
        hashMap.put(key, value);
    }

    @Override
    public Map<K, V> getContent() {
        return hashMap;
    }

    /**
     * THIS DOESN'T WORK
     * @return a new dictionary with the same objects -> shallow copy
     */
    @Override
    public IDictionary<K, V> deepcopy() {

        IDictionary<K, V> newDictionary = new Dictionary<K, V>();
        for (Map.Entry<K, V> entry: hashMap.entrySet()) {
            K keyShallowCopy = entry.getKey();
            V valueShallowCopy = entry.getValue();
            newDictionary.update(keyShallowCopy, valueShallowCopy);
        }

        return newDictionary;
    }

    @Override
    public String toString() {
        return hashMap.entrySet()
                .stream()
                .map(kvEntry -> kvEntry.getKey() + " --> " + kvEntry.getValue())
                .reduce("", (acc, e) -> acc + e + "\n");
    }
}
