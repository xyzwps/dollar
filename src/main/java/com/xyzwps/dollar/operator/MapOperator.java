package com.xyzwps.dollar.operator;

import com.xyzwps.dollar.tube.Capsule;
import com.xyzwps.dollar.tube.Tube;

import java.util.Objects;
import java.util.function.Function;

public class MapOperator<T, R> implements Operator<T, R> {

    private final Function<T, R> mapFn;

    public MapOperator(Function<T, R> mapFn) {
        this.mapFn = Objects.requireNonNull(mapFn);
    }

    @Override
    public Capsule<R> next(Tube<T> upstream) {
        return switch (upstream.next()) {
            case Capsule.Done<T> ignored -> Capsule.done();
            case Capsule.Failure<T> failure -> Capsule.failed(failure.cause());
            case Capsule.Carrier<T> carrier -> Capsule.carry(mapFn.apply(carrier.value()));
        };
    }
}
