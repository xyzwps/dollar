package com.xyzwps.lib.dollar.iterator;


import com.xyzwps.lib.dollar.iterable.EmptyIterable;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;

public class FlatMapIterator<T, R> extends PreGetIterator<R> {
    private final Iterator<T> up;
    private final Function<T, Iterable<R>> flatMapFn;
    private Iterator<R> itr;

    public FlatMapIterator(Iterator<T> up, Function<T, Iterable<R>> flatMapFn) {
        this.up = up == null ? EmptyIterator.create() : up;
        this.flatMapFn = Objects.requireNonNull(flatMapFn);
    }

    @Override
    protected void tryToGetNext() {
        if (this.holder.cached()) return;

        while (true) {
            if (this.itr != null) {
                if (this.itr.hasNext()) {
                    this.holder.accept(this.itr.next());
                    return;
                } else {
                    this.itr = null;
                }
            }

            if (up.hasNext()) {
                Iterable<R> flat = flatMapFn.apply(up.next());
                flat = flat == null ? EmptyIterable.create() : flat;
                this.itr = flat.iterator();
            } else {
                return;
            }
        }
    }
}
