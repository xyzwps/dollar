package com.xyzwps.dollar.collector;

import java.util.Objects;
import java.util.function.BiFunction;

public class ReduceCollector<T, R> implements Collector<T, R> {

    private R result;

    private final BiFunction<R, T, R> reduceFn;

    public ReduceCollector(R init, BiFunction<R, T, R> reduceFn) {
        this.result = init;
        this.reduceFn = Objects.requireNonNull(reduceFn);
    }

    @Override
    public void onRequest(T t) {
        this.result = reduceFn.apply(result, t);
    }

    @Override
    public R result() {
        return result;
    }
}
