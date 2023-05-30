package com.xyzwps.lib.dollar.iterator;

import com.xyzwps.lib.dollar.Pair;

import java.util.*;
import java.util.function.Function;

public class KeyByIterator<T, K> extends PreGetIterator<Pair<K, T>> {

    private final Function<T, K> toKey;
    private final Iterator<T> up;
    private final Set<K> visitedKeys = new HashSet<>();

    public KeyByIterator(Iterator<T> up, Function<T, K> toKey) {
        this.up = up == null ? EmptyIterator.create() : up;
        this.toKey = Objects.requireNonNull(toKey);
    }

    @Override
    protected void tryToGetNext() {
        if (this.holder.cached()) return;

        while (up.hasNext()) {
            T upnext = up.next();
            K key = this.toKey.apply(upnext);
            if (!visitedKeys.contains(key)) {
                this.holder.accept(Pair.of(key, upnext));
                this.visitedKeys.add(key);
                return;
            }
        }
    }
}
