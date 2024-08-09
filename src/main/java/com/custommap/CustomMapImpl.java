package com.custommap;

import java.util.ArrayList;
import java.util.List;

public class CustomMapImpl<K,V> implements CustomMapInterface<K, V> {

    private int numBuckets;
    private List<CustomMapImpl.Bucket<K, V>> list;
    private int size=0;

    CustomMapImpl(int numBuckets)
    {
        list=new ArrayList<CustomMapImpl.Bucket<K, V>>();
        CustomMapImpl.Bucket<K,V> bucket;
        for(int i=0;i<numBuckets;i++)
        {
            bucket=new BucketImpl();
            list.add(bucket);
        }
        this.numBuckets=numBuckets;
    }

    @Override
    public V get(K key) {

        int i=translate(key.hashCode(),numBuckets);
        CustomMapImpl.Bucket<K,V> bucket=list.get(i);
        for(CustomMapImpl.Entry<K, V> entry:bucket.getEntries())
        {
            if(entry.getKey().equals(key))
                return entry.getValue();
        }
        return null;
    }

    @Override
    public V put(K key, V value) {

        int i=translate(key.hashCode(),numBuckets);
        CustomMapImpl.Bucket<K,V> bucket=list.get(i);
        for(CustomMapImpl.Entry<K, V> entry:bucket.getEntries())
        {
            if(entry.getKey().equals(key))
            {
                V val=((EntryImpl)entry).v;
                ((BucketImpl)bucket).list2.remove(entry);
                ((EntryImpl)entry).v=value;
                ((BucketImpl)bucket).list2.add(entry);
                return val;
            }
        }
        CustomMapImpl.Entry<K, V> entryy=new EntryImpl();
        ((EntryImpl)entryy).k=key;
        ((EntryImpl)entryy).v=value;
        ((BucketImpl)bucket).list2.add(entryy);
        size++;
        return null;
    }

    @Override
    public V remove(K key) {
        int i=translate(key.hashCode(),numBuckets);
        CustomMapImpl.Bucket<K,V> bucket=list.get(i);
        for(CustomMapImpl.Entry<K, V> entry:bucket.getEntries())
        {
            if(entry.getKey()==key)
            {
                V val=((EntryImpl)entry).v;
                ((BucketImpl)bucket).list2.remove(entry);
                size--;
                return val;
            }
        }
        return null;
    }

    @Override
    public int size() {

        return size;
    }

    @Override
    public List<? extends CustomMapImpl.Bucket<K, V>> getBuckets() {

        return list;
    }

    public int translate(int hashCode, int size) {
        return Math.abs(hashCode) % size;
    }

    public class EntryImpl implements CustomMapImpl.Entry<K, V>
    {

        K k;
        V v;
        @Override
        public K getKey() {

            return k;
        }

        @Override
        public V getValue() {

            return v;
        }

    }

    public class BucketImpl implements CustomMapImpl.Bucket<K, V>
    {

        private List<CustomMapImpl.Entry<K, V>> list2=new ArrayList<CustomMapImpl.Entry<K, V>>();

        @Override
        public List<? extends CustomMapImpl.Entry<K, V>> getEntries() {

            return list2;
        }

    }

}