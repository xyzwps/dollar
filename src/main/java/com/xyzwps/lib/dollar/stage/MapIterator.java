package com.xyzwps.lib.dollar.stage;

import com.xyzwps.lib.dollar.iterator.EmptyIterator;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;

/**
 * Used by map method.
 *
 * @param <T> source element type
 * @param <R> mapped element type
 */
public class MapIterator<T, R> implements Iterator<R> {

    private final Function<T, R> mapFn;
    private final Iterator<T> up;

    public MapIterator(Iterator<T> up, Function<T, R> mapFn) {
        this.up = up == null ? EmptyIterator.create() : up;
        this.mapFn = Objects.requireNonNull(mapFn);
    }


    @Override
    public boolean hasNext() {
        return this.up.hasNext();
    }

    @Override
    public R next() {
        return mapFn.apply(this.up.next());
    }
}
