package com.xyzwps.lib.dollar;

import com.xyzwps.lib.dollar.function.Function3;
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
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * Map handling stage.
 *
 * @param <K> type of keys
 * @param <V> type of values
 */
public class MapStage<K, V> implements Iterable<Pair<K, V>> {

    private final Iterable<Pair<K, V>> entryIterable;

    /**
     * Create a stage from {@link Map}.
     *
     * @param map source
     */
    public MapStage(Map<K, V> map) {
        this(new MapEntryIterable<>(map));
    }

    MapStage(Iterable<Pair<K, V>> entryIterable) {
        this.entryIterable = Objects.requireNonNull(entryIterable);
    }

    <K0, V0> MapStage(Iterable<Pair<K0, V0>> up, Function<Iterator<Pair<K0, V0>>, Iterator<Pair<K, V>>> chainFn) {
        this(ChainIterable.create(up, chainFn));
    }

    /**
     * Map values to another type of values from just origin values.
     *
     * @param mapValueFn value mapping function
     * @param <V2>       type of mapped values
     * @return chained stage
     */
    public <V2> MapStage<K, V2> mapValues(Function<V, V2> mapValueFn) {
        Objects.requireNonNull(mapValueFn);
        ObjIntFunction<Pair<K, V>, Pair<K, V2>> fn = (pair, index) -> Pair.of(pair.key(), mapValueFn.apply(pair.value()));
        return new MapStage<>(this, up -> new MapIterator<>(up, fn));
    }

    /**
     * Map values to another type of values from origin keys and values.
     *
     * @param mapValueFn value mapping function
     * @param <V2>       type of mapped values
     * @return chained stage
     */
    public <V2> MapStage<K, V2> mapValues(BiFunction<V, K, V2> mapValueFn) {
        Objects.requireNonNull(mapValueFn);
        ObjIntFunction<Pair<K, V>, Pair<K, V2>> fn = (pair, index) -> Pair.of(pair.key(), mapValueFn.apply(pair.value(), pair.key()));
        return new MapStage<>(this, up -> new MapIterator<>(up, fn));
    }

    /**
     * Map keys to another type of keys from just origin keys.
     * The corresponding entries of duplicated keys would be discarded.
     *
     * @param mapKeyFn key mapping function
     * @param <K2>     type of mapped keys
     * @return chained stage
     */
    public <K2> MapStage<K2, V> mapKeys(Function<K, K2> mapKeyFn) {
        Objects.requireNonNull(mapKeyFn);
        ObjIntFunction<Pair<K, V>, Pair<K2, V>> fn0 = (pair, index) -> Pair.of(mapKeyFn.apply(pair.key()), pair.value());
        MapStage<K2, V> stage0 = new MapStage<>(this, up -> new MapIterator<>(up, fn0));
        return new MapStage<>(stage0, up -> new UniqueByIterator<>(up, Pair::key));
    }

    /**
     * Map keys to another type of keys from origin keys and values.
     * The corresponding entries of duplicated keys would be discarded.
     *
     * @param mapKeyFn key mapping function
     * @param <K2>     type of mapped keys
     * @return chained stage
     */
    public <K2> MapStage<K2, V> mapKeys(BiFunction<K, V, K2> mapKeyFn) {
        Objects.requireNonNull(mapKeyFn);
        ObjIntFunction<Pair<K, V>, Pair<K2, V>> fn0 = (pair, index) -> Pair.of(mapKeyFn.apply(pair.key(), pair.value()), pair.value());
        MapStage<K2, V> stage0 = new MapStage<>(this, up -> new MapIterator<>(up, fn0));
        return new MapStage<>(stage0, up -> new UniqueByIterator<>(up, Pair::key));
    }


    /**
     * The entries would be retained if they were predicated to true.
     * Otherwise, the entries would be discarded.
     *
     * @param predicateFn predicate function
     * @return chained stage
     */
    public MapStage<K, V> filter(BiPredicate<K, V> predicateFn) {
        Objects.requireNonNull(predicateFn);
        return new MapStage<>(this, up -> new FilterIterator<>(up, (p, i) -> predicateFn.test(p.key(), p.value())));
    }

    public <R> R reduce(R initValue, Function3<R, K, V, R> callbackFn) {
        Objects.requireNonNull(callbackFn);
        R result = initValue;
        for (Pair<K, V> p : this) {
            result = callbackFn.apply(result, p.left(), p.right());
        }
        return result;
    }

    /**
     * Collect entries into a {@link Map}.
     *
     * @return new map containing all collected entries
     */
    public Map<K, V> value() {
        Map<K, V> result = new HashMap<>();
        this.forEach(p -> result.put(p.key(), p.value()));
        return result;
    }

    /**
     * Iterate over all entries, and handling them by a specific action.
     *
     * @param action is a handler to handle each entry
     */
    public void forEach(BiConsumer<K, V> action) {
        Objects.requireNonNull(action);
        this.forEach(pair -> action.accept(pair.key(), pair.value()));
    }

    /**
     * Just chaining values to another stage.
     *
     * @return chained stage
     */
    public ListStage<V> values() {
        return new ListStage<>(this, up -> new MapIterator<>(up, (p, i) -> p.value()));
    }

    /**
     * Just chaining keys to another stage.
     *
     * @return chained stage
     */
    public ListStage<K> keys() {
        return new ListStage<>(this, up -> new MapIterator<>(up, (p, i) -> p.key()));
    }


    /**
     * The stage is an {@link Iterable}.
     *
     * @return an {@link Iterator} to iterate over the entries
     */
    @Override
    public Iterator<Pair<K, V>> iterator() {
        return this.entryIterable.iterator();
    }
}
