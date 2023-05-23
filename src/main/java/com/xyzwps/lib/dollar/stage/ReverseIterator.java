package com.xyzwps.lib.dollar.stage;

import com.xyzwps.lib.dollar.iterator.ArrayListReverseIterator;
import com.xyzwps.lib.dollar.iterator.EmptyIterator;

import java.util.Comparator;
import java.util.Iterator;
import java.util.function.Function;

import static com.xyzwps.lib.dollar.Dollar.*;

public class ReverseIterator<T> implements Iterator<T> {

    private final Iterator<T> up;

    public ReverseIterator(Iterator<T> up) {
        this.up = up == null ? EmptyIterator.create() : up;
    }

    private Iterator<T> itr;

    @Override
    public boolean hasNext() {
        this.tryToInitItr();
        return this.itr.hasNext();
    }

    @Override
    public T next() {
        this.tryToInitItr();
        return this.itr.next();
    }

    private void tryToInitItr() {
        if (this.itr != null) {
            return;
        }

        this.itr = new ArrayListReverseIterator<>($.arrayListFrom(up));
    }


    /**
     * Create an ascending order comparator.
     *
     * @param toKey for getting element key
     * @param <E>   element type
     * @param <K>   element key type
     * @return ascending comparator
     */
    public static <E, K extends Comparable<K>> Comparator<E> ascComparator(Function<E, K> toKey) {
        return Comparator.comparing(toKey);
    }


    /**
     * Create a descending order comparator.
     *
     * @param toKey for getting element key
     * @param <E>   element type
     * @param <K>   element key type
     * @return descending comparator
     */
    public static <E, K extends Comparable<K>> Comparator<E> descComparator(Function<E, K> toKey) {
        return (o1, o2) -> toKey.apply(o2).compareTo(toKey.apply(o1));
    }


}
