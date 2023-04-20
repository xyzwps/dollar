package com.xyzwps.lib.dollar.operator;

import com.xyzwps.lib.dollar.tube.Capsule;
import com.xyzwps.lib.dollar.tube.Tube;

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
    public Capsule<R> next(Tube<T> upstream) {
        while (true) {
            if (this.subTube == null) {
                Capsule<T> c = upstream.next();
                if (c instanceof Capsule.Done) {
                    return (Capsule<R>) c;
                } else if (c instanceof Capsule.Carrier) {
                    T v = ((Capsule.Carrier<T>) c).value();
                    this.subTube = this.flatMapFn.apply(v);
                } else {
                    throw new Capsule.UnknownCapsuleException();
                }
            }


            Capsule<R> c = subTube.next();
            if (c instanceof Capsule.Done) {
                this.subTube = null;
            } else if (c instanceof Capsule.Carrier) {
                return c;
            } else {
                throw new Capsule.UnknownCapsuleException();
            }
        }
    }
}
