package com.xyzwps.dollar.operator;

import com.xyzwps.dollar.tube.Capsule;
import com.xyzwps.dollar.tube.Tube;

import java.util.Objects;
import java.util.function.Function;

import static com.xyzwps.dollar.tube.Capsule.carry;
import static com.xyzwps.dollar.tube.Capsule.failed;

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
                switch (upstream.next()) {
                    case Capsule.Done<T> ignored -> {
                        return Capsule.done();
                    }
                    case Capsule.Failure<T> failure -> {
                        return Capsule.failed(failure.cause());
                    }
                    case Capsule.Carrier<T> carrier -> this.subTube = this.flatMapFn.apply(carrier.value());
                }
            }

            switch (subTube.next()) {
                case Capsule.Done<R> ignored -> this.subTube = null;
                case Capsule.Failure<R> failure -> {
                    return failed(failure.cause());
                }
                case Capsule.Carrier<R> carrier -> {
                    return carry(carrier.value());
                }
            }
        }
    }
}
