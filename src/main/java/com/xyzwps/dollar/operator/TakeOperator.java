package com.xyzwps.dollar.operator;

import com.xyzwps.dollar.tube.Capsule;
import com.xyzwps.dollar.tube.Tube;

public class TakeOperator<T> implements Operator<T, T> {

    private final int n;

    private int taken = 0;

    public TakeOperator(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Take count should be greater than or equal to 0.");
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
