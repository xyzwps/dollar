package com.xyzwps.lib.dollar.seq;


import com.xyzwps.lib.dollar.Direction;
import com.xyzwps.lib.dollar.iterator.ArrayListReverseIterator;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.xyzwps.lib.dollar.Dollar.*;
import static com.xyzwps.lib.dollar.iterator.OrderByIterator.*;

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
public interface Seq<T> {

    void forEach(Consumer<T> consumer);

    default <R> Seq<R> map(Function<T, R> mapFn) {
        Objects.requireNonNull(mapFn);
        return rConsumer -> this.forEach(t -> rConsumer.accept(mapFn.apply(t)));
    }

    default Seq<T> filter(Predicate<T> predicate) {
        Objects.requireNonNull(predicate);
        return tConsumer -> this.forEach(t -> {
            if (predicate.test(t)) {
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

    // TODO: 多版本
    default Seq<T> concat(Seq<T> seq2) {
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
            int[] counter = {0};
            this.forEach(t -> {
                if (counter[0] < n) {
                    tConsumer.accept(t);
                    counter[0]++;
                } else {
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

    // TODO: implement Iterable
    default Iterator<T> iterator() {
        // TODO: 有点难搞，怎么把一个回调拆开成两部分
        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public T next() {
                return null;
            }
        };
    }

    default <K extends Comparable<K>> Seq<T> orderBy(Function<T, K> toKey, Direction direction) {
        Objects.requireNonNull(toKey);
        Objects.requireNonNull(direction);
        ArrayList<T> list = this.toList();
        Comparator<T> comparator = direction == Direction.DESC ? descComparator(toKey) : ascComparator(toKey);
        list.sort(comparator);
        return list::forEach;
    }

    // TODO: 多版本
    default <R, T2> Seq<R> zip(Seq<T2> seq2, BiFunction<T, T2, R> zipper) {
        Objects.requireNonNull(zipper);
        if (seq2 == null) {
            return this.map(t -> zipper.apply(t, null));
        }

        Iterator<T2> itr = seq2.iterator();
        return rConsumer -> {
            this.forEach(t -> rConsumer.accept(zipper.apply(t, itr.hasNext() ? itr.next() : null)));
            while (itr.hasNext()) {
                rConsumer.accept(zipper.apply(null, itr.next()));
            }
        };
    }

    // TODO: groupBy
    // TODO: keyBy

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

    default List<T> value() {
        return this.toList();
    }

    static void main(String[] args) {
        Seq<Integer> seq = consumer -> $.listOf(1, 2, 3, 4, 5, 6, 7).forEach(consumer);
        seq.chunk(3).forEach(System.out::println);
        seq.chunk(5).forEach(System.out::println);

        System.out.println(seq.map(i -> {
            System.out.println(i + " mapped.");
            return i;
        }).take(3).toList());

        System.out.println("=== take while ===");

        System.out.println(seq.map(i -> {
            System.out.println(i + " mapped.");
            return i;
        }).takeWhile(i -> i < 5).toList());

        System.out.println("=== skip ===");

        System.out.println(seq.skip(3).toList());

        System.out.println("=== skip while ===");

        System.out.println(seq.skipWhile(i -> i % 2 == 1).toList());
        System.out.println(seq.skipWhile(i -> i % 2 == 0).toList());

        System.out.println("=== first ===");

        System.out.println(seq.first());

        System.out.println("=== reverse ===");

        System.out.println(seq.reverse().toList());
    }

}
