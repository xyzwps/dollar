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
        return Capsule.map(upstream.next(), carrier -> Capsule.carry(mapFn.apply(carrier.value())));
    }
}
