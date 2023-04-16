package com.xyzwps.dollar.collector;

import java.util.ArrayList;
import java.util.List;

public class ListCollector<T> implements Collector<T, List<T>> {

    private final List<T> list = new ArrayList<>();

    @Override
    public void onRequest(T t) {
        this.list.add(t);
    }

    @Override
    public List<T> result() {
        return list;
    }
}
