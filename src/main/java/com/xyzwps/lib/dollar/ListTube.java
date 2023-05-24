package com.xyzwps.lib.dollar;

import com.xyzwps.lib.dollar.collector.*;
import com.xyzwps.lib.dollar.function.IndexedFunction;
import com.xyzwps.lib.dollar.function.IndexedPredicate;
import com.xyzwps.lib.dollar.operator.*;
import com.xyzwps.lib.dollar.tube.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.*;

import static com.xyzwps.lib.dollar.Dollar.*;

/**
 * List elements tube.
 *
 * @param <T> Element type
 */
public abstract class ListTube<T> implements Tube<T> {


    public ListTube<List<T>> chunk(int size) {
        return new ListTubeStage<>(new ChunkOperator<>(size), this);
    }

    public ListTube<T> compact() {
        return this.filter(it -> !$.isFalsey(it));
    }

    public ListTube<T> concat(Iterable<T> iterable) {
        return new ListTubeStage<>(new ConcatOperator<>(iterable), this);
    }

    public ListTube<T> filter(Predicate<T> predicateFn) {
        Objects.requireNonNull(predicateFn);
        return new ListTubeStage<>(new FilterOperator<>(predicateFn), this);
    }

    public ListTube<T> filter(IndexedPredicate<T> predicateFn) {
        Objects.requireNonNull(predicateFn);
        return new ListTubeStage<>(new IndexedFilterOperator<>(predicateFn), this);
    }

    public Optional<T> first() {
        return this.collect(new FirstCollector<>());
    }


    public <R> ListTube<R> flatMap(Function<T, Tube<R>> flatMapFn) {
        return new ListTubeStage<>(new FlatMapOperator<>(Objects.requireNonNull(flatMapFn)), this);
    }

    public <R> ListTube<R> flatten(Function<T, List<R>> flattenFn) {
        Function<T, List<R>> nonNullFlattenFn = (it) -> $.defaultTo(flattenFn.apply(it), $.arrayList());
        return new ListTubeStage<>(new FlatMapOperator<>(it -> new ListTubeFromIterator<>(nonNullFlattenFn.apply(it).iterator())), this);
    }


    public int forEach(Consumer<T> handler) {
        Objects.requireNonNull(handler);
        return this.forEach((it, index) -> handler.accept(it));
    }


    public int forEach(ObjIntConsumer<T> handler) {
        return this.collect(new IndexedForEachCollector<>(handler));
    }


    public <K> MapTube<K, List<T>> groupBy(Function<T, K> toKey) {
        return new MapTubeForGroupBy<>(this.map(it -> Pair.of(toKey.apply(it), it)));
    }


    public Optional<T> head() {
        return this.first();
    }


    public String join(String sep) {
        return this.collect(new JoinCollector<>(sep));
    }


    /**
     * Aggregate all elements into a map with a specified key.
     * If two elements produce the same key, the first consumed element will be selected.
     * <p>
     * Examples:
     * <pre>
     * $.just(1, 2, 3, 4, 5).keyBy(i -> i % 2 == 0 ? "even" : "odd").value() => { "odd": 1, "even": 2 }
     * </pre>
     *
     * @param toKey to calculate element key
     * @param <K>   element key type
     * @return next tube
     */
    public <K> MapTube<K, T> keyBy(Function<T, K> toKey) {
        return new MapTubeForKeyBy<>(this.map(it -> Pair.of(toKey.apply(it), it)));
    }


    public <R> ListTube<R> map(Function<T, R> mapFn) {
        Objects.requireNonNull(mapFn);
        return this.map((it, index) -> mapFn.apply(it));
    }

    public <R> ListTube<R> map(IndexedFunction<T, R> mapFn) {
        return new ListTubeStage<>(new IndexedMapOperator<>(mapFn), this);
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

    public <R> ListTube<Pair<T, R>> zip(List<R> list) {
        return this.zip(list, Pair::of);
    }

    public <R, S> ListTube<S> zip(List<R> list, BiFunction<T, R, S> combineFn) {
        if ($.isEmpty(list)) {
            return this.map(it -> combineFn.apply(it, null));
        } else {
            return new ListTubeStage<>(new ZipOperator<>(list.iterator(), combineFn), this);
        }
    }


    /**
     * A api not for user. FIXME: hide it
     *
     * @return next capsule
     */
    public abstract T next() throws EndException;
}
