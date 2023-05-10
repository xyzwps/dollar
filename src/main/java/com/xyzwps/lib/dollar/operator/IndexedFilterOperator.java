package com.xyzwps.lib.dollar.operator;

import com.xyzwps.lib.dollar.function.IndexedPredicate;
import com.xyzwps.lib.dollar.tube.Tube;
import com.xyzwps.lib.dollar.tube.EndException;

/**
 * Used by filter method.
 *
 * @param <T> element type
 */
public class IndexedFilterOperator<T> implements Operator<T, T> {

    private final IndexedPredicate<T> predicateFn;
    private int index = 0;

    /**
     * @param predicateFn never be null
     */
    public IndexedFilterOperator(IndexedPredicate<T> predicateFn) {
        this.predicateFn = predicateFn;
    }

    @Override
    public T next(Tube<T> upstream) throws EndException {
        while (true) {
            T v = upstream.next();
            if (predicateFn.test(v, this.index++)) {
                return v;
            }
        }
    }
}
