package com.xyzwps.lib.dollar.tube;

import com.xyzwps.lib.dollar.Pair;
import com.xyzwps.lib.dollar.collector.ReduceCollector;
import com.xyzwps.lib.dollar.iterator.MapEntryIterator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapTubeForGroupBy<K, V> extends MapTube<K, List<V>> {

    private final ListTube<Pair<K, V>> upstream;
    private MapEntryIterator<K, List<V>> itr;

    public MapTubeForGroupBy(ListTube<Pair<K, V>> upstream) {
        this.upstream = upstream;
    }

    @Override
    public Pair<K, List<V>> next() throws EndException {
        if (this.itr == null) {
            this.initItr();
        }

        if (this.itr.hasNext()) {
            return this.itr.next();
        } else {
            throw new EndException();
        }
    }

    private void initItr() {
        this.itr = new MapEntryIterator<>(upstream
                .collect(new ReduceCollector<Pair<K, V>, Map<K, List<V>>>(
                        new HashMap<>(),
                        (map, pair) -> {
                            List<V> list = map.computeIfAbsent(pair.key(), k -> new ArrayList<>());
                            list.add(pair.value());
                            return map;
                        }
                )));
    }
}