package model.adts;


import java.util.HashMap;
import java.util.Map;

public class DictionaryWithClonableValues<K, V extends model.interfaces.Cloneable<V>> implements IDictionary<K, V> {
    private final HashMap<K, V> symbolTableDictionary;

    public DictionaryWithClonableValues() {
        this.symbolTableDictionary = new HashMap<>();
    }

    @Override
    public boolean isDefined(K key) {
        return symbolTableDictionary.containsKey(key);
    }

    @Override
    public V lookUp(K key) {
        return symbolTableDictionary.get(key);
    }

    @Override
    public void update(K key, V value) {
        symbolTableDictionary.put(key, value);
    }

    @Override
    public Map<K, V> getContent() {
        return symbolTableDictionary;
    }

    @Override
    public IDictionary<K, V> deepcopy() {
        IDictionary<K, V> newSymbolTable = new DictionaryWithClonableValues<K, V>();
        for (Map.Entry<K, V> entry: symbolTableDictionary.entrySet()) {
            K kCopy = entry.getKey();
            V vCopy = entry.getValue().deepCopy();
            newSymbolTable.update(kCopy, vCopy);
        }

        return newSymbolTable;
    }

    @Override
    public String toString() {
//        return dictionary.toString();
        String result = "";
        for (Map.Entry<K, V> entry : symbolTableDictionary.entrySet()) {
            result = result.concat(entry.getKey() + " --> " + entry.getValue() + "\n");
        }
        return result;
    }
}
