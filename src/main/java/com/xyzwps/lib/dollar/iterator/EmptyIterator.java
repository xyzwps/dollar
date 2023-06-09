package com.xyzwps.lib.dollar.iterator;

import java.util.Iterator;

/**
 * An empty iterator. You can get nothing here.
 *
 * @param <T> element type. Haha.
 */
public class EmptyIterator<T> implements Iterator<T> {

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public T next() {
        return null;
    }

    private EmptyIterator() {
    }

    /**
     * Create an empty iterator.
     *
     * @param <T> element type
     * @return an empty iterator
     */
    public static <T> Iterator<T> create() {
        return new EmptyIterator<>();
    }

}
