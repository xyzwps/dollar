package com.xyzwps.lib.dollar.iterator;

import com.xyzwps.lib.dollar.iterator.EmptyIterator;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiFunction;

public class ZipIterator<T, R, S> implements Iterator<S> {

    private final Iterator<T> left;
    private final Iterator<R> right;
    private final BiFunction<T, R, S> combineFn;

    public ZipIterator(Iterator<T> left, Iterator<R> right, BiFunction<T, R, S> combineFn) {
        this.left = left == null ? EmptyIterator.create() : left;
        this.right = right == null ? EmptyIterator.create() : right;
        this.combineFn = Objects.requireNonNull(combineFn);
    }

    @Override
    public boolean hasNext() {
        return this.hasMore() > 0;
    }

    private int hasMore() {
        return (left.hasNext() ? 1 : 0) + (right.hasNext() ? 2 : 0);
    }

    @Override
    public S next() {
        switch (hasMore()) {
            case 0: throw new NoSuchElementException();
            case 1: return combineFn.apply(left.next(), null);
            case 2: return combineFn.apply(null, right.next());
            case 3: return combineFn.apply(left.next(), right.next());
            default: throw new IllegalStateException();
        }
    }
}
