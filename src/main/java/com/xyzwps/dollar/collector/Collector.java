package com.xyzwps.dollar.collector;

public interface Collector<T, R> {

    default boolean needMore() {
        return true;
    }

    void onRequest(T t);

    R result();
}
