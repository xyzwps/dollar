package com.xyzwps.lib.dollar.operator;

import com.xyzwps.lib.dollar.function.IndexedFunction;
import com.xyzwps.lib.dollar.tube.Tube;
import com.xyzwps.lib.dollar.tube.EndException;

import java.util.Objects;

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
    public R next(Tube<T> upstream) throws EndException {
        return mapFn.apply(upstream.next(), this.index++);
    }
}
