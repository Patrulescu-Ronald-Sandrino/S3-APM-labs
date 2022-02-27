package model.adts;

public interface IFileTable<K, V> {
    boolean isDefined(K k);

    V lookUp(K id);

    void update(K id, V value);

    V remove(K id);
}