package model.adts;

import model.values.StringValue;

import java.io.BufferedReader;
import java.util.Map;
import java.util.Set;

public interface IFileTable<K, V> {
    boolean isDefined(K k);

    V lookUp(K id);

    void update(K id, V value);

    V remove(K id);


    Set<Map.Entry<StringValue, BufferedReader>> getContent();
}