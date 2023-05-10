package com.xyzwps.lib.dollar.operator;

import com.xyzwps.lib.dollar.tube.Tube;
import com.xyzwps.lib.dollar.tube.EndException;

import java.util.Objects;
import java.util.function.Function;

/**
 * Used by flatMap method.
 *
 * @param <T> source element type
 * @param <R> flatMap result elements result
 */
public class FlatMapOperator<T, R> implements Operator<T, R> {

    private final Function<T, Tube<R>> flatMapFn;

    private Tube<R> subTube;

    public FlatMapOperator(Function<T, Tube<R>> flatMapFn) {
        this.flatMapFn = Objects.requireNonNull(flatMapFn);
    }

    @Override
    public R next(Tube<T> upstream) throws EndException {
        while (true) {
            if (this.subTube == null) {
                T v = upstream.next();
                this.subTube = this.flatMapFn.apply(v);
            }

            try {
                return subTube.next();
            } catch (EndException e) {
                this.subTube = null;
            }
        }
    }
}
