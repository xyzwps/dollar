package com.xyzwps.lib.dollar.iterator;


import com.xyzwps.lib.dollar.iterable.EmptyIterable;
import com.xyzwps.lib.dollar.iterator.EmptyIterator;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;

public class FlatMapIterator<T, R> implements Iterator<R> {
    private final Iterator<T> up;
    private final Function<T, Iterable<R>> flatMapFn;

    private Iterator<R> itr;

    private R nextCache;
    private boolean nextCached = false;

    public FlatMapIterator(Iterator<T> up, Function<T, Iterable<R>> flatMapFn) {
        this.up = up == null ? EmptyIterator.create() : up;
        this.flatMapFn = Objects.requireNonNull(flatMapFn);
    }

    // TODO: 丑

    @Override
    public boolean hasNext() {
        if (this.nextCached) return true;

        // TODO: 看一下其他 iterator，尽量都改成这种风格
        while (true) {
            if (this.itr != null) {
                if (this.itr.hasNext()) {
                    this.nextCache = this.itr.next();
                    this.nextCached = true;
                    return true;
                } else {
                    this.itr = null;
                }
            }

            if (up.hasNext()) {
                Iterable<R> flat = flatMapFn.apply(up.next());
                flat = flat == null ? EmptyIterable.create() : flat;
                this.itr = flat.iterator();
            } else {
                return false;
            }
        }
    }

    @Override
    public R next() {
        if (this.hasNext()) {
            this.nextCached = false;
            return this.nextCache;
        }
        throw new NoSuchElementException();
    }
}
