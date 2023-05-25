package com.xyzwps.lib.dollar.iterator;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;

public class UniqueByIterator<T, K> implements Iterator<T> {

    private final Iterator<T> up;
    private final Function<T, K> toKey;
    private final HashSet<K> keys = new HashSet<>();

    public UniqueByIterator(Iterator<T> up, Function<T, K> toKey) {
        this.up = up == null ? EmptyIterator.create() : up;
        this.toKey = Objects.requireNonNull(toKey);
    }

    private T nextCache;
    private boolean nextCached = false;

    @Override
    public boolean hasNext() {
        if (nextCached) return true;

        while (true) {
            if (up.hasNext()) {
                T upnext = up.next();
                K key = this.toKey.apply(upnext);
                if (!this.keys.contains(key)) {
                    this.keys.add(key);
                    this.nextCache = upnext;
                    this.nextCached = true;
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    @Override
    public T next() {
        if (hasNext()) {
            this.nextCached = false;
            return this.nextCache;
        }
        throw new NoSuchElementException();
    }
}