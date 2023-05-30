package com.xyzwps.lib.dollar.iterator;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Used by takeWhile method.
 *
 * @param <T> element type
 */
public class TakeWhileIterator<T> extends PreGetIterator<T> {

    private final Iterator<T> up;
    private final Predicate<T> predicate;
    private boolean end = false;

    public TakeWhileIterator(Iterator<T> up, Predicate<T> predicate) {
        this.up = up == null ? EmptyIterator.create() : up;
        this.predicate = Objects.requireNonNull(predicate);
    }

    @Override
    protected void tryToGetNext() {
        if (end) return;

        if (this.holder.cached()) return;

        if (up.hasNext()) {
            T n = up.next();
            if (predicate.test(n)) {
                this.holder.accept(n);
            } else {
                this.end = true;
            }
        }
    }

}
