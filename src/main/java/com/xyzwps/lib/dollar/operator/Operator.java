package com.xyzwps.lib.dollar.operator;

import com.xyzwps.lib.dollar.Tube;
import com.xyzwps.lib.dollar.tube.EndException;

/**
 * Operator definition.
 *
 * @param <U> upstream element type
 * @param <D> downstream element type
 */
public interface Operator<U, D> {
    D next(Tube<U> upstream) throws EndException;
}
