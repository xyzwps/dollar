package com.xyzwps.lib.dollar.operator;

import com.xyzwps.lib.dollar.function.IndexedFunction;
import com.xyzwps.lib.dollar.tube.Capsule;
import com.xyzwps.lib.dollar.tube.Tube;

import java.util.Objects;
import java.util.function.Function;

/**
 * Used by map method.
 *
 * @param <T> source element type
 * @param <R> mapped element type
 */
public class IndexedMapOperator<T, R> implements Operator<T, R> {

    private final IndexedFunction<T, R> mapFn;
    private int index = 0;

    public IndexedMapOperator(IndexedFunction<T, R> mapFn) {
        this.mapFn = Objects.requireNonNull(mapFn);
    }

    @Override
    public Capsule<R> next(Tube<T> upstream) {
        return Capsule.map(upstream.next(), carrier -> Capsule.carry(mapFn.apply(carrier.value(), this.index++)));
    }
}
