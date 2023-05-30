package com.xyzwps.lib.dollar.iterator;

import com.xyzwps.lib.dollar.function.ObjIntPredicate;

import java.util.Iterator;
import java.util.Objects;

public class FilterIterator<T> extends PreGetIterator<T> {

    private final Iterator<T> up;
    private final ObjIntPredicate<T> predicate;
    private int index = 0;

    public FilterIterator(Iterator<T> up, ObjIntPredicate<T> predicate) {
        this.up = up == null ? EmptyIterator.create() : up;
        this.predicate = Objects.requireNonNull(predicate);
    }

    @Override
    protected void tryToGetNext() {
        if (this.holder.cached()) {
            return;
        }

        while (this.up.hasNext()) {
            T upnext = this.up.next();
            if (this.predicate.test(upnext, this.index++)) {
                this.holder.accept(upnext);
                return;
            }
        }
    }
}
