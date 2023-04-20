package com.xyzwps.lib.dollar.tube;

import com.xyzwps.lib.dollar.collector.Collector;

/**
 * Element processing tube.
 *
 * @param <T> element type
 */
public interface Tube<T> {

    /**
     * Get next element from tube.
     *
     * @return element capsule.
     */
    Capsule<T> next();

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

            Capsule<T> c = this.next();
            if (c instanceof Capsule.Done) {
                return collector.result();
            } else if (c instanceof Capsule.Carrier) {
                T v = ((Capsule.Carrier<T>) c).value();
                collector.onRequest(v);
            } else {
                throw new Capsule.UnknownCapsuleException();
            }
        }
    }
}