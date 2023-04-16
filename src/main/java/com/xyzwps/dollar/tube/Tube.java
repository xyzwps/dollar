package com.xyzwps.dollar.tube;

import com.xyzwps.dollar.collector.Collector;

public interface Tube<T> {

    Capsule<T> next();

    default <R> R collect(Collector<T, R> collector) {
        while (true) {
            if (!collector.needMore()) {
                return collector.result();
            }

            switch (this.next()) {
                case Capsule.Done<T> ignored -> {
                    return collector.result();
                }
                case Capsule.Failure<T> failure -> throw new RuntimeException(failure.cause()); // TODO: 定义错误
                case Capsule.Carrier<T> carrier -> collector.onRequest(carrier.value());
            }
        }
    }


}