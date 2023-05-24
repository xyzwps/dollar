package com.xyzwps.lib.dollar.stage;

import com.xyzwps.lib.dollar.Pair;
import com.xyzwps.lib.dollar.iterator.EmptyIterator;
import com.xyzwps.lib.dollar.iterator.MapEntryIterator;

import java.util.*;
import java.util.function.Function;

// TODO: 测试
public class GroupByIterator<T, K> implements Iterator<Pair<K, List<T>>> {

    private final Function<T, K> toKey;
    private final Iterator<T> up;
    private MapEntryIterator<K, List<T>> itr;

    public GroupByIterator(Iterator<T> up, Function<T, K> toKey) {
        this.up = up == null ? EmptyIterator.create() : up;
        this.toKey = Objects.requireNonNull(toKey);
    }

    @Override
    public boolean hasNext() {
        this.tryToInitItr();
        return this.itr.hasNext();
    }

    @Override
    public Pair<K, List<T>> next() {
        this.tryToInitItr();
        return this.itr.next();
    }

    private void tryToInitItr() {
        if (itr != null) return;

        Map<K, List<T>> map = new HashMap<>();
        while (up.hasNext()) {
            T upnext = up.next();
            K key = this.toKey.apply(upnext);
            List<T> list = map.computeIfAbsent(key, k -> new ArrayList<>());
            list.add(upnext);
        }
        this.itr = new MapEntryIterator<>(map);
    }
}
