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
 * {@link MapStage} 表示连续处理 {@link Map#entrySet()} 到了某个阶段。
 * <p>
 * 注意：{@link MapStage} 本身就是 {@link Iterable}，所以，你可以直接对其使用 for-each 循环语句来遍历所有的 entry。
 * <p>
 * 注意：{@link Map.Entry} 是可以被修改的。为避免处理过程中出现意外情况，本类用用 {@link Pair} 表示 entry。
 *
 * @param <K> key 的类型
 * @param <V> value 的类型
 */
public class MapStage<K, V> implements Iterable<Pair<K, V>> {

    private final Iterable<Pair<K, V>> entryIterable;

    /**
     * 连续处理的开始阶段。
     *
     * @param map 要处理的 {@link Map}
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
     * 把 entry 的 value 映射为一个新值来作为下一阶段 entry 的 value。
     * <p>
     * 例:
     * <pre>
     * $($.mapOf(0, "", 1, "1", 2, "11", 3, "111"))
     *   .mapValues(String::length)
     *   .value()
     * => {0=0, 1=1, 2=2, 3=3}
     * </pre>
     *
     * @param mapValueFn 映射函数
     * @param <V2>       新 value 的类型
     * @return 已经经过的阶段
     */
    public <V2> MapStage<K, V2> mapValues(Function<V, V2> mapValueFn) {
        Objects.requireNonNull(mapValueFn);
        ObjIntFunction<Pair<K, V>, Pair<K, V2>> fn = (pair, index) -> Pair.of(pair.key(), mapValueFn.apply(pair.value()));
        return new MapStage<>(this, up -> new MapIterator<>(up, fn));
    }

    /**
     * 把 entry 的 value 和 key 映射为一个新值来作为下一阶段 entry 的 value。
     * <p>
     * 例:
     * <pre>
     * $($.mapOf(0, "", 1, "1", 2, "11", 3, "111"))
     *   .mapValues((value, key) -> String.format("%d:%s", key, value))
     *   .value()
     * => {0=0:, 1=1:1, 2=2:11, 3=3:111}
     * </pre>
     *
     * @param mapValueFn 映射函数
     * @param <V2>       新 value 的类型
     * @return 已经经过的阶段
     */
    public <V2> MapStage<K, V2> mapValues(BiFunction<V, K, V2> mapValueFn) {
        Objects.requireNonNull(mapValueFn);
        ObjIntFunction<Pair<K, V>, Pair<K, V2>> fn = (pair, index) -> Pair.of(pair.key(), mapValueFn.apply(pair.value(), pair.key()));
        return new MapStage<>(this, up -> new MapIterator<>(up, fn));
    }

    /**
     * 把 entry 的 key 映射为一个新值来作为下一阶段 entry 的 key。
     * 如果 key 出现重复，则保留先出现的 entry，丢弃后来的。
     * <p>
     * 例：
     * <pre>
     * $($.mapOf(1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6))
     *   .mapKeys(i -> i % 3)
     *   .value();
     * => {1=1, 2=2, 0=3}
     * </pre>
     *
     * @param mapKeyFn 映射函数
     * @param <K2>     新 key 的类型
     * @return 已经经过的阶段
     */
    public <K2> MapStage<K2, V> mapKeys(Function<K, K2> mapKeyFn) {
        Objects.requireNonNull(mapKeyFn);
        ObjIntFunction<Pair<K, V>, Pair<K2, V>> fn0 = (pair, index) -> Pair.of(mapKeyFn.apply(pair.key()), pair.value());
        MapStage<K2, V> stage0 = new MapStage<>(this, up -> new MapIterator<>(up, fn0));
        return new MapStage<>(stage0, up -> new UniqueByIterator<>(up, Pair::key));
    }

    /**
     * 把 entry 的 key 和 value 映射为一个新值来作为下一阶段 entry 的 key。
     * 如果 key 出现重复，则保留先出现的 entry，丢弃后来的。
     * <p>
     * 例：
     * <pre>
     * $($.mapOf(1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6))
     *   .mapKeys((key, value) -> (key + value) % 5)
     *   .value();
     * => {2=1, 4=2, 1=3, 3=4, 0=5}
     * </pre>
     *
     * @param mapKeyFn 映射函数
     * @param <K2>     新 key 的类型
     * @return 已经经过的阶段
     */
    public <K2> MapStage<K2, V> mapKeys(BiFunction<K, V, K2> mapKeyFn) {
        Objects.requireNonNull(mapKeyFn);
        ObjIntFunction<Pair<K, V>, Pair<K2, V>> fn0 = (pair, index) -> Pair.of(mapKeyFn.apply(pair.key(), pair.value()), pair.value());
        MapStage<K2, V> stage0 = new MapStage<>(this, up -> new MapIterator<>(up, fn0));
        return new MapStage<>(stage0, up -> new UniqueByIterator<>(up, Pair::key));
    }


    /**
     * 筛选满足条件的 entry.
     * <p>
     * 例：
     * <pre>
     * $($.mapOf(1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6))
     *   .filter((key, value) -> value % 2 == 0)
     *   .value();
     * => {2=2, 4=4, 6=6}
     * </pre>
     *
     * @param predicateFn entry 应满足的条件
     * @return 已经经过的阶段
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
     * 把全部 entry 装入 {@link Map}。
     *
     * @return 包含全部 entry 的新 Map
     */
    public Map<K, V> value() {
        Map<K, V> result = new HashMap<>();
        this.forEach(p -> result.put(p.key(), p.value()));
        return result;
    }

    /**
     * 遍历全部的 entry。
     *
     * @param action 处理每个 entry 的动作
     */
    public void forEach(BiConsumer<K, V> action) {
        Objects.requireNonNull(action);
        this.forEach(pair -> action.accept(pair.key(), pair.value()));
    }

    /**
     * 获取每个 entry 的 value。
     *
     * @return 已经经过的阶段
     */
    public ListStage<V> values() {
        return new ListStage<>(this, up -> new MapIterator<>(up, (p, i) -> p.value()));
    }

    /**
     * 获取每个 entry 的 key。
     *
     * @return 已经经过的阶段
     */
    public ListStage<K> keys() {
        return new ListStage<>(this, up -> new MapIterator<>(up, (p, i) -> p.key()));
    }


    /**
     * 遍历所有 entry。
     *
     * @return 一个新的可遍历所有 entry 的 {@link Iterator}
     */
    @Override
    public Iterator<Pair<K, V>> iterator() {
        return this.entryIterable.iterator();
    }
}
