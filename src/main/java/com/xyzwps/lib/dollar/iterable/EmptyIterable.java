package com.xyzwps.lib.dollar.iterable;

import com.xyzwps.lib.dollar.iterator.EmptyIterator;

import java.util.Iterator;

public class EmptyIterable<T> implements Iterable<T> {

    private final Iterator<T> iterator = EmptyIterator.create();

    @Override
    public Iterator<T> iterator() {
        return iterator;
    }

    public static <T> EmptyIterable<T> create() {
        return new EmptyIterable<>();
    }
}
