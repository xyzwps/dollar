package com.xyzwps.lib.dollar.operator;

import com.xyzwps.lib.dollar.tube.Tube;
import com.xyzwps.lib.dollar.tube.EndException;

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
    public S next(Tube<T> upstream) throws EndException {
        try {
            T t = upstream.next();
            return combineFn.apply(t, this.itr.hasNext() ? this.itr.next() : null);
        } catch (EndException e) {
            if (this.itr.hasNext()) {
                return combineFn.apply(null, this.itr.next());
            } else {
                throw e;
            }
        }
    }
}
