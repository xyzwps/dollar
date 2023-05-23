package com.xyzwps.lib.dollar.stage;

import com.xyzwps.lib.dollar.iterator.EmptyIterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ConcatIterator<T> implements Iterator<T> {

    private final Iterator<T> up;
    private final Iterator<T> tail;

    public ConcatIterator(Iterator<T> up, Iterator<T> tail) {
        this.up = up == null ? EmptyIterator.create() : up;
        this.tail = tail == null ? EmptyIterator.create() : tail;
    }

    @Override
    public boolean hasNext() {
        return this.up.hasNext() || this.tail.hasNext();
    }

    @Override
    public T next() {
        if (this.up.hasNext()) {
            return this.up.next();
        }
        if (this.tail.hasNext()) {
            return this.tail.next();
        }
        throw new NoSuchElementException();
    }
}
