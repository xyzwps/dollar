package com.xyzwps.dollar.tube;

import com.xyzwps.dollar.iterator.MapEntryIterator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapTubeForGroupBy<K, V> extends MapTube<K, List<V>> {

    private final ListTube<Pair<K, V>> upstream;
    private final Map<K, List<V>> map = new HashMap<>();
    private MapEntryIterator<K, List<V>> itr;

    public MapTubeForGroupBy(ListTube<Pair<K, V>> upstream) {
        this.upstream = upstream;
    }

    @Override
    public Capsule<Pair<K, List<V>>> next() {
        while (this.itr == null) {
            switch (upstream.next()) {
                case Capsule.Done<Pair<K, V>> ignored -> this.itr = new MapEntryIterator<>(map);
                case Capsule.Failure<Pair<K, V>> failure -> {
                    return Capsule.failed(failure.cause());
                }
                case Capsule.Carrier<Pair<K, V>> carrier -> {
                    var pair = carrier.value();
                    var list = map.computeIfAbsent(pair.key(), k -> new ArrayList<>());
                    list.add(pair.value());
                }
            }
        }

        return this.itr.hasNext() ? Capsule.carry(this.itr.next()) : Capsule.done();
    }
}