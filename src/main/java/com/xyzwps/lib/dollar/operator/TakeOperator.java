package com.xyzwps.lib.dollar.operator;

import com.xyzwps.lib.dollar.Tube;
import com.xyzwps.lib.dollar.tube.EndException;

/**
 * Used by take method.
 *
 * @param <T> element type
 */
public class TakeOperator<T> implements Operator<T, T> {

    private final int n;

    private int taken = 0;

    public TakeOperator(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Take count should be greater than 0.");
        }
        this.n = n;
    }

    @Override
    public T next(Tube<T> upstream) throws EndException {
        if (this.taken >= this.n) {
            throw new EndException();
        }

        T t = upstream.next();
        this.taken++;
        return t;
    }
}
