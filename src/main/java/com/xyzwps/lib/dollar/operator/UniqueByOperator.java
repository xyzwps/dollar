package com.xyzwps.lib.dollar.operator;

import com.xyzwps.lib.dollar.tube.Capsule;
import com.xyzwps.lib.dollar.tube.Tube;

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
            Capsule<T> c = upstream.next();
            if (c instanceof Capsule.Done) {
                return c;
            } else if (c instanceof Capsule.Failure) {
                return c;
            } else if (c instanceof Capsule.Carrier) {
                K k = this.toKey.apply(((Capsule.Carrier<T>) c).value());
                if (!keys.contains(k)) {
                    keys.add(k);
                    return c;
                }
            } else throw new Capsule.UnknownCapsuleException();
        }
    }
}
