package com.xyzwps.lib.dollar.operator;

import com.xyzwps.lib.dollar.Tube;
import com.xyzwps.lib.dollar.tube.EndException;

import java.util.Objects;
import java.util.function.Function;

/**
 * Used by map method.
 *
 * @param <T> source element type
 * @param <R> mapped element type
 */
public class MapOperator<T, R> implements Operator<T, R> {

    private final Function<T, R> mapFn;

    public MapOperator(Function<T, R> mapFn) {
        this.mapFn = Objects.requireNonNull(mapFn);
    }

    @Override
    public R next(Tube<T> upstream) throws EndException {
        return mapFn.apply(upstream.next());
    }
}
