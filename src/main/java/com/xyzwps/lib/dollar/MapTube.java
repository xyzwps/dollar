package com.xyzwps.lib.dollar;

import com.xyzwps.lib.dollar.collector.MapCollector;
import com.xyzwps.lib.dollar.operator.FilterOperator;
import com.xyzwps.lib.dollar.operator.MapOperator;
import com.xyzwps.lib.dollar.operator.UniqueByOperator;
import com.xyzwps.lib.dollar.tube.ListTubeForMapEntries;
import com.xyzwps.lib.dollar.tube.MapTubeStage;

import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * TODO: add docs
 */
public abstract class MapTube<K, V> implements Tube<Pair<K, V>> {

    public <V2> MapTube<K, V2> mapValues(Function<V, V2> mapValueFn) {
        return new MapTubeStage<>(new MapOperator<>(p -> Pair.of(p.key(), mapValueFn.apply(p.value()))), this);
    }

    public <K2> MapTube<K2, V> mapKeys(Function<K, K2> mapKeyFn) {
        MapTube<K2, V> mapKeysStage = new MapTubeStage<>(new MapOperator<>(p -> Pair.of(mapKeyFn.apply(p.key()), p.value())), this);
        return new MapTubeStage<>(new UniqueByOperator<>(Pair::key), mapKeysStage);
    }

    public MapTube<K, V> filter(BiPredicate<K, V> predicateFn) {
        return new MapTubeStage<>(new FilterOperator<>(p -> predicateFn.test(p.key(), p.value())), this);
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