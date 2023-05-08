package com.xyzwps.lib.dollar.operator;

import com.xyzwps.lib.dollar.function.IndexedPredicate;
import com.xyzwps.lib.dollar.tube.Capsule;
import com.xyzwps.lib.dollar.tube.Tube;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Used by filter method.
 *
 * @param <T> element type
 */
public class FilterOperator<T> implements Operator<T, T> {

    private final Predicate<T> predicateFn;

    /**
     * @param predicateFn never be null
     */
    public FilterOperator(Predicate<T> predicateFn) {
        this.predicateFn = predicateFn;
    }

    @Override
    public Capsule<T> next(Tube<T> upstream) {
        while (true) {
            Capsule<T> c = upstream.next();
            if (c instanceof Capsule.Done) {
                return c;
            } else if (c instanceof Capsule.Carrier) {
                T v = ((Capsule.Carrier<T>) c).value();
                if (predicateFn.test(v)) {
                    return Capsule.carry(v);
                }
            } else {
                throw new Capsule.UnknownCapsuleException();
            }
        }
    }
}
