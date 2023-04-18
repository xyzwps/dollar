package com.xyzwps.lib.dollar.tube;

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
    public Capsule<Pair<K, V>> next() {
        while (true) {
            Capsule<Pair<K, V>> c = upstream.next();
            if (c instanceof Capsule.Done) {
                return c;
            } else if (c instanceof Capsule.Carrier) {
                Pair<K, V> pair = ((Capsule.Carrier<Pair<K, V>>) c).value();
                if (!deduplicatedKeySet.contains(pair.key())) {
                    deduplicatedKeySet.add(pair.key());
                    return c;
                }
            } else {
                throw new Capsule.UnknownCapsuleException();
            }
        }
    }
}