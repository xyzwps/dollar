package com.xyzwps.lib.dollar.operator;

import com.xyzwps.lib.dollar.collector.ListCollector;
import com.xyzwps.lib.dollar.iterator.ArrayListReverseIterator;
import com.xyzwps.lib.dollar.Tube;
import com.xyzwps.lib.dollar.tube.EndException;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Used by reserve method.
 *
 * @param <T> source element type.
 */
public class ReverseOperator<T> implements Operator<T, T> {

    private Iterator<T> itr;

    @Override
    public T next(Tube<T> upstream) throws EndException {
        if (this.itr == null) {
            this.initItr(upstream);
        }

        if (itr.hasNext()) {
            return itr.next();
        } else {
            throw new EndException();
        }
    }

    private void initItr(Tube<T> upstream) {
        ArrayList<T> list = upstream.collect(new ListCollector<>());
        this.itr = new ArrayListReverseIterator<>(list);
    }
}