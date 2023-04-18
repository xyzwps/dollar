package com.xyzwps.lib.dollar.tube;

import com.xyzwps.lib.dollar.iterator.MapEntryIterator;

import java.util.HashMap;
import java.util.Map;

public class MapTubeFromMap<K, V> extends MapTube<K, V> {

    private final MapEntryIterator<K, V> itr;

    public MapTubeFromMap(Map<K, V> map) {
        this.itr = new MapEntryIterator<>(map == null ? new HashMap<>() : map);
    }

    @Override
    public Capsule<Pair<K, V>> next() {
        if (itr.hasNext()) {
            return Capsule.carry(itr.next());
        } else {
            return Capsule.done();
        }
    }
}
