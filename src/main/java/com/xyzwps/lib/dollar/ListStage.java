package com.xyzwps.lib.dollar;

import com.xyzwps.lib.dollar.function.ObjIntFunction;
import com.xyzwps.lib.dollar.function.ObjIntPredicate;
import com.xyzwps.lib.dollar.iterable.ChainIterable;
import com.xyzwps.lib.dollar.iterable.EmptyIterable;
import com.xyzwps.lib.dollar.iterator.*;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.ObjIntConsumer;
import java.util.function.Predicate;

import static com.xyzwps.lib.dollar.Dollar.*;

/**
 * {@link ListStage} 表示连续处理 {@link Iterable} 到了某个阶段。
 * <p>
 * 注意：{@link ListStage} 本身就是 {@link Iterable}，所以，
 * 你可以直接对其使用 for-each 循环语句。
 *
 * @param <T> {@link Iterable} 中的元素类型。
 */
public class ListStage<T> implements Iterable<T> {

    private final Iterable<T> iterable;

    /**
     * 连续处理的开始处。
     *
     * @param iterable 要处理的 {@link Iterable}。
     */
    public ListStage(Iterable<T> iterable) {
        this.iterable = iterable == null ? EmptyIterable.create() : iterable;
    }

    /**
     * 从一个阶段迈向下一个阶段。
     *
     * @param prev    上一个阶段
     * @param chainFn 从上一阶段迈向下一阶段的秘密。
     * @param <P>     上一阶段元素类型
     */
    <P> ListStage(Iterable<P> prev, Function<Iterator<P>, Iterator<T>> chainFn) {
        this(ChainIterable.create(prev, chainFn));
    }

    /**
     * 从头开始，把相邻的 n 个元素撮合成一组。如果最后剩下的不足 n 个元素，那么也把它们放入一组。
     * <p>
     * 例：
     * <pre>
     * $.just(1, 2, 3, 4, 5).chunk(2).value() => [[1,2], [3,4], [5]]
     * </pre>
     *
     * @param n 每组元素的数量。至少应该是 1。
     * @return 已经经过的阶段
     */
    public ListStage<List<T>> chunk(int n) {
        return new ListStage<>(this, up -> new ChunkIterator<>(up, n));
    }

    /**
     * 排除所有的 {@link $#isFalsey falsey} 值。
     * <p>
     * 例:
     * <pre>
     * $.just(null, 1, 0, true, false, "a", "").compact().value() => [1, true, "a"]
     * </pre>
     *
     * @return 已经经过的阶段
     * @see $#isFalsey(Object)
     */
    public ListStage<T> compact() {
        return this.filter(it -> !$.isFalsey(it));
    }

    /**
     * 把一个 {@link Iterable} 接到后面。
     * <p>
     * 例:
     * <pre>
     * $.just(1, 2).concat($.listOf(3, 4)).value() => [1, 2, 3, 4]
     * </pre>
     *
     * @param tail 被接到尾巴上的 {@link Iterable}
     * @return 已经经过的阶段
     */
    public ListStage<T> concat(Iterable<T> tail) {
        return new ListStage<>(this, up -> {
            Iterator<T> itr = tail == null ? EmptyIterator.create() : tail.iterator();
            return new ConcatIterator<>(up, itr);
        });
    }

    /**
     * 按顺序筛选满足条件（<i>predicate</i>）的元素。
     * <p>
     * 例:
     * <pre>
     * $.just(1, 2, 3, 4, 5).filter(i -> i % 2 == 1).value() => [1, 3, 5]
     * $.just(1, 2, 3, 4, 5).filter(i -> i % 2 == 0).value() => [2, 4]
     * </pre>
     *
     * @param predicate 被保留的元素应该满足的条件。不可以是 null。
     * @return 已经经过的阶段
     */
    public ListStage<T> filter(Predicate<T> predicate) {
        Objects.requireNonNull(predicate);
        return this.filter((it, index) -> predicate.test(it));
    }

    /**
     * 相比于 {@link #filter(Predicate)}，本方法在筛选时可以把元素的索引作为条件。
     *
     * @param predicateFn 被保留的元素应该满足的条件，第二个参数是元素索引。不可以是 null。
     * @return 已经经过的阶段
     */
    public ListStage<T> filter(ObjIntPredicate<T> predicateFn) {
        return new ListStage<>(this, up -> new FilterIterator<>(up, predicateFn));
    }

    /**
     * 把一个元素映射为一个 {@link Iterable} 之后，再对 {@link Iterable} 中的元素进行遍历。
     * <p>
     * 例:
     * <pre>
     * $.just(1, 2, 3).flatMap(i -> $.just(i*2, i*3)).value() => [2, 3, 4, 6, 6, 9]
     * </pre>
     *
     * @param flatMapFn 把元素映射为 {@link Iterable} 的函数
     * @param <R>       {@link Iterable} 中元素的类型
     * @return 已经经过的阶段
     */
    public <R> ListStage<R> flatMap(Function<T, Iterable<R>> flatMapFn) {
        return new ListStage<>(this, up -> new FlatMapIterator<>(up, flatMapFn));
    }

    /**
     * 按 key 对元素进行分组，得到一个 {@link Map} 处理流程。
     * 如果有两个元素对应相同的 key，则先出现的元素排在所在组的前面。
     * <p>
     * 例:
     * <pre>
     * $.just(1, 2, 3, 4, 5).groupBy(i -> i % 2 == 0 ? "even" : "odd").value() => { "odd": [1, 3, 5], "even": [2, 4] }
     * </pre>
     *
     * @param toKey 用于计算/获取元素的 key
     * @param <K>   key 的类型
     * @return 已经经过的阶段
     */
    public <K> MapStage<K, List<T>> groupBy(Function<T, K> toKey) {
        return new MapStage<>(() -> new GroupByIterator<>(this.iterator(), toKey));
    }

    /**
     * 按 key 对元素处理，得到一个 {@link Map} 处理流程。
     * 如果有两个元素对应相同的 key，则后来的元素会被舍弃。
     * <p>
     * 例:
     * <pre>
     * $.just(1, 2, 3, 4, 5).keyBy(i -> i % 2 == 0 ? "even" : "odd").value() => { "odd": 1, "even": 2 }
     * </pre>
     *
     * @param toKey 用于计算/获取元素的 key
     * @param <K>   key 的类型
     * @return 已经经过的阶段
     */
    public <K> MapStage<K, T> keyBy(Function<T, K> toKey) {
        return new MapStage<>(() -> new KeyByIterator<>(this.iterator(), toKey));
    }

    /**
     * 把一个元素映射为另一个元素。
     * <p>
     * 例:
     * <pre>
     * $.just(1, 2, 3).map(i -> i * 2).value() => [2, 4, 6]
     * $.just(1, 2, 3).map(i -> i % 2).value() => [1, 0, 1]
     * </pre>
     *
     * @param mapFn 映射函数
     * @param <R>   映射结果的类型
     * @return 已经经过的阶段
     */
    public <R> ListStage<R> map(Function<T, R> mapFn) {
        Objects.requireNonNull(mapFn);
        return this.map((it, index) -> mapFn.apply(it));
    }

    /**
     * 是 {@link #map(Function)} 带索引的版本。
     *
     * @param mapFn 映射函数，第二个被映射参数是元素的索引
     * @param <R>   映射结果的类型
     * @return 已经经过的阶段
     */
    public <R> ListStage<R> map(ObjIntFunction<T, R> mapFn) {
        return new ListStage<>(this, up -> new MapIterator<>(up, mapFn));
    }

    /**
     * 按指定 key 和顺序对所有元素排序。
     * <p>
     * 例：
     * <pre>
     * $.just("C1", "A2", "B3").orderBy(it -> Integer.parseInt(it.substring(1)), ASC).value() => [C1, A2, B3]
     *
     * $.just("C1", "A2", "B3").orderBy(Function.identity(), ASC).value() => [A2, B3, C1]
     * </pre>
     *
     * @param toKey     用于计算/获取元素的 key
     * @param direction 排序方向
     * @param <K>       元素 key 的类型
     * @return 已经经过的阶段
     */
    public <K extends Comparable<K>> ListStage<T> orderBy(Function<T, K> toKey, Direction direction) {
        return new ListStage<>(this, up -> new OrderByIterator<>(up, toKey, direction));
    }

    /**
     * 逆序遍历所有元素。
     * <p>
     * 例:
     * <pre>
     * $.just(1, 2, 3).reverse().value() => [3, 2, 1]
     * </pre>
     *
     * @return 已经经过的阶段
     */
    public ListStage<T> reverse() {
        return new ListStage<>(this.iterable, ReverseIterator::new);
    }

    /**
     * 按遍历顺序获取前 n(≥1) 个元素。后面未被获取到的元素不会被计算。
     * <p>
     * 例:
     * <pre>
     * $.just(1, 2, 3, 4, 5).take(6).value() => [1, 2, 3, 4, 5]
     * $.just(1, 2, 3, 4, 5).take(3).value() => [1, 2, 3]
     * </pre>
     *
     * @param n 要获取的元素个数，n≥1
     * @return 已经经过的阶段
     */
    public ListStage<T> take(int n) {
        return new ListStage<>(this, up -> new TakeIterator<>(up, n));
    }

    /**
     * 按遍历顺序，从前往后按条件筛选元素，直到遇到第一个不满足条件的为止。
     * 第一个不满足条件的元素之后的元素不会被计算。
     * <p>
     * 例:
     * <pre>
     * $.just(1, 2, 3, 4, 5).takeWhile(i -> i &lt; 3).value() => [1, 2]
     * </pre>
     *
     * @param predicate 元素应满足的条件
     * @return 已经经过的阶段
     */
    public ListStage<T> takeWhile(Predicate<T> predicate) {
        return new ListStage<>(this, up -> new TakeWhileIterator<>(up, predicate));
    }

    /**
     * 对元素进行去重。先遇到的保留，后遇到的扔掉。
     * <p>
     * 例:
     * <pre>
     * $.just(1, 2, 1).unique().value() => [1, 2]
     * </pre>
     *
     * @return 已经经过的阶段
     */
    public ListStage<T> unique() {
        return this.uniqueBy(Function.identity());
    }

    /**
     * 按元素的 key 对元素进行去重。先遇到的保留，后遇到的扔掉。
     * <p>
     * 例:
     * <pre>
     * $.just(1.2, 2.3, 1.4).uniqueBy(Double::intValue).value() => [1.2, 2.3]
     * </pre>
     *
     * @param toKey 用于计算/获取元素的 key
     * @param <K>   key 的类型
     * @return 已经经过的阶段
     */
    public <K> ListStage<T> uniqueBy(Function<T, K> toKey) {
        return new ListStage<>(this, up -> new UniqueByIterator<>(up, toKey));
    }

    /**
     * 把两列元素按索引对应组合成 {@link Pair} 序列。
     * <p>
     * 例：
     * <pre>
     * $.just(1, 2, 3).zip($.listOf(1, 2)).value() => [(1, 1), (2, 2), (3, null)]
     * </pre>
     *
     * @param iterable 右边的序列
     * @param <R>      右边序列中元素的类型
     * @return 已经经过的阶段
     */
    public <R> ListStage<Pair<T, R>> zip(Iterable<R> iterable) {
        return this.zip(iterable, Pair::of);
    }


    /**
     * 把两列元素按索引对应组合成指定序列。
     * <p>
     * 例：
     * <pre>
     * $.just(1, 2, 3).zip($.listOf(1, 2), (l, r) -> (l == null ? 0 : l) + (r == null ? 0 : r)).value() => [2, 4, 3]
     *
     * $.just(1, 2, 3).zip($.listOf(1, 2), Pair::of).value() => [(1, 1), (2, 2), (3, null)]
     * </pre>
     *
     * @param iterable  右边的序列
     * @param combineFn 组合左右两边元素的函数
     * @param <R>       右边序列中元素的类型
     * @param <S>       组合结果类型
     * @return 已经经过的阶段
     */
    public <R, S> ListStage<S> zip(Iterable<R> iterable, BiFunction<T, R, S> combineFn) {
        Objects.requireNonNull(combineFn);
        if (iterable == null) {
            return this.map(it -> combineFn.apply(it, null));
        } else {
            return new ListStage<>(this, up -> new ZipIterator<>(up, iterable.iterator(), combineFn));
        }
    }

    /**
     * @see Iterable#iterator()
     */
    @Override
    public Iterator<T> iterator() {
        return iterable.iterator();
    }


    /**
     * 获取第一个元素。
     * <p>
     * 例:
     * <pre>
     * $.just(1, 2).first()          => Optional.of(1)
     * $.just((Object) null).first() => Optional.empty()
     * $.just().first()              => Optional.empty()
     * </pre>
     *
     * @return 第一个元素
     */
    public Optional<T> first() {
        Iterator<T> itr = this.take(1).iterator();
        return itr.hasNext() ? Optional.ofNullable(itr.next()) : Optional.empty();
    }

    /**
     * 带有索引的遍历。
     *
     * @param handler 第二个参数是元素的索引
     */
    public void forEach(ObjIntConsumer<T> handler) {
        Iterator<T> itr = this.iterator();
        for (int i = 0; itr.hasNext(); i++) {
            handler.accept(itr.next(), i);
        }
    }

    /**
     * {@link #first()} 的别名。
     *
     * @return 第一个元素
     * @see #first()
     */
    public Optional<T> head() {
        return this.first();
    }

    /**
     * 用指定的 <code>seq</code> 把所有元素的字符串表示连接在一起。
     * <pre>
     * $.just("hello", "world").join(", ")       => "hello, world"
     * $.just("hello", null, "world").join(", ") => "hello, null, world"
     * $.just(1, 2, 3, 4, 5).join(" - ")         => "1 - 2 - 3 - 4 - 5"
     * </pre>
     *
     * @param sep 分隔字符串
     * @return 连接后的新字符串
     */
    public String join(String sep) {
        StringJoiner joiner = new StringJoiner(sep); // TODO: 对 sep 检查
        for (T t : this) {
            joiner.add(t.toString()); // TODO: 做 null 检查
        }
        return joiner.toString();
    }

    /**
     * 对序列中的每个元素按序执行一个提供的 reducer 函数。
     * 每一次运行 reducer 会将先前元素的计算结果作为参数传入，最后将其结果汇总为单个返回值。
     * <p>
     * 例:
     * <pre>
     * $.just(1, 2, 3).reduce(10, Integer::sum) => 16
     *
     * BiFunction&lt;List&lt;Integer&gt;, Integer, List&lt;Integer&gt;&gt; reducer = (list, it) -> {
     *       list.add(it);
     *       return list;
     * };
     * $.just(1, 2, 3).reduce(new ArrayList&lt;Integer&gt;(), reducer) => [1, 2, 3]
     * </pre>
     *
     * @param initValue 结果的初值
     * @param reducer   对每个元素进行计算的函数
     * @param <R>       结果的类型
     * @return 使用 reducer 函数遍历整个序列后的结果
     */
    public <R> R reduce(R initValue, BiFunction<R, T, R> reducer) {
        Objects.requireNonNull(reducer);

        R result = initValue;
        for (T it : this) {
            result = reducer.apply(result, it);
        }
        return result;
    }

    /**
     * 把元素装入 {@link Set}。
     *
     * @return 包含不重复元素的新 Set
     */
    public Set<T> toSet() {
        return this.reduce(new HashSet<>(), (set, i) -> {
            set.add(i);
            return set;
        });
    }

    /**
     * 把元素按顺序装入 {@link List}。
     *
     * @return 包含全部元素的新 List
     */
    public List<T> value() {
        return $.listFrom(this.iterator());
    }

}
