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
            var parcel = upstream.next();
            switch (parcel) {
                case Capsule.Done<T> ignored -> {
                    return Capsule.done();
                }
                case Capsule.Failure<T> failure -> {
                    return Capsule.failed(failure.cause());
                }
                case Capsule.Carrier<T> carrier -> {
                    var v = carrier.value();
                    if (predicateFn.test(v)) {
                        return Capsule.carry(v);
                    }
                }
            }
        }
    }
}
