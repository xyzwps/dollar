package com.xyzwps.lib.dollar.tube;

import com.xyzwps.lib.dollar.ListTube;
import com.xyzwps.lib.dollar.MapTube;
import com.xyzwps.lib.dollar.Pair;

import java.util.HashSet;
import java.util.Set;

public class MapTubeForKeyBy<K, V> extends MapTube<K, V> {

    private final ListTube<Pair<K, V>> upstream;
    private final Set<K> deduplicatedKeySet;

    public MapTubeForKeyBy(ListTube<Pair<K, V>> upstream) {
        this.upstream = upstream;
        this.deduplicatedKeySet = new HashSet<>();
    }

    @Override
    public Pair<K, V> next() throws EndException {
        while (true) {
            Pair<K, V> pair = upstream.next();
            if (!deduplicatedKeySet.contains(pair.key())) {
                deduplicatedKeySet.add(pair.key());
                return pair;
            }
        }
    }
}