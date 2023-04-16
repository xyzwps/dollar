package com.xyzwps.dollar.collector;

import java.util.Objects;
import java.util.function.Consumer;

public class ForEachCollector<T> implements Collector<T, Integer> {

    private int count = 0;
    private final Consumer<T> handler;

    public ForEachCollector(Consumer<T> handler) {
        this.handler = Objects.requireNonNull(handler);
    }

    @Override
    public void onRequest(T t) {
        this.count++;
        this.handler.accept(t);
    }

    @Override
    public Integer result() {
        return this.count;
    }
}
