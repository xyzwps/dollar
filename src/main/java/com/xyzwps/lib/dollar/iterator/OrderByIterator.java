package com.xyzwps.lib.dollar.iterator;

import com.xyzwps.lib.dollar.Direction;

import java.util.*;
import java.util.function.Function;

import static com.xyzwps.lib.dollar.Dollar.*;

public class OrderByIterator<T, K extends Comparable<K>> implements Iterator<T> {

    private final Function<T, K> toKey;
    private final Direction direction;
    private final Iterator<T> up;

    public OrderByIterator(Iterator<T> up, Function<T, K> toKey, Direction direction) {
        this.up = up == null ? EmptyIterator.create() : up;
        this.toKey = Objects.requireNonNull(toKey);
        this.direction = Objects.requireNonNull(direction);
    }

    private Iterator<T> itr;

    @Override
    public boolean hasNext() {
        return this.getItr().hasNext();
    }

    @Override
    public T next() {
        return this.getItr().next();
    }

    private Iterator<T> getItr() {
        if (this.itr != null) {
            return this.itr;
        }

        List<T> list = $.listFrom(up);
        Comparator<T> comparator = direction == Direction.DESC ? descComparator(toKey) : ascComparator(toKey);
        list.sort(comparator);
        this.itr = list.iterator();
        return this.itr;
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
