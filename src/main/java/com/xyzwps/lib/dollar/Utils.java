package com.xyzwps.lib.dollar;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Dollar utilities.
 */
public final class Utils {

    /**
     * Create an empty iterator.
     *
     * @param <T> element type
     * @return an empty iterator
     */
    public static <T> Iterator<T> emptyIterator() {
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


    /**
     * A always-true predicate.
     */
    public static final Predicate ALWAYS_TRUE_PREDICATE = (o) -> true;


    /**
     * A always-true bi-predicate.
     */
    public static final BiPredicate ALWAYS_TRUE_BIPREDICATE = (o, i) -> true;


    private Utils() throws IllegalAccessException {
        throw new IllegalAccessException("???");
    }
}
