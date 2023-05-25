package com.xyzwps.lib.dollar.iterator;

import java.util.Iterator;

import static com.xyzwps.lib.dollar.Dollar.*;

public class ReverseIterator<T> implements Iterator<T> {

    private final Iterator<T> up;

    public ReverseIterator(Iterator<T> up) {
        this.up = up == null ? EmptyIterator.create() : up;
    }

    private Iterator<T> itr;

    @Override
    public boolean hasNext() {
        return this.getItr().hasNext();
    }

    @Override
    public T next() {
        return this.getItr().next();
    }

    private Iterator<T> getItr() {
        if (this.itr != null) {
            return this.itr;
        }

        this.itr = new ArrayListReverseIterator<>($.listFrom(up));
        return this.itr;
    }
}
