package com.xyzwps.lib.dollar;

import com.xyzwps.lib.dollar.function.Function3;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

import static com.xyzwps.lib.dollar.Helper.*;

public interface MESeq<K, V> {

    void forEach(BiConsumer<K, V> consumer);

    default <V2> MESeq<K, V2> mapValues(Function<V, V2> mapValueFn) {
        Objects.requireNonNull(mapValueFn);
        return kv2Consumer -> this.forEach((k, v) -> kv2Consumer.accept(k, mapValueFn.apply(v)));
    }

    default <V2> MESeq<K, V2> mapValues(BiFunction<V, K, V2> mapValueFn) {
        Objects.requireNonNull(mapValueFn);
        return kv2Consumer -> this.forEach((k, v) -> kv2Consumer.accept(k, mapValueFn.apply(v, k)));
    }

    default <K2> MESeq<K2, V> mapKeys(Function<K, K2> mapKeyFn) {
        Objects.requireNonNull(mapKeyFn);
        final HashSet<K2> dedupSet = new HashSet<>();
        return k2vConsumer -> this.forEach((k, v) -> {
            K2 k2 = mapKeyFn.apply(k);
            if (!dedupSet.contains(k2)) {
                dedupSet.add(k2);
                k2vConsumer.accept(k2, v);
            }
        });
    }

    default <K2> MESeq<K2, V> mapKeys(BiFunction<K, V, K2> mapKeyFn) {
        Objects.requireNonNull(mapKeyFn);
        final HashSet<K2> dedupSet = new HashSet<>();
        return k2vConsumer -> this.forEach((k, v) -> {
            K2 k2 = mapKeyFn.apply(k, v);
            if (!dedupSet.contains(k2)) {
                dedupSet.add(k2);
                k2vConsumer.accept(k2, v);
            }
        });
    }

    default MESeq<K, V> filter(BiPredicate<K, V> predicateFn) {
        Objects.requireNonNull(predicateFn);
        return kvConsumer -> this.forEach((k, v) -> {
            if (predicateFn.test(k, v)) {
                kvConsumer.accept(k, v);
            }
        });
    }

    default <R> R reduce(R initValue, Function3<R, K, V, R> callbackFn) {
        Objects.requireNonNull(callbackFn);
        Holder<R> rHolder = new Holder<>(initValue);
        this.forEach((k, v) -> rHolder.value = callbackFn.apply(rHolder.value, k, v));
        return rHolder.value;
    }

    default HashMap<K, V> value() {
        HashMap<K, V> result = new HashMap<>();
        this.forEach(result::put);
        return result;
    }

    default Seq<V> values() {
        return vConsumer -> this.forEach((k, v) -> vConsumer.accept(v));
    }

    default Seq<K> keys() {
        return vConsumer -> this.forEach((k, v) -> vConsumer.accept(k));
    }

    static <K, V> MESeq<K, V> empty() {
        return kvConsumer -> {
        };
    }

}
