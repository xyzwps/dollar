package com.xyzwps.lib.dollar.tube;

import com.xyzwps.lib.dollar.Direction;
import com.xyzwps.lib.dollar.Dollar;
import com.xyzwps.lib.dollar.collector.*;
import com.xyzwps.lib.dollar.operator.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * List elements tube.
 *
 * @param <T> Element type
 */
public abstract class ListTube<T> implements Tube<T> {

    /**
     * Examples:
     * <pre>
     * $(1, 2, 3, 4, 5).chunk(2).value() => [[1,2], [3,4], [5]]
     * </pre>
     *
     * @param size The length of each chunk, should be greater than 0.
     * @return next tube
     */
    public ListTube<List<T>> chunk(int size) {
        return new ListTubeStage<>(new ChunkOperator<>(size), this);
    }


    /**
     * Exclude all falsey values. The values <code>null</code>, <code>false</code>,
     * <code>0(.0)</code> and <code>""</code> are falsey.
     * <p>
     * Examples:
     * <pre>
     * $(null, 1, 0, true, false, "a", "").compact().value() => [1, true, ""]
     * </pre>
     *
     * @return next tube
     * @see Dollar.$#isFalsey
     */
    public ListTube<T> compact() {
        return this.filter(it -> !Dollar.$.isFalsey(it));
    }


    /**
     * Iterates over elements, and exclude those which being predicated with <code>false</code>.
     * <p>
     * Examples:
     * <pre>
     * $(1, 2, 3, 4, 5).filter(i -> i % 2 == 1).value() => [1, 3, 5]
     * $(1, 2, 3, 4, 5).filter(i -> i % 2 == 0).value() => [2, 4]
     * </pre>
     *
     * @param predicateFn decide which element should be retained
     * @return next tube
     */
    public ListTube<T> filter(Predicate<T> predicateFn) {
        return new ListTubeStage<>(new FilterOperator<>(Objects.requireNonNull(predicateFn)), this);
    }

    // TODO: filter support bi-predicate


    /**
     * Collect the first element.
     * <p>
     * Examples:
     * <pre>
     * $(1, 2).first()          => Optional.of(1)
     * $((Object) null).first() => Optional.empty()
     * $().first()              => Optional.empty()
     * </pre>
     *
     * @return first element
     */
    public Optional<T> first() {
        return this.collect(new FirstCollector<>());
    }


    /**
     * Examples:
     * <pre>
     * $(1, 2, 3).flatMap(i -> $(i*2, i*3)).value() => [2, 3, 4, 6, 6, 9]
     * </pre>
     *
     * @param flatMapFn which flatMap elements to a tube
     * @param <R>       flatted elements type
     * @return next tube
     */
    public <R> ListTube<R> flatMap(Function<T, Tube<R>> flatMapFn) {
        return new ListTubeStage<>(new FlatMapOperator<>(Objects.requireNonNull(flatMapFn)), this);
    }


    /**
     * Examples:
     * <pre>
     * $(1, 2, 3).flatten(i -> $.list(i * 2, i * 3)).value() => [2, 3, 4, 6, 6, 9]
     * </pre>
     *
     * @param flattenFn which flatten elements to a list
     * @param <R>       flatted elements type
     * @return next tube
     */
    public <R> ListTube<R> flatten(Function<T, List<R>> flattenFn) {
        return new ListTubeStage<>(new FlatMapOperator<>(it -> new ListTubeFromList<>(flattenFn.apply(it))), this);
    }


    /**
     * Iterate all elements.
     *
     * @param handler which handling element
     * @return handled elements count
     */
    public int forEach(Consumer<T> handler) {
        return this.collect(new ForEachCollector<>(handler));
    }
    // TODO: forEach support bi-consumer


    /**
     * Group elements by key.
     * <p>
     * Examples:
     * <pre>
     * $(1, 2, 3, 4, 5).groupBy(i -> i % 2 == 0 ? "even" : "odd").value() => { "odd": [1, 3, 5], "even": [2, 4] }
     * </pre>
     *
     * @param toKey to calculate element key
     * @param <K>   element key type
     * @return next tube
     */
    public <K> MapTube<K, List<T>> groupBy(Function<T, K> toKey) {
        return new MapTubeForGroupBy<>(this.map(it -> Pair.of(toKey.apply(it), it)));
    }


    /**
     * Alias for {@link #first()}
     *
     * @return first element
     * @see #first()
     */
    public Optional<T> head() {
        return this.first();
    }


    /**
     * Join the string representation(<code>toString()</code>) of all elements
     * with specified <code>sep</code>arator.
     * <pre>
     * $("hello", "world").join(", ") => "hello, world"
     * $(1, 2, 3, 4, 5).join(" - ")   => "1 - 2 - 3 - 4 - 5"
     * </pre>
     *
     * @param sep joining separator string
     * @return joined string
     */
    public String join(String sep) {
        return this.collect(new JoinCollector<>(sep));
    }


    /**
     * Aggregate all elements into a map with a specified key.
     * If two elements produce the same key, the first consumed element will be selected.
     * <p>
     * Examples:
     * <pre>
     * $(1, 2, 3, 4, 5).keyBy(i -> i % 2 == 0 ? "even" : "odd").value() => { "odd": 1, "even": 2 }
     * </pre>
     *
     * @param toKey to calculate element key
     * @param <K>   element key type
     * @return next tube
     */
    public <K> MapTube<K, T> keyBy(Function<T, K> toKey) {
        return new MapTubeForKeyBy<>(this.map(it -> Pair.of(toKey.apply(it), it)));
    }


    /**
     * Examples:
     * <pre>
     * $(1, 2, 3).map(i -> i * 2).value() => [2, 4, 6]
     * $(1, 2, 3).map(i -> i % 2).value() => [1, 0, 1]
     * </pre>
     *
     * @param mapFn mapper function
     * @param <R>   element type of map result
     * @return next tube
     */
    public <R> ListTube<R> map(Function<T, R> mapFn) {
        return new ListTubeStage<>(new MapOperator<>(Objects.requireNonNull(mapFn)), this);
    }
    // TODO: map support bi-function


    /**
     * Sort all elements with specified key and direction.
     * <pre>
     * $("C1", "A2", "B3").orderBy(it -> Integer.parseInt(it.substring(1)), ASC).value() => [C1, A2, B3]
     * $("C1", "A2", "B3").orderBy(Function.identity(), ASC).value() => [A2, B3, C1]
     * </pre>
     *
     * @param toKey     to calculate element key
     * @param direction order by derection
     * @param <K>       element key type
     * @return next tube
     */
    public <K extends Comparable<K>> ListTube<T> orderBy(Function<T, K> toKey, Direction direction) {
        return new ListTubeStage<>(new OrderByOperator<>(Objects.requireNonNull(toKey), Objects.requireNonNull(direction)), this);
    }


    /**
     * Examples:
     * <pre>
     * $(1, 2, 3).reduce(10, Integer::sum) => 16
     *
     * BiFunction&lt;ArrayList&lt;Integer&gt;, Integer, ArrayList&lt;Integer&gt;&gt; accelerator = (list, it) -> {
     *       list.add(it);
     *       return list;
     * };
     * $(1, 2, 3).reduce(new ArrayList&lt;Integer&gt;(), accelerator) => [1, 2, 3]
     * </pre>
     *
     * @param identity    the identity element of accelerator
     * @param accelerator accelerate function
     * @param <R>         identity type
     * @return the result of the reduction
     */
    public <R> R reduce(R identity, BiFunction<R, T, R> accelerator) {
        return this.collect(new ReduceCollector<>(identity, Objects.requireNonNull(accelerator)));
    }


    /**
     * Examples:
     * <pre>
     * $(1, 2, 3).reverse().value() => [3, 2, 1]
     * </pre>
     *
     * @return next tube
     */
    public ListTube<T> reverse() {
        return new ListTubeStage<>(new ReverseOperator<>(), this);
    }


    /**
     * Take the first <code>n</code> elements.
     * <p>
     * Examples:
     * <pre>
     * $(1, 2, 3, 4, 5).take(6).value() => [1, 2, 3, 4, 5]
     * $(1, 2, 3, 4, 5).take(3).value() => [1, 2, 3]
     * </pre>
     *
     * @param n elements count to take, which should be greater than 0
     * @return next tube
     */
    public ListTube<T> take(int n) {
        return new ListTubeStage<>(new TakeOperator<>(n), this);
    }


    /**
     * Take elements from the beginning, until <code>predicate</code> returns <code>false</code>.
     * <p>
     * Examples:
     * <pre>
     * $(1, 2, 3, 4, 5).takeWhile(i -> i &lt; 3).value() => [1, 2]
     * </pre>
     *
     * @param predicate to determine which elements should be taken
     * @return next tube
     */
    public ListTube<T> takeWhile(Predicate<T> predicate) {
        return new ListTubeStage<>(new TakeWhileOperator<>(predicate), this);
    }


    /**
     * Examples:
     * <pre>
     * $(1, 2, 1).unique().value() => [1, 2]
     * </pre>
     *
     * @return next tube
     */
    public ListTube<T> unique() {
        return new ListTubeStage<>(new UniqueOperator<>(), this);
    }


    /**
     * Examples:
     * <pre>
     * $(1.2, 2.3, 1.4).uniqueBy(Double::intValue).value() => [1.2, 2.3]
     * </pre>
     *
     * @param toKey to calculate element key
     * @param <K>   element key type
     * @return next tube
     */
    public <K> ListTube<T> uniqueBy(Function<T, K> toKey) {
        return new ListTubeStage<>(new UniqueByOperator<>(toKey), this);
    }


    /**
     * Collect element into a list.
     *
     * @return collected list
     */
    public List<T> value() {
        return this.collect(new ListCollector<>());
    }


    /**
     * A api not for user. FIXME: hide it
     *
     * @return next capsule
     */
    public abstract Capsule<T> next();
}
