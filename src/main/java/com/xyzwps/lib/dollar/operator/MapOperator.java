package com.xyzwps.lib.dollar.operator;

import com.xyzwps.lib.dollar.tube.Capsule;
import com.xyzwps.lib.dollar.tube.Tube;

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