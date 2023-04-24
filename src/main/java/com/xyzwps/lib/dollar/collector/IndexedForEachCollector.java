package com.xyzwps.lib.dollar.collector;

import java.util.Objects;
import java.util.function.ObjIntConsumer;

public class IndexedForEachCollector<T> implements Collector<T, Integer> {

    private int index = 0;
    private final ObjIntConsumer<T> handler;

    public IndexedForEachCollector(ObjIntConsumer<T> handler) {
        this.handler = Objects.requireNonNull(handler);
    }

    @Override
    public void onRequest(T t) {
        this.handler.accept(t, this.index++);
    }

    @Override
    public Integer result() {
        return this.index;
    }
}
