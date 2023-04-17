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
            Capsule<T> c = upstream.next();
            if (c instanceof Capsule.Done) {
                return c;
            } else if (c instanceof Capsule.Failure) {
                return c;
            } else if (c instanceof Capsule.Carrier) {
                T t = ((Capsule.Carrier<T>) c).value();
                if (!set.contains(t)) {
                    set.add(t);
                    return c;
                }
            } else throw new Capsule.UnknownCapsuleException();
        }
    }
}
