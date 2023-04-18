package com.xyzwps.lib.dollar.operator;

import com.xyzwps.lib.dollar.tube.Capsule;
import com.xyzwps.lib.dollar.tube.Tube;

import java.util.Objects;
import java.util.function.Predicate;

public class TakeWhileOperator<T> implements Operator<T, T> {

    private final Predicate<T> predicate;

    public TakeWhileOperator(Predicate<T> predicate) {
        this.predicate = Objects.requireNonNull(predicate);
    }

    @Override
    public Capsule<T> next(Tube<T> upstream) {
        return Capsule.map(upstream.next(), carrier -> predicate.test(carrier.value()) ? carrier : Capsule.done());
    }
}
