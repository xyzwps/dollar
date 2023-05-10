package com.xyzwps.lib.dollar.tube;

import com.xyzwps.lib.dollar.Pair;

import java.util.Objects;
import java.util.function.BiFunction;

public class ListTubeForMapEntries<K, V, D> extends ListTube<D> {

    private final MapTube<K, V> upstream;

    private final BiFunction<K, V, D> toValue;

    public ListTubeForMapEntries(MapTube<K, V> upstream, BiFunction<K, V, D> toValue) {
        this.upstream = upstream;
        this.toValue = Objects.requireNonNull(toValue);
    }

    @Override
    public D next() throws EndException {
        Pair<K, V> p = upstream.next();
        return this.toValue.apply(p.key(), p.value());
    }
}
