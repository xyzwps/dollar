package com.xyzwps.lib.dollar.tube;

import com.xyzwps.lib.dollar.operator.Operator;

public class ListTubeStage<U, T> extends ListTube<T> {

    private final Operator<U, T> op;
    private final Tube<U> upstream;

    public ListTubeStage(Operator<U, T> op, Tube<U> upstream) {
        this.op = op;
        this.upstream = upstream;
    }

    @Override
    public Capsule<T> next() {
        return op.next(upstream);
    }
}
