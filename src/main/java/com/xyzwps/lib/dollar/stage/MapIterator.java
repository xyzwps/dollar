package com.xyzwps.lib.dollar.stage;

import com.xyzwps.lib.dollar.function.IndexedFunction;
import com.xyzwps.lib.dollar.iterator.EmptyIterator;

import java.util.Iterator;
import java.util.Objects;

/**
 * Used by map method.
 *
 * @param <T> source element type
 * @param <R> mapped element type
 */
public class MapIterator<T, R> implements Iterator<R> {

    private final IndexedFunction<T, R> mapFn;
    private final Iterator<T> up;
    private int index = 0;

    public MapIterator(Iterator<T> up, IndexedFunction<T, R> mapFn) {
        this.up = up == null ? EmptyIterator.create() : up;
        this.mapFn = Objects.requireNonNull(mapFn);
    }


    @Override
    public boolean hasNext() {
        return this.up.hasNext();
    }

    @Override
    public R next() {
        return mapFn.apply(this.up.next(), this.index++);
    }
}
