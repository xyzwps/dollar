package com.xyzwps.lib.dollar.iterator;

import com.xyzwps.lib.dollar.Pair;

import java.util.Iterator;
import java.util.Map;

import static com.xyzwps.lib.dollar.Dollar.*;

/**
 * Iterate over the entry set of a map in default order.
 *
 * @param <K> map key type
 * @param <V> map value type
 */
public final class MapEntryIterator<K, V> implements Iterator<Pair<K, V>> {

    private final Map<K, V> map;
    private final Iterator<K> keyItr;

    public MapEntryIterator(Map<K, V> map) {
        this.map = map == null ? $.hashMap() : map;
        this.keyItr = this.map.keySet().iterator();
    }

    @Override
    public boolean hasNext() {
        return keyItr.hasNext();
    }

    @Override
    public Pair<K, V> next() {
        K key = keyItr.next();
        return new Pair<>(key, map.get(key));
    }
}