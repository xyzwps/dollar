package com.xyzwps.lib.dollar.operator;

import com.xyzwps.lib.dollar.tube.Capsule;
import com.xyzwps.lib.dollar.tube.Tube;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.BiFunction;

public class ZipOperator<T, R, S> implements Operator<T, S> {

    private final Iterator<R> itr;
    private final BiFunction<T, R, S> combineFn;

    public ZipOperator(Iterator<R> itr, BiFunction<T, R, S> combineFn) {
        this.itr = Objects.requireNonNull(itr);
        this.combineFn = Objects.requireNonNull(combineFn);
    }

    @Override
    public Capsule<S> next(Tube<T> upstream) {
        Capsule<T> c = upstream.next();
        if (c instanceof Capsule.Carrier) {
            T t = ((Capsule.Carrier<T>) c).value();
            return Capsule.carry(combineFn.apply(t, this.itr.hasNext() ? this.itr.next() : null));
        } else {
            return this.itr.hasNext()
                    ? Capsule.carry(combineFn.apply(null, this.itr.next()))
                    : Capsule.done();
        }
    }
}
