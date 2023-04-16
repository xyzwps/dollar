package com.xyzwps.dollar.operator;

import com.xyzwps.dollar.tube.Capsule;
import com.xyzwps.dollar.tube.Tube;

public interface Operator<U, D> {
    Capsule<D> next(Tube<U> upstream);
}
