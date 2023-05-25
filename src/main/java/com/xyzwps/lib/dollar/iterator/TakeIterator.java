package com.xyzwps.lib.dollar.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Used by takeWhile method.
 *
 * @param <T> element type
 */
public class TakeIterator<T> implements Iterator<T> {

    private final Iterator<T> up;
    private final int n;
    private int taken = 0;

    public TakeIterator(Iterator<T> up, int n) {
        this.up = up == null ? EmptyIterator.create() : up;
        if (n <= 0) {
            throw new IllegalArgumentException("You should take at least one element.");
        }
        this.n = n;
    }


    @Override
    public boolean hasNext() {
        if (taken >= n) {
            return false;
        }

        return up.hasNext();
    }

    @Override
    public T next() {
        if (hasNext()) {
            this.taken++;
            return up.next();
        }
        throw new NoSuchElementException();
    }
}
