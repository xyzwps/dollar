package com.xyzwps.lib.dollar.operator;

import com.xyzwps.lib.dollar.Tube;
import com.xyzwps.lib.dollar.tube.EndException;

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
    public T next(Tube<T> upstream) throws EndException {
        while (true) {
            T v = upstream.next();
            if (predicateFn.test(v)) {
                return v;
            }
        }
    }
}
