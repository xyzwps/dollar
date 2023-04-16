package com.xyzwps.dollar.collector;

import java.util.Optional;

public class FirstCollector<T> implements Collector<T, Optional<T>> {

    private T t;

    private boolean needMore = true;

    @Override
    public void onRequest(T t) {
        this.needMore = false;
        this.t = t;
    }

    @Override
    public Optional<T> result() {
        return Optional.ofNullable(t);
    }
}
