package com.xyzwps.dollar.collector;

import com.xyzwps.dollar.tube.Pair;

import java.util.HashMap;
import java.util.Map;

public class MapCollector<K, V> implements Collector<Pair<K, V>, Map<K, V>> {

    private final Map<K, V> result = new HashMap<>();

    @Override
    public void onRequest(Pair<K, V> pair) {
        result.put(pair.key(), pair.value());
    }

    @Override
    public Map<K, V> result() {
        return result;
    }
}
