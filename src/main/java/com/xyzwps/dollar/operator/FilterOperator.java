package com.xyzwps.dollar.operator;

import com.xyzwps.dollar.tube.Capsule;
import com.xyzwps.dollar.tube.Tube;

import java.util.Objects;
import java.util.function.Predicate;

public class FilterOperator<T> implements Operator<T, T> {

    private final Predicate<T> predicateFn;

    public FilterOperator(Predicate<T> predicateFn) {
        this.predicateFn = Objects.requireNonNull(predicateFn);
    }

    @Override
    public Capsule<T> next(Tube<T> upstream) {
        while (true) {
            Capsule<T> c = upstream.next();
            if (c instanceof Capsule.Done) {
                return c;
            } else if (c instanceof Capsule.Failure) {
                return c;
            } else if (c instanceof Capsule.Carrier) {
                T v = ((Capsule.Carrier<T>) c).value();
                if (predicateFn.test(v)) {
                    return Capsule.carry(v);
                }
            } else {
                throw new Capsule.UnknownCapsuleException();
            }
        }
    }
}
