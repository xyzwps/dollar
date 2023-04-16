package com.xyzwps.dollar.operator;

import com.xyzwps.dollar.tube.Capsule;
import com.xyzwps.dollar.tube.Tube;

import java.util.HashSet;

public class UniqueOperator<T> implements Operator<T, T> {

    private final HashSet<T> set;

    public UniqueOperator() {
        this.set = new HashSet<>();
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
                    var t = carrier.value();
                    if (!set.contains(t)) {
                        set.add(t);
                        return carrier;
                    }
                }
            }
        }
    }
}
