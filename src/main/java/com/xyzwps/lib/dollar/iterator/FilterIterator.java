package com.xyzwps.lib.dollar.iterator;

import com.xyzwps.lib.dollar.function.ObjIntPredicate;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class FilterIterator<T> implements Iterator<T> {

    private final Iterator<T> up;
    private final ObjIntPredicate<T> predicate;

    private T nextCache;
    private boolean nextCached = false;

    private int index = 0;

    public FilterIterator(Iterator<T> up, ObjIntPredicate<T> predicate) {
        this.up = up == null ? EmptyIterator.create() : up;
        this.predicate = Objects.requireNonNull(predicate);
    }

    // TODO: 丑

    @Override
    public boolean hasNext() {
        if (this.nextCached) {
            return true;
        }

        while (this.up.hasNext()) {
            T upnext = this.up.next();
            if (this.predicate.test(upnext, this.index++)) {
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
