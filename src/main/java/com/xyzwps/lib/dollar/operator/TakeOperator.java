package com.xyzwps.lib.dollar.operator;

import com.xyzwps.lib.dollar.tube.Capsule;
import com.xyzwps.lib.dollar.tube.Tube;

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
    public Capsule<T> next(Tube<T> upstream) {
        if (this.taken >= this.n) {
            return Capsule.done();
        }

        return Capsule.map(upstream.next(), carrier -> {
            this.taken++;
            return carrier;
        });
    }
}
