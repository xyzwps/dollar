package com.xyzwps.lib.dollar.collector;

/**
 * Collect elements from tube into a result.
 *
 * @param <T> type of elements from tube
 * @param <R> type of collecting result
 */
public interface Collector<T, R> {

    /**
     * Check if collecting more elements or not. It's useful
     * for method <code>first</code> or <code>take</code>.
     *
     * @return false if no more elements are needed.
     */
    default boolean needMore() {
        return true;
    }

    /**
     * Receive a element from tube.
     *
     * @param t the received element
     */
    void onRequest(T t);

    /**
     * Get the result of collector.
     *
     * @return collecting result
     */
    R result();
}
