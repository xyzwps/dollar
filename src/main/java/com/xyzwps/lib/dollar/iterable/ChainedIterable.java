package com.xyzwps.lib.dollar.iterable;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;

public class ChainedIterable<T, R> implements Iterable<R> {

    private final Iterable<T> up;
    private final Function<Iterator<T>, Iterator<R>> chain;

    private ChainedIterable(Iterable<T> up, Function<Iterator<T>, Iterator<R>> chain) {
        this.up = up == null ? EmptyIterable.create() : up;
        this.chain = Objects.requireNonNull(chain);
    }

    @Override
    public Iterator<R> iterator() {
        return chain.apply(up.iterator());
    }

    public static <T, R> ChainedIterable<T, R> create(Iterable<T> up, Function<Iterator<T>, Iterator<R>> chain) {
        return new ChainedIterable<>(up, chain);
    }
}
