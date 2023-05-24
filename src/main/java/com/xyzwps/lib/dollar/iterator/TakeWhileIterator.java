package com.xyzwps.lib.dollar.iterator;

import com.xyzwps.lib.dollar.iterator.EmptyIterator;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Used by takeWhile method.
 *
 * @param <T> element type
 */
public class TakeWhileIterator<T> implements Iterator<T> {

    private final Iterator<T> up;
    private final Predicate<T> predicate;

    private T nextCache;
    private boolean nextCached = false;
    private boolean end = false;

    public TakeWhileIterator(Iterator<T> up, Predicate<T> predicate) {
        this.up = up == null ? EmptyIterator.create() : up;
        this.predicate = Objects.requireNonNull(predicate);
    }


    @Override
    public boolean hasNext() {
        if (end) {
            return false;
        }

        if (nextCached) {
            return true;
        }

        if (up.hasNext()) {
            T n = up.next();
            if (predicate.test(n)) {
                this.nextCache = n;
                this.nextCached = true;
                return true;
            } else {
                this.end = true;
            }
        }

        return false;
    }

    @Override
    public T next() {
        if (hasNext()) {
            this.nextCached = false;
            return nextCache;
        }
        throw new NoSuchElementException();
    }
}
