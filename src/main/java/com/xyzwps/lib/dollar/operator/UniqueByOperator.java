package com.xyzwps.lib.dollar.operator;

import com.xyzwps.lib.dollar.Tube;
import com.xyzwps.lib.dollar.tube.EndException;

import java.util.HashSet;
import java.util.Objects;
import java.util.function.Function;

/**
 * Used by uniqueBy method
 *
 * @param <T> element type
 * @param <K> element key type
 */
public class UniqueByOperator<T, K> implements Operator<T, T> {

    private final Function<T, K> toKey;
    private final HashSet<K> keys = new HashSet<>();

    public UniqueByOperator(Function<T, K> toKey) {
        this.toKey = Objects.requireNonNull(toKey);
    }

    @Override
    public T next(Tube<T> upstream) throws EndException {
        while (true) {
            T t = upstream.next();
            K k = this.toKey.apply(t);
            if (!keys.contains(k)) {
                keys.add(k);
                return t;
            }
        }
    }
}
