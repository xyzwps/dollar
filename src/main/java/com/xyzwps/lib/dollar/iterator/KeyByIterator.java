package com.xyzwps.lib.dollar.iterator;

import com.xyzwps.lib.dollar.Pair;
import com.xyzwps.lib.dollar.iterator.EmptyIterator;

import java.util.*;
import java.util.function.Function;

public class KeyByIterator<T, K> implements Iterator<Pair<K, T>> {

    private final Function<T, K> toKey;
    private final Iterator<T> up;
    private final Set<K> visitedKeys = new HashSet<>();

    private Pair<K, T> nextCache; // TODO: 这东西用了很多，感觉可以把这种公共的逻辑抽出来
    private boolean nextCached = false;

    public KeyByIterator(Iterator<T> up, Function<T, K> toKey) {
        this.up = up == null ? EmptyIterator.create() : up;
        this.toKey = Objects.requireNonNull(toKey);
    }

    @Override
    public boolean hasNext() {
        this.tryToGetNext();
        return this.nextCached;
    }

    @Override
    public Pair<K, T> next() {
        this.tryToGetNext();

        if (this.nextCached) {
            this.nextCached = false;
            return this.nextCache;
        } else {
            throw new NoSuchElementException();
        }
    }

    private void tryToGetNext() {
        if (this.nextCached) return;

        while (up.hasNext()) {
            T upnext = up.next();
            K key = this.toKey.apply(upnext);
            if (!visitedKeys.contains(key)) {
                this.nextCache = Pair.of(key, upnext);
                this.nextCached = true;
                this.visitedKeys.add(key);
                return;
            }
        }
    }


}
