package com.xyzwps.lib.dollar.collector;

/**
 * Collect elements into a result.
 *
 * @param <T> type of elements to collect
 * @param <R> type of collecting result
 *
 * TODO: 好像不太需要了
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
     * Receive an element.
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
