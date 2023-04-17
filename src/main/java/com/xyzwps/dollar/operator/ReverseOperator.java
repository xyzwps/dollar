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
            ArrayList<T> list = new ArrayList<>();
            for (boolean go = true; go; ) {
                Capsule<T> c = upstream.next();
                if (c instanceof Capsule.Done) {
                    go = false;
                } else if (c instanceof Capsule.Failure) {
                    return c;
                } else if (c instanceof Capsule.Carrier) {
                    list.add(((Capsule.Carrier<T>) c).value());
                } else {
                    throw new Capsule.UnknownCapsuleException();
                }
            }
            this.drained = true;
            this.itr = new ArrayListReverseIterator<>(list);
        } // end if

        return itr.hasNext() ? Capsule.carry(itr.next()) : Capsule.done();
    }
}
