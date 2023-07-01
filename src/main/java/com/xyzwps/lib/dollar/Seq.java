package com.xyzwps.lib.dollar;


import com.xyzwps.lib.dollar.function.ObjIntFunction;
import com.xyzwps.lib.dollar.function.ObjIntPredicate;

import java.util.*;
import java.util.function.*;

import static com.xyzwps.lib.dollar.Dollar.*;
import static com.xyzwps.lib.dollar.Helper.*;

/**
 * 操作符应该分 3 类
 * 1. 收集一个就可以干活，比如 map、filter
 * 2. 收集一些才能干活，比如 chunk
 * 3. 收集全部才能干活，比如 groupBy、orderBy、reverse
 * <p>
 * TODO: 实现 index 版本
 *
 * @param <T>
 */
public interface Seq<T> extends Iterable<T> {

    void forEach(Consumer<? super T> consumer);

    default void forEach(ObjIntConsumer<? super T> consumer) {
        Objects.requireNonNull(consumer);
        Counter counter = new Counter(0);
        this.forEach(t -> consumer.accept(t, counter.getAndIncr()));
    }

    default <R> Seq<R> map(Function<T, R> mapFn) {
        Objects.requireNonNull(mapFn);
        return rConsumer -> this.forEach(t -> rConsumer.accept(mapFn.apply(t)));
    }

    default <R> Seq<R> map(ObjIntFunction<T, R> mapFn) {
        Objects.requireNonNull(mapFn);
        Counter counter = new Counter(0);
        return rConsumer -> this.forEach(t -> rConsumer.accept(mapFn.apply(t, counter.getAndIncr())));
    }

    default Seq<T> filter(Predicate<T> predicate) {
        Objects.requireNonNull(predicate);
        return tConsumer -> this.forEach(t -> {
            if (predicate.test(t)) {
                tConsumer.accept(t);
            }
        });
    }

    default Seq<T> filter(ObjIntPredicate<T> predicate) {
        Objects.requireNonNull(predicate);
        Counter counter = new Counter(0);
        return tConsumer -> this.forEach(t -> {
            if (predicate.test(t, counter.getAndIncr())) {
                tConsumer.accept(t);
            }
        });
    }

    default <R> Seq<R> flatMap(Function<T, Seq<R>> flatMapFn) {
        return rConsumer -> this.forEach(t -> {
            Seq<R> rSeq = flatMapFn.apply(t);
            if (rSeq != null) {
                rSeq.forEach(rConsumer);
            }
        });
    }

    default Seq<List<T>> chunk(final int chunkSize) {
        if (chunkSize < 1) {
            throw new IllegalArgumentException("Each chunk should have at least one element.");
        }

        // TODO: 优化
        class ChunkEnv {
            List<T> list = new ArrayList<>(chunkSize);
            int count = 0;

            boolean add(T t) {
                this.list.add(t);
                this.count++;
                return this.count == chunkSize;
            }

            List<T> getAndClean() {
                List<T> result = this.list;
                this.list = new ArrayList<>(chunkSize);
                this.count = 0;
                return result;
            }
        }

        return listConsumer -> {
            ChunkEnv env = new ChunkEnv();
            this.forEach(t -> {
                if (env.add(t)) {
                    listConsumer.accept(env.getAndClean());
                }
            });
            if (env.count > 0) {
                listConsumer.accept(env.getAndClean());
            }
        };
    }

    default Seq<T> compact() {
        return this.filter(t -> !$.isFalsey(t));
    }

    default Seq<T> concat(Iterable<T> seq2) {
        if (seq2 == null) return this;

        return tConsumer -> {
            this.forEach(tConsumer);
            seq2.forEach(tConsumer);
        };
    }


    default Seq<T> take(final int n) {
        if (n < 1) {
            throw new IllegalArgumentException("You should take at least one element.");
        }

        return StopException.stop(tConsumer -> {
            Counter counter = new Counter(0);
            this.forEach(t -> {
                if (counter.getAndIncr() < n) {
                    tConsumer.accept(t);
                }

                if (counter.get() >= n) {
                    throw new StopException();
                }
            });
        });
    }

    default Seq<T> takeWhile(Predicate<T> predicate) {
        Objects.requireNonNull(predicate);
        return StopException.stop(tConsumer -> {
            this.forEach(t -> {
                if (predicate.test(t)) {
                    tConsumer.accept(t);
                } else {
                    throw new StopException();
                }
            });
        });
    }

    default Seq<T> skip(int n) {
        return tConsumer -> {
            int[] counter = {0};
            this.forEach(t -> {
                if (counter[0] < n) {
                    counter[0]++;
                } else {
                    tConsumer.accept(t);
                }
            });
        };
    }

    default Seq<T> skipWhile(Predicate<T> predicate) {
        return tConsumer -> {
            boolean[] next = {true};
            this.forEach(t -> {
                next[0] = next[0] && predicate.test(t);
                if (!next[0]) {
                    tConsumer.accept(t);
                }
            });
        };
    }

    default Optional<T> first() {
        Holder<T> holder = new Holder<>(null);
        StopException.stop(() -> this.forEach(t -> {
            holder.value = t;
            throw new StopException();
        }));
        return Optional.ofNullable(holder.value);
    }

    default Optional<T> head() {
        return this.first();
    }

    default <R> R reduce(R initValue, BiFunction<R, T, R> reducer) {
        Holder<R> rHolder = new Holder<>(initValue);
        this.forEach(t -> rHolder.value = reducer.apply(rHolder.value, t));
        return rHolder.value;
    }

    default Seq<T> reverse() {
        ArrayList<T> list = this.toList();
        ArrayListReverseIterator<T> itr = new ArrayListReverseIterator<>(list);
        return tConsumer -> {
            while (itr.hasNext()) tConsumer.accept(itr.next());
        };
    }

    @Override
    default Iterator<T> iterator() {
        List<T> list = this.toList();
        return list.iterator();
    }

    default <K extends Comparable<K>> Seq<T> orderBy(Function<T, K> toKey, Direction direction) {
        Objects.requireNonNull(toKey);
        Objects.requireNonNull(direction);
        ArrayList<T> list = this.toList();
        Comparator<T> comparator = direction == Direction.DESC ? descComparator(toKey) : ascComparator(toKey);
        list.sort(comparator);
        return list::forEach;
    }

    default <R, T2> Seq<R> zip(Iterable<T2> iterable, BiFunction<T, T2, R> zipper) {
        Objects.requireNonNull(zipper);
        if (iterable == null) {
            return this.map(t -> zipper.apply(t, null));
        }

        Iterator<T2> itr = iterable.iterator();
        return rConsumer -> {
            this.forEach(t -> rConsumer.accept(zipper.apply(t, itr.hasNext() ? itr.next() : null)));
            while (itr.hasNext()) {
                rConsumer.accept(zipper.apply(null, itr.next()));
            }
        };
    }

    default <T2> Seq<Pair<T, T2>> zip(Iterable<T2> iterable) {
        return this.zip(iterable, Pair::of);
    }

    default <K> MESeq<K, List<T>> groupBy(Function<T, K> toKey) {
        Objects.requireNonNull(toKey);
        Map<K, List<T>> map = new HashMap<>();
        this.forEach(t -> map.computeIfAbsent(toKey.apply(t), k -> new ArrayList<>()).add(t));
        return map::forEach;
    }

    default <K> MESeq<K, T> keyBy(Function<T, K> toKey) {
        Objects.requireNonNull(toKey);
        Map<K, T> map = new HashMap<>();
        this.forEach(t -> map.computeIfAbsent(toKey.apply(t), k -> t));
        return map::forEach;
    }

    default <K> Seq<T> uniqueBy(Function<T, K> toKey) {
        Objects.requireNonNull(toKey);
        Set<K> set = new HashSet<>();
        return tConsumer -> {
            this.forEach(t -> {
                K key = toKey.apply(t);
                if (set.contains(key)) {
                    return;
                }

                set.add(key);
                tConsumer.accept(t);
            });
        };
    }

    default Seq<T> unique() {
        Set<T> set = new HashSet<>();
        return tConsumer -> {
            this.forEach(t -> {
                if (set.contains(t)) {
                    return;
                }

                set.add(t);
                tConsumer.accept(t);
            });
        };
    }

    default String join(String sep) {
        // TODO: 检查 seq
        return this.reduce(new StringJoiner(sep), (joiner, t) -> {
            joiner.add(t == null ? null : t.toString());
            return joiner;
        }).toString();
    }

    default HashSet<T> toSet() {
        return this.reduce(new HashSet<>(), (set, t) -> {
            set.add(t);
            return set;
        });
    }

    default ArrayList<T> toList() {
        return this.reduce(new ArrayList<>(), (list, t) -> {
            list.add(t);
            return list;
        });
    }

    static <T> Seq<T> empty() {
        return tConsumer -> {
        };
    }

    default List<T> value() {
        return this.toList();
    }

}
