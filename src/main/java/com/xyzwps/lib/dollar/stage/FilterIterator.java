package com.xyzwps.lib.dollar.stage;

import com.xyzwps.lib.dollar.iterator.EmptyIterator;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;

public class FilterIterator<T> implements Iterator<T> {

    private final Iterator<T> up;
    private final Predicate<T> predicate;

    private T nextCache;
    private boolean nextCached = false;

    public FilterIterator(Iterator<T> up, Predicate<T> predicate) {
        this.up = up == null ? EmptyIterator.create() : up;
        this.predicate = Objects.requireNonNull(predicate);
    }

    @Override
    public boolean hasNext() {
        if (this.nextCached) {
            return true;
        }

        while (this.up.hasNext()) {
            T upnext = this.up.next();
            if (this.predicate.test(upnext)) {
                this.nextCache = upnext;
                this.nextCached = true;
                return true;
            }
        }

        return false;
    }

    @Override
    public T next() {
        if (hasNext()) {
            this.nextCached = false;
            return this.nextCache;
        }

        throw new NoSuchElementException();
    }
}
