package com.xyzwps.lib.dollar.function;

/**
 * Indexed version of {@link java.util.function.Function Function}.
 *
 * @param <T> the type of the input to the function
 * @param <R> the type of the result of the function
 */
@FunctionalInterface
public interface IndexedFunction<T, R> {

    /**
     * Applies this function to the given argument.
     *
     * @param element the function argument
     * @param index   the index of <code>element</code>
     * @return the function result
     */
    R apply(T element, int index);
}
