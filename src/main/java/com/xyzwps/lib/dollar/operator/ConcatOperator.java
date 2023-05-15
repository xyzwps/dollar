package com.xyzwps.lib.dollar.operator;

import com.xyzwps.lib.dollar.Tube;
import com.xyzwps.lib.dollar.iterator.EmptyIterator;
import com.xyzwps.lib.dollar.tube.EndException;

import java.util.Iterator;

public class ConcatOperator<T> implements Operator<T, T> {

    private final Iterator<T> itr;
    private boolean drained;

    public ConcatOperator(Iterable<T> iterable) {
        this.itr = iterable == null ? EmptyIterator.create() : iterable.iterator();
        this.drained = false;
    }


    @Override
    public T next(Tube<T> upstream) throws EndException {
        if (!this.drained) {
            try {
                return upstream.next();
            } catch (EndException e) {
                this.drained = true;
            }
        }

        if (this.itr.hasNext()) {
            return this.itr.next();
        } else {
            throw new EndException();
        }
    }
}
