package com.xyzwps.dollar.operator;

import com.xyzwps.dollar.tube.Capsule;
import com.xyzwps.dollar.tube.Tube;

import java.util.HashSet;
import java.util.Objects;
import java.util.function.Function;

public class UniqueByOperator<T, K> implements Operator<T, T> {

    private final Function<T, K> toKey;
    private final HashSet<K> keys = new HashSet<>();

    public UniqueByOperator(Function<T, K> toKey) {
        this.toKey = Objects.requireNonNull(toKey);
    }

    @Override
    public Capsule<T> next(Tube<T> upstream) {
        while (true) {
            var parcel = upstream.next();
            switch (parcel) {
                case Capsule.Done<T> ignored -> {
                    return Capsule.done();
                }
                case Capsule.Failure<T> failure -> {
                    return Capsule.failed(failure.cause());
                }
                case Capsule.Carrier<T> carrier -> {
                    var k = this.toKey.apply(carrier.value());
                    if (!keys.contains(k)) {
                        keys.add(k);
                        return carrier;
                    }
                }
            }
        }
    }
}
