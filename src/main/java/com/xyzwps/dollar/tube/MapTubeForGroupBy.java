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

            Capsule<Pair<K, V>> c = upstream.next();
            if (c instanceof Capsule.Done) {
                this.itr = new MapEntryIterator<>(map);
            } else if (c instanceof Capsule.Failure) {
                return Capsule.failed(((Capsule.Failure<Pair<K, V>>) c).cause());
            } else if (c instanceof Capsule.Carrier) {
                Pair<K, V> pair = ((Capsule.Carrier<Pair<K, V>>) c).value();
                List<V> list = map.computeIfAbsent(pair.key(), k -> new ArrayList<>());
                list.add(pair.value());
            } else {
                throw new Capsule.UnknownCapsuleException();
            }
        }

        return this.itr.hasNext() ? Capsule.carry(this.itr.next()) : Capsule.done();
    }
}