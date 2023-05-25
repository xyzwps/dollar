package com.xyzwps.lib.dollar.iterator;

import com.xyzwps.lib.dollar.Unreachable;

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
        return this.hasMore() > 0;
    }

    private int hasMore() {
        if (this.up.hasNext()) {
            return 1;
        }
        if (this.tail.hasNext()) {
            return 2;
        }
        return 0;
    }

    @Override
    public T next() {
        switch (this.hasMore()) {
            case 0: throw new NoSuchElementException();
            case 1: return this.up.next();
            case 2: return this.tail.next();
            default: throw new Unreachable();
        }
    }
}
