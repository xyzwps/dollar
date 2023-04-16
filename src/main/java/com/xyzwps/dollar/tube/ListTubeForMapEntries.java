package com.xyzwps.dollar.tube;

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
        return switch (upstream.next()) {
            case Capsule.Done<Pair<K, V>> ignored -> Capsule.done();
            case Capsule.Failure<Pair<K, V>> failure -> Capsule.failed(failure.cause());
            case Capsule.Carrier<Pair<K, V>> carrier -> {
                var p = carrier.value();
                yield Capsule.carry(this.toValue.apply(p.key(), p.value()));
            }
        };
    }
}
