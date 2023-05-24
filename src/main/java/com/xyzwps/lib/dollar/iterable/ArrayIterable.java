package com.xyzwps.lib.dollar.iterable;

import com.xyzwps.lib.dollar.iterator.ArrayIterator;
import com.xyzwps.lib.dollar.iterator.EmptyIterator;

import java.util.Iterator;

public class ArrayIterable<T> implements Iterable<T> {

    private final T[] array;

    public ArrayIterable(T[] array) {
        this.array = array;
    }

    @Override
    public Iterator<T> iterator() {
        return this.array == null ? EmptyIterator.create() : new ArrayIterator<>(this.array);
    }
}
