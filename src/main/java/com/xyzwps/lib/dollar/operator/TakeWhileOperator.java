package com.xyzwps.lib.dollar.operator;

import com.xyzwps.lib.dollar.Tube;
import com.xyzwps.lib.dollar.tube.EndException;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Used by takeWhile method.
 *
 * @param <T> element type
 */
public class TakeWhileOperator<T> implements Operator<T, T> {

    private final Predicate<T> predicate;

    public TakeWhileOperator(Predicate<T> predicate) {
        this.predicate = Objects.requireNonNull(predicate);
    }

    @Override
    public T next(Tube<T> upstream) throws EndException {
        T t = upstream.next();
        if (predicate.test(t)) {
            return t;
        }
        throw new EndException();
    }
}
