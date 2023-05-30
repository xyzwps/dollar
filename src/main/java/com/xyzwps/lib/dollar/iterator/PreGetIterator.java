package com.xyzwps.lib.dollar.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class PreGetIterator<T> implements Iterator<T> {

    protected final PreGetHolder<T> holder = new PreGetHolder<>();

    @Override
    public boolean hasNext() {
        this.tryToGetNext();
        return this.holder.cached();
    }

    protected abstract void tryToGetNext();

    @Override
    public T next() {
        this.tryToGetNext();
        if (this.holder.cached()) {
            return this.holder.evict();
        }
        throw new NoSuchElementException();
    }

    public static class PreGetHolder<T> {
        private T value;
        private boolean cached = false;

        public void accept(T value) {
            if (this.cached) {
                throw new IllegalStateException("A value has been cached.");
            }
            this.value = value;
            this.cached = true;
        }

        public boolean cached() {
            return cached;
        }

        public T evict() {
            if (this.cached) {
                this.cached = false;
                return this.value;
            }
            throw new IllegalStateException("Nothing to evict.");
        }
    }
}