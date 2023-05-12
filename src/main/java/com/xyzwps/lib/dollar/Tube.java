package com.xyzwps.lib.dollar;

import com.xyzwps.lib.dollar.collector.Collector;
import com.xyzwps.lib.dollar.tube.EndException;

/**
 * Element processing tube.
 * <p>
 * A tube is a stage of element process pipeline.
 * Tubes are lazy. The computations won't
 * be performed until the {@link #collect(Collector)}
 * method was invoked.
 * Generally, tubes cannot be reusable. This means that
 * once the {@link #collect(Collector)} method was
 * called, the elements were consumed.
 *
 * @param <T> element type
 */
public interface Tube<T> {

    /**
     * Get next element from tube.
     *
     * @return element capsule.
     */
    T next() throws EndException;

    /**
     * Collect elements from tube.
     *
     * @param collector element collector
     * @param <R>       result element type
     * @return the collected
     */
    default <R> R collect(Collector<T, R> collector) {
        while (true) {
            if (!collector.needMore()) {
                return collector.result();
            }

            try {
                T v = this.next();
                collector.onRequest(v);
            } catch (EndException e) {
                return collector.result();
            }
        }
    }
}