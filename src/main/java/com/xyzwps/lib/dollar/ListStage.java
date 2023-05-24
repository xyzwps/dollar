package com.xyzwps.lib.dollar;

import com.xyzwps.lib.dollar.collector.Collector;
import com.xyzwps.lib.dollar.collector.JoinCollector;
import com.xyzwps.lib.dollar.collector.ReduceCollector;
import com.xyzwps.lib.dollar.function.ObjIntFunction;
import com.xyzwps.lib.dollar.function.ObjIntPredicate;
import com.xyzwps.lib.dollar.iterable.ChainedIterable;
import com.xyzwps.lib.dollar.iterable.EmptyIterable;
import com.xyzwps.lib.dollar.iterator.*;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.ObjIntConsumer;
import java.util.function.Predicate;

import static com.xyzwps.lib.dollar.Dollar.*;

public class ListStage<T> implements Iterable<T> {

    private final Iterable<T> iterable;

    public ListStage(Iterable<T> iterable) {
        this.iterable = iterable == null ? EmptyIterable.create() : iterable;
    }

    <S> ListStage(Iterable<S> up, Function<Iterator<S>, Iterator<T>> chainFn) {
        this(ChainedIterable.create(up, chainFn));
    }

    /**
     * Examples:
     * <pre>
     * $.just(1, 2, 3, 4, 5).chunk(2).value() => [[1,2], [3,4], [5]]
     * </pre>
     *
     * @param size The length of each chunk, should be greater than 0.
     * @return next stage
     */
    public ListStage<List<T>> chunk(int size) {
        return new ListStage<>(this.iterable, up -> new ChunkIterator<>(up, size));
    }

    /**
     * Exclude all falsey values. The values <code>null</code>, <code>false</code>,
     * <code>0(.0)</code> and <code>""</code> are falsey.
     * <p>
     * Examples:
     * <pre>
     * $.just(null, 1, 0, true, false, "a", "").compact().value() => [1, true, ""]
     * </pre>
     *
     * @return next stage
     * @see DollarGeneral#isFalsey(Object)
     */
    public ListStage<T> compact() {
        return this.filter(it -> !$.isFalsey(it));
    }

    /**
     * Concatenating with an {@link Iterable}.
     *
     * @param tail concatenated iterable. Null is allowed.
     * @return next stage
     */
    public ListStage<T> concat(Iterable<T> tail) {
        return new ListStage<>(this.iterable, up -> {
            Iterator<T> itr = tail == null ? EmptyIterator.create() : tail.iterator();
            return new ConcatIterator<>(up, itr);
        });
    }

    /**
     * Iterates over elements, and exclude those which being predicated with <code>false</code>.
     * <p>
     * Examples:
     * <pre>
     * $.just(1, 2, 3, 4, 5).filter(i -> i % 2 == 1).value() => [1, 3, 5]
     * $.just(1, 2, 3, 4, 5).filter(i -> i % 2 == 0).value() => [2, 4]
     * </pre>
     *
     * @param predicate determine which element should be retained. Not null.
     * @return next stage
     */
    public ListStage<T> filter(Predicate<T> predicate) {
        Objects.requireNonNull(predicate);
        return this.filter((it, index) -> predicate.test(it));
    }

    /**
     * Indexed version of {@link #filter(Predicate)}.
     *
     * @param predicateFn determine which element should be retained. Not null.
     * @return next tube
     */
    public ListStage<T> filter(ObjIntPredicate<T> predicateFn) {
        return new ListStage<>(this.iterable, up -> new FilterIterator<>(up, predicateFn));
    }

    /**
     * Examples:
     * <pre>
     * $.just(1, 2, 3).flatMap(i -> $.just(i*2, i*3)).value() => [2, 3, 4, 6, 6, 9]
     * </pre>
     *
     * @param flatMapFn which flatMap elements to a tube
     * @param <R>       flatted elements type
     * @return next stage
     */
    public <R> ListStage<R> flatMap(Function<T, Iterable<R>> flatMapFn) {
        return new ListStage<>(this.iterable, up -> new FlatMapIterator<>(up, flatMapFn));
    }

    /**
     * Examples:
     * <pre>
     * $.just(1, 2, 3).flatten(i -> $.list(i * 2, i * 3)).value() => [2, 3, 4, 6, 6, 9]
     * $.just(1, 2, 3).flatten(i -> null).value() => []
     * </pre>
     * <p>
     *
     * @param flattenFn which flatten elements to a list
     * @param <R>       flatted elements type
     * @return next stage
     */
    public <R> ListStage<R> flatten(Function<T, Iterable<R>> flattenFn) {
        return this.flatMap(flattenFn);
    }


    /**
     * Group elements by key.
     * <p>
     * Examples:
     * <pre>
     * $.just(1, 2, 3, 4, 5).groupBy(i -> i % 2 == 0 ? "even" : "odd").value() => { "odd": [1, 3, 5], "even": [2, 4] }
     * </pre>
     *
     * @param toKey to calculate element key
     * @param <K>   element key type
     * @return next tube TODO: 删除所有 tube 字眼
     */
    public <K> MapStage<K, List<T>> groupBy(Function<T, K> toKey) {
        return new MapStage<>(() -> new GroupByIterator<>(this.iterator(), toKey));
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
     * @return next stage
     */
    public <K> MapStage<K, T> keyBy(Function<T, K> toKey) {
        return new MapStage<K, T>(() -> new KeyByIterator<>(this.iterator(), toKey));
    }

    /**
     * Examples:
     * <pre>
     * $.just(1, 2, 3).map(i -> i * 2).value() => [2, 4, 6]
     * $.just(1, 2, 3).map(i -> i % 2).value() => [1, 0, 1]
     * </pre>
     *
     * @param mapFn mapper function
     * @param <R>   element type of map result
     * @return next stage
     */
    public <R> ListStage<R> map(Function<T, R> mapFn) {
        Objects.requireNonNull(mapFn);
        return this.map((it, index) -> mapFn.apply(it));
    }

    /**
     * Indexed version of {@link #map(Function)}.
     *
     * @param mapFn mapper function
     * @param <R>   element type of map result
     * @return next tube
     */
    public <R> ListStage<R> map(ObjIntFunction<T, R> mapFn) {
        return new ListStage<>(this.iterable, up -> new MapIterator<>(up, mapFn));
    }

    /**
     * Sort all elements with specified key and direction.
     * <pre>
     * $.just("C1", "A2", "B3").orderBy(it -> Integer.parseInt(it.substring(1)), ASC).value() => [C1, A2, B3]
     * $.just("C1", "A2", "B3").orderBy(Function.identity(), ASC).value() => [A2, B3, C1]
     * </pre>
     *
     * @param toKey     to calculate element key
     * @param direction order by derection
     * @param <K>       element key type
     * @return next stage
     */
    public <K extends Comparable<K>> ListStage<T> orderBy(Function<T, K> toKey, Direction direction) {
        return new ListStage<>(this.iterable, up -> new OrderByIterator<>(up, toKey, direction));
    }

    /**
     * Examples:
     * <pre>
     * $.just(1, 2, 3).reverse().value() => [3, 2, 1]
     * </pre>
     *
     * @return next stage
     */
    public ListStage<T> reverse() {
        return new ListStage<>(this.iterable, ReverseIterator::new);
    }

    /**
     * Take the first <code>n</code> elements.
     * <p>
     * Examples:
     * <pre>
     * $.just(1, 2, 3, 4, 5).take(6).value() => [1, 2, 3, 4, 5]
     * $.just(1, 2, 3, 4, 5).take(3).value() => [1, 2, 3]
     * </pre>
     *
     * @param n elements count to take, which should be greater than 0
     * @return next stage
     */
    public ListStage<T> take(int n) {
        return new ListStage<>(this.iterable, up -> new TakeIterator<>(up, n));
    }
    // TODO: take 可以用 filter 实现

    /**
     * Take elements from the beginning, until <code>predicate</code> returns <code>false</code>.
     * <p>
     * Examples:
     * <pre>
     * $.just(1, 2, 3, 4, 5).takeWhile(i -> i &lt; 3).value() => [1, 2]
     * </pre>
     *
     * @param predicate to determine which elements should be taken
     * @return next stage
     */
    public ListStage<T> takeWhile(Predicate<T> predicate) {
        return new ListStage<>(this.iterable, up -> new TakeWhileIterator<>(up, predicate));
    }

    /**
     * Examples:
     * <pre>
     * $.just(1, 2, 1).unique().value() => [1, 2]
     * </pre>
     *
     * @return next stage
     */
    public ListStage<T> unique() {
        return this.uniqueBy(Function.identity());
    }

    /**
     * Examples:
     * <pre>
     * $.just(1.2, 2.3, 1.4).uniqueBy(Double::intValue).value() => [1.2, 2.3]
     * </pre>
     *
     * @param toKey to calculate element key
     * @param <K>   element key type
     * @return next stage
     */
    public <K> ListStage<T> uniqueBy(Function<T, K> toKey) {
        return new ListStage<>(this.iterable, up -> new UniqueByIterator<>(up, toKey));
    }

    /**
     * Zip to a list of pairs.
     *
     * @param list zipped list
     * @param <R>  type of elements in zipped list
     * @return zipped pairs stage
     */
    public <R> ListStage<Pair<T, R>> zip(List<R> list) {
        return this.zip(list, Pair::of);
    }


    /**
     * Zip to a list of pairs.
     *
     * @param list      zipped list
     * @param combineFn zip combine function
     * @param <R>       type of elements in zipped list
     * @param <S>       zip result type
     * @return zipped pairs stage
     */
    public <R, S> ListStage<S> zip(List<R> list, BiFunction<T, R, S> combineFn) {
        if ($.isEmpty(list)) {
            return this.map(it -> combineFn.apply(it, null));
        } else {
            return new ListStage<>(this.iterable, up -> new ZipIterator<>(up, list.iterator(), combineFn));
        }
    }

    @Override
    public Iterator<T> iterator() {
        return iterable.iterator();
    }

    /**
     * Collect elements from tube.
     *
     * @param collector element collector
     * @param <R>       result element type
     * @return the collected
     */
    public <R> R collect(Collector<T, R> collector) {
        for (T t : this) {
            if (!collector.needMore()) {
                return collector.result();
            }
            collector.onRequest(t);
        }
        return collector.result();
    }

    /**
     * Collect the first element.
     * <p>
     * Examples:
     * <pre>
     * $.just(1, 2).first()          => Optional.of(1)
     * $.just((Object) null).first() => Optional.empty()
     * $.just().first()              => Optional.empty()
     * </pre>
     *
     * @return first element
     */
    public Optional<T> first() {
        Iterator<T> itr = this.take(1).iterator();
        return itr.hasNext() ? Optional.ofNullable(itr.next()) : Optional.empty();
    }

    public void forEach(ObjIntConsumer<T> handler) {
        Iterator<T> itr = this.iterator();
        for (int i = 0; itr.hasNext(); i++) {
            handler.accept(itr.next(), i);
        }
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
     * $.just("hello", "world").join(", ") => "hello, world"
     * $.just(1, 2, 3, 4, 5).join(" - ")   => "1 - 2 - 3 - 4 - 5"
     * </pre>
     *
     * @param sep joining separator string
     * @return joined string
     */
    public String join(String sep) {
        return this.collect(new JoinCollector<>(sep));
    }

    /**
     * Examples:
     * <pre>
     * $.just(1, 2, 3).reduce(10, Integer::sum) => 16
     *
     * BiFunction&lt;ArrayList&lt;Integer&gt;, Integer, ArrayList&lt;Integer&gt;&gt; accelerator = (list, it) -> {
     *       list.add(it);
     *       return list;
     * };
     * $.just(1, 2, 3).reduce(new ArrayList&lt;Integer&gt;(), accelerator) => [1, 2, 3]
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
     * Collect element into a hash set.
     *
     * @return collected set
     */
    public HashSet<T> toSet() {
        return this.reduce(new HashSet<T>(), (set, i) -> {
            set.add(i);
            return set;
        });
    }

    /**
     * Collect element into a list.
     *
     * @return collected list
     */
    public List<T> value() {
        return $.arrayListFrom(this.iterator());
    }

}
