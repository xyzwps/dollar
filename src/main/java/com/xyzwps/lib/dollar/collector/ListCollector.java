package com.xyzwps.lib.dollar.collector;

import java.util.ArrayList;
import java.util.List;

public class ListCollector<T> implements Collector<T, ArrayList<T>> {

    private final ArrayList<T> list = new ArrayList<>();

    @Override
    public void onRequest(T t) {
        this.list.add(t);
    }

    @Override
    public ArrayList<T> result() {
        return list;
    }
}
