package com.xyzwps.dollar.tube;

import com.xyzwps.dollar.collector.MapCollector;
import com.xyzwps.dollar.operator.MapOperator;
import com.xyzwps.dollar.operator.UniqueByOperator;

import java.util.Map;
import java.util.function.Function;

public abstract class MapTube<K, V> implements Tube<Pair<K, V>> {

    public <V2> MapTube<K, V2> mapValues(Function<V, V2> mapValueFn) {
        return new MapTubeStage<>(new MapOperator<>(p -> Pair.of(p.key(), mapValueFn.apply(p.value()))), this);
    }

    public <K2> MapTube<K2, V> mapKeys(Function<K, K2> mapKeyFn) {
        MapTube<K2, V> mapKeysStage = new MapTubeStage<>(new MapOperator<>(p -> Pair.of(mapKeyFn.apply(p.key()), p.value())), this);
        return new MapTubeStage<>(new UniqueByOperator<>(Pair::key), mapKeysStage);
    }

    public Map<K, V> value() {
        return this.collect(new MapCollector<>());
    }

    public ListTube<V> values() {
        return new ListTubeForMapEntries<>(this, (k, v) -> v);
    }

    public ListTube<K> keys() {
        return new ListTubeForMapEntries<>(this, (k, v) -> k);
    }
}