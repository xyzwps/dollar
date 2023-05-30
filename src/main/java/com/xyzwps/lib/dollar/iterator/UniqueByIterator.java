package com.xyzwps.lib.dollar.iterator;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;

public class UniqueByIterator<T, K> extends PreGetIterator<T> {

    private final Iterator<T> up;
    private final Function<T, K> toKey;
    private final HashSet<K> keys = new HashSet<>();

    public UniqueByIterator(Iterator<T> up, Function<T, K> toKey) {
        this.up = up == null ? EmptyIterator.create() : up;
        this.toKey = Objects.requireNonNull(toKey);
    }

    @Override
    protected void tryToGetNext() {
        if (holder.cached()) return;

        while (up.hasNext()) {
            T upnext = up.next();
            K key = this.toKey.apply(upnext);
            if (!this.keys.contains(key)) {
                this.keys.add(key);
                this.holder.accept(upnext);
                return;
            }
        }
    }


}