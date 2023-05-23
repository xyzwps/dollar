package com.xyzwps.lib.dollar.stage;

import com.xyzwps.lib.dollar.Direction;
import com.xyzwps.lib.dollar.iterable.ChainedIterable;
import com.xyzwps.lib.dollar.iterable.EmptyIterable;
import com.xyzwps.lib.dollar.iterator.EmptyIterator;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.xyzwps.lib.dollar.Dollar.*;

public class ListStage<T> implements Iterable<T> {

    private final Iterable<T> iterable;

    public ListStage(Iterable<T> iterable) {
        this.iterable = iterable == null ? EmptyIterable.create() : iterable;
    }

    private <S> ListStage(Iterable<S> up, Function<Iterator<S>, Iterator<T>> chainFn) {
        this(ChainedIterable.create(up, chainFn));
    }

    public ListStage<List<T>> chunk(int chunkSize) {
        return new ListStage<>(this.iterable, up -> new ChunkIterator<>(up, chunkSize));
    }

    public ListStage<T> concat(Iterable<T> tail) {
        return new ListStage<>(this.iterable, up -> {
            Iterator<T> itr = tail == null ? EmptyIterator.create() : tail.iterator();
            return new ConcatIterator<>(up, itr);
        });
    }

    public ListStage<T> filter(Predicate<T> predicate) {
        return new ListStage<>(this.iterable, up -> new FilterIterator<>(up, predicate));
    }

    public <R> ListStage<R> flatMap(Function<T, Iterable<R>> flatMapFn) {
        return new ListStage<>(this.iterable, up -> new FlatMapIterator<>(up, flatMapFn));
    }

    public <R> ListStage<R> map(Function<T, R> mapFn) {
        return new ListStage<>(this.iterable, up -> new MapIterator<>(up, mapFn));
    }

    public <K extends Comparable<K>> ListStage<T> orderBy(Function<T, K> toKey, Direction direction) {
        return new ListStage<>(this.iterable, up -> new OrderByIterator<>(up, toKey, direction));
    }

    public ListStage<T> reverse() {
        return new ListStage<>(this.iterable, ReverseIterator::new);
    }

    public ListStage<T> take(int n) {
        return new ListStage<>(this.iterable, up -> new TakeIterator<>(up, n));
    }

    public ListStage<T> takeWhile(Predicate<T> predicate) {
        return new ListStage<>(this.iterable, up -> new TakeWhileIterator<>(up, predicate));
    }

    public ListStage<T> unique() {
        return this.uniqueBy(Function.identity());
    }

    public <K> ListStage<T> uniqueBy(Function<T, K> toKey) {
        return new ListStage<>(this.iterable, up -> new UniqueByIterator<>(up, toKey));
    }

    @Override
    public Iterator<T> iterator() {
        return iterable.iterator();
    }

    public List<T> value() {
        return $.arrayListFrom(this.iterator());
    }
}
