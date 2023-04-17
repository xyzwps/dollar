package com.xyzwps.dollar.tube;

import com.xyzwps.dollar.collector.Collector;

public interface Tube<T> {

    Capsule<T> next();

    default <R> R collect(Collector<T, R> collector) {
        while (true) {
            if (!collector.needMore()) {
                return collector.result();
            }

            Capsule<T> c = this.next();
            if (c instanceof Capsule.Done) {
                return collector.result();
            } else if (c instanceof Capsule.Failure) {
                throw new RuntimeException(((Capsule.Failure<T>) c).cause()); // TODO: 定义错误;
            } else if (c instanceof Capsule.Carrier) {
                T v = ((Capsule.Carrier<T>) c).value();
                collector.onRequest(v);
            } else {
                throw new Capsule.UnknownCapsuleException();
            }
        }
    }
}