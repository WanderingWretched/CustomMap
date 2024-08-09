package com.custommap;

import java.util.ArrayList;
import java.util.List;

public interface CustomMapInterface<K, V> {
    public V get(K key);
    public V put(K key, V value);
    public V remove(K key);
    public int size();
    public List<? extends Bucket<K, V>> getBuckets();

    public static interface Entry<K, V> {
        public K getKey();
        public V getValue();
    }

    public static interface Bucket<K, V> {
        public List<? extends Entry<K, V>> getEntries();
    }
}