package dev.luzifer.data;

import java.util.Map;

public interface Mapper<K, V> {

    void map(K key, V value);

    boolean hasMapped(K key);

    boolean hasAnyMapped();

    V getMapping(K key);

    Map<K, V> getMappings();

}
