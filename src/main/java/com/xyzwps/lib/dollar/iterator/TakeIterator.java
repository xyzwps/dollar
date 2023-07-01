package com.xyzwps.lib.dollar.iterator;

import java.util.Iterator;

/**
 * Used by take method.
 *
 * @param <T> element type
 */
public class TakeIterator<T> extends PreGetIterator<T> {

    private final Iterator<T> up;
    private final int n;
    private int count = 0;

    public TakeIterator(Iterator<T> up, int n) {
        if (n < 1) {
            throw new IllegalArgumentException("You should take at least one element.");
        }
        this.n = n;

        this.up = up == null ? EmptyIterator.create() : up;
    }

    @Override
    protected void tryToGetNext() {
        if (count >= n) return;

        if (this.holder.cached()) return;

        if (up.hasNext()) {
            T next = up.next();
            if (count < n) {
                this.holder.accept(next);
            }
            this.count++;
        }
    }

}
