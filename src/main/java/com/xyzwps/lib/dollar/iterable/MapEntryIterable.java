package com.xyzwps.lib.dollar.iterable;

import com.xyzwps.lib.dollar.Pair;
import com.xyzwps.lib.dollar.iterator.MapEntryIterator;

import java.util.Iterator;
import java.util.Map;

import static com.xyzwps.lib.dollar.Dollar.*;

public class MapEntryIterable<K, V> implements Iterable<Pair<K, V>> {

    private final Map<K, V> map;

    public MapEntryIterable(Map<K, V> map) {
        this.map = map == null ? $.hashMap() : map;
    }

    @Override
    public Iterator<Pair<K, V>> iterator() {
        return new MapEntryIterator<>(this.map);
    }
}
