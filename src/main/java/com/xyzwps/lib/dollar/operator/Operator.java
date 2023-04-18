package com.xyzwps.lib.dollar.operator;

import com.xyzwps.lib.dollar.tube.Capsule;
import com.xyzwps.lib.dollar.tube.Tube;

/**
 * Operator definition.
 *
 * @param <U> upstream element type
 * @param <D> downstream element type
 */
public interface Operator<U, D> {
    Capsule<D> next(Tube<U> upstream);
}
