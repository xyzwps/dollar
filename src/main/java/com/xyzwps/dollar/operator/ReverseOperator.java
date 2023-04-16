package com.xyzwps.dollar.operator;

import com.xyzwps.dollar.iterator.ArrayListReverseIterator;
import com.xyzwps.dollar.tube.Capsule;
import com.xyzwps.dollar.tube.Tube;

import java.util.ArrayList;
import java.util.Iterator;

public class ReverseOperator<T> implements Operator<T, T> {

    private Iterator<T> itr;

    private boolean drained = false;

    @Override
    public Capsule<T> next(Tube<T> upstream) {
        if (!drained) {
            var list = new ArrayList<T>();
            for (boolean go = true; go; ) {
                switch (upstream.next()) {
                    case Capsule.Done<T> ignored -> go = false;
                    case Capsule.Carrier<T> carrier -> list.add(carrier.value());
                    case Capsule.Failure<T> failure -> {
                        return Capsule.failed(failure.cause());
                    }
                }
            }
            this.drained = true;
            this.itr = new ArrayListReverseIterator<>(list);
        } // end if

        return itr.hasNext() ? Capsule.carry(itr.next()) : Capsule.done();
    }
}
