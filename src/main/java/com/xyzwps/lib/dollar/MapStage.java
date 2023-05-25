package com.xyzwps.lib.dollar;

import com.xyzwps.lib.dollar.function.ObjIntFunction;
import com.xyzwps.lib.dollar.iterable.ChainIterable;
import com.xyzwps.lib.dollar.iterable.MapEntryIterable;
import com.xyzwps.lib.dollar.iterator.FilterIterator;
import com.xyzwps.lib.dollar.iterator.MapIterator;
import com.xyzwps.lib.dollar.iterator.UniqueByIterator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class MapStage<K, V> implements Iterable<Pair<K, V>> {

    private final Iterable<Pair<K, V>> entryIterable;

    public MapStage(Map<K, V> map) {
        this(new MapEntryIterable<>(map));
    }

    MapStage(Iterable<Pair<K, V>> entryIterable) {
        this.entryIterable = Objects.requireNonNull(entryIterable);
    }

    <K0, V0> MapStage(Iterable<Pair<K0, V0>> up, Function<Iterator<Pair<K0, V0>>, Iterator<Pair<K, V>>> chainFn) {
        this(ChainIterable.create(up, chainFn));
    }

    public <V2> MapStage<K, V2> mapValues(Function<V, V2> mapValueFn) {
        Objects.requireNonNull(mapValueFn);
        ObjIntFunction<Pair<K, V>, Pair<K, V2>> fn = (pair, index) -> Pair.of(pair.key(), mapValueFn.apply(pair.value()));
        return new MapStage<>(this.entryIterable, up -> new MapIterator<>(up, fn)); // TODO: 这里不用 this.entryIterable，直接 this 就可以了。。。其他地方也是
    }

    // TODO: mapValues2

    public <K2> MapStage<K2, V> mapKeys(Function<K, K2> mapKeyFn) {
        Objects.requireNonNull(mapKeyFn);
        ObjIntFunction<Pair<K, V>, Pair<K2, V>> fn0 = (pair, index) -> Pair.of(mapKeyFn.apply(pair.key()), pair.value());
        MapStage<K2, V> stage0 = new MapStage<>(this.entryIterable, up -> new MapIterator<>(up, fn0));
        return new MapStage<>(stage0.entryIterable, up -> new UniqueByIterator<>(up, Pair::key));
    }

    // TODO: mapKeys2

    public MapStage<K, V> filter(BiPredicate<K, V> predicateFn) {
        Objects.requireNonNull(predicateFn);
        return new MapStage<>(this.entryIterable, up -> new FilterIterator<>(up, (p, i) -> predicateFn.test(p.key(), p.value())));
    }

    public HashMap<K, V> value() {
        HashMap<K, V> result = new HashMap<>();
        this.forEach(p -> result.put(p.key(), p.value()));
        return result;
    }

    // TODO: forEach

    public ListStage<V> values() {
        return new ListStage<>(this, up -> new MapIterator<>(up, (p, i) -> p.value()));
    }

    public ListStage<K> keys() {
        return new ListStage<>(this, up -> new MapIterator<>(up, (p, i) -> p.key()));
    }

    @Override
    public Iterator<Pair<K, V>> iterator() {
        return this.entryIterable.iterator();
    }
}
