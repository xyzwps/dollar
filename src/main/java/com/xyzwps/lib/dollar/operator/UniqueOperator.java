package com.xyzwps.lib.dollar.operator;

import com.xyzwps.lib.dollar.tube.Tube;
import com.xyzwps.lib.dollar.tube.EndException;

import java.util.HashSet;

/**
 * Used by unique method
 *
 * @param <T> element type
 */
public class UniqueOperator<T> implements Operator<T, T> {

    private final HashSet<T> set;

    public UniqueOperator() {
        this.set = new HashSet<>();
    }

    @Override
    public T next(Tube<T> upstream) throws EndException {
        while (true) {
            T t = upstream.next();
            if (!set.contains(t)) {
                set.add(t);
                return t;
            }
        }
    }
}
