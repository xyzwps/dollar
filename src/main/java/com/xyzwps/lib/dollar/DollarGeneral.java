package com.xyzwps.lib.dollar;

import java.util.Objects;

interface DollarGeneral {

    /**
     * Checks <code>value</code> to determine whether a default value
     * should be returned in its place. The <code>defaultValue</code>
     * is returned when <code>value</code> is <code>null</code>.
     *
     * @param value        The value to check
     * @param defaultValue The default value
     * @param <T>          value type
     * @return resolved value
     */
    default <T> T defaultTo(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }


    /**
     * Check value which is falsey or not. The values <code>null</code>, <code>false</code>,
     * <code>0(.0)</code> and <code>""</code> are falsey.
     *
     * @param value value to be checked
     * @return true if the value is falsey
     * @see ListTube#compact
     */
    default boolean isFalsey(Object value) {
        return value == null
                || Objects.equals(value, false)
                || "".equals(value)
                || Objects.equals(value, 0)
                || Objects.equals(value, 0L)
                || Objects.equals(value, 0.0)
                || Objects.equals(value, 0.0f);
    }
}
