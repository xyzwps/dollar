package com.xyzwps.lib.dollar.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

// TODO: 测试
public final class ArrayIterator<T> implements Iterator<T> {

    private final T[] array;

    private int current;

    public ArrayIterator(T[] array) {
        this.array = Objects.requireNonNull(array);
    }

    @Override
    public boolean hasNext() {
        return this.current < array.length;
    }

    @Override
    public T next() {
        if (this.current < this.array.length) {
            return this.array[this.current++];
        }

        throw new NoSuchElementException();
    }
}
