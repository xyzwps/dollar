package com.xyzwps.lib.dollar.tube;

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
    public Capsule<D> next() {
        return Capsule.map(upstream.next(),
                carrier -> {
                    Pair<K, V> p = carrier.value();
                    return Capsule.carry(this.toValue.apply(p.key(), p.value()));
                });
    }
}
