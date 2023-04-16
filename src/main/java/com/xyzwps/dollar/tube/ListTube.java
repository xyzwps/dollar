package com.xyzwps.dollar.tube;

import com.xyzwps.dollar.Direction;
import com.xyzwps.dollar.collector.FirstCollector;
import com.xyzwps.dollar.collector.ForEachCollector;
import com.xyzwps.dollar.collector.ListCollector;
import com.xyzwps.dollar.collector.ReduceCollector;
import com.xyzwps.dollar.operator.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class ListTube<T> implements Tube<T> {

    /**
     * To split a list into groups the length of size. If list can't be split evenly,
     * the final chunk will be the remaining elements.
     * <p>
     * 把一个列表切分成具有指定长度的子列表的列表。如果不能均分，最后一组则包含剩余元素。
     *
     * @param size The length of each chunk, should be greater than 0.
     *             <p>
     *             每个 chunk 的长度，应大于 0。
     */
    public ListTube<List<T>> chunk(int size) {
        return new ListTubeStage<>(new ChunkOperator<>(size), this);
    }

    /**
     *
     * @return
     */
    public ListTube<T> compact() {
        return this.filter(Objects::nonNull);
    }

    public ListTube<T> filter(Predicate<T> predicateFn) {
        return new ListTubeStage<>(new FilterOperator<>(Objects.requireNonNull(predicateFn)), this);
    }

    public Optional<T> first() {
        return this.collect(new FirstCollector<>());
    }

    public <R> ListTube<R> flatMap(Function<T, Tube<R>> flatMapFn) {
        return new ListTubeStage<>(new FlatMapOperator<>(Objects.requireNonNull(flatMapFn)), this);
    }

    public <R> ListTube<R> flatten(Function<T, List<R>> flattenFn) {
        return new ListTubeStage<>(new FlatMapOperator<>(it -> new ListTubeFromList<>(flattenFn.apply(it))), this);
    }

    public int forEach(Consumer<T> handler) {
        return this.collect(new ForEachCollector<>(handler));
    }

    public <K> MapTube<K, List<T>> groupBy(Function<T, K> toKey) {
        return new MapTubeForGroupBy<>(this.map(it -> Pair.of(toKey.apply(it), it)));
    }

    public <K> MapTube<K, T> keyBy(Function<T, K> toKey) {
        return new MapTubeForKeyBy<>(this.map(it -> Pair.of(toKey.apply(it), it)));
    }

    public <R> ListTube<R> map(Function<T, R> mapFn) {
        return new ListTubeStage<>(new MapOperator<>(Objects.requireNonNull(mapFn)), this);
    }

    public <K extends Comparable<K>> ListTube<T> orderBy(Function<T, K> toKey, Direction direction) {
        return new ListTubeStage<>(new OrderByOperator<>(Objects.requireNonNull(toKey), Objects.requireNonNull(direction)), this);
    }

    public <R> R reduce(R identity, BiFunction<R, T, R> accelerator) {
        return this.collect(new ReduceCollector<>(identity, Objects.requireNonNull(accelerator)));
    }

    public ListTube<T> reverse() {
        return new ListTubeStage<>(new ReverseOperator<>(), this);
    }

    public ListTube<T> take(int n) {
        return new ListTubeStage<>(new TakeOperator<>(n), this);
    }

    public ListTube<T> takeWhile(Predicate<T> predicate) {
        return new ListTubeStage<>(new TakeWhileOperator<>(predicate), this);
    }

    public ListTube<T> unique() {
        return new ListTubeStage<>(new UniqueOperator<>(), this);
    }

    public <K> ListTube<T> uniqueBy(Function<T, K> toKey) {
        return new ListTubeStage<>(new UniqueByOperator<>(toKey), this);
    }

    public List<T> value() {
        return this.collect(new ListCollector<>());
    }

    public abstract Capsule<T> next();
}
