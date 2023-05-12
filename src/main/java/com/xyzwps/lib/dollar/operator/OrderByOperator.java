package com.xyzwps.lib.dollar.operator;

import com.xyzwps.lib.dollar.Direction;
import com.xyzwps.lib.dollar.collector.ListCollector;
import com.xyzwps.lib.dollar.Tube;
import com.xyzwps.lib.dollar.tube.EndException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.function.Function;


/**
 * Used by orderBy method.
 *
 * @param <T> source element type
 * @param <K> sorting key type
 */
public class OrderByOperator<T, K extends Comparable<K>> implements Operator<T, T> {

    private final Function<T, K> toKey;
    private final Direction direction;

    public OrderByOperator(Function<T, K> toKey, Direction direction) {
        this.toKey = toKey;
        this.direction = direction;
    }

    private Iterator<T> itr;

    @Override
    public T next(Tube<T> upstream) throws EndException {
        if (itr == null) {
            this.initItr(upstream);
        }

        if (itr.hasNext()) {
            return itr.next();
        } else {
            throw new EndException();
        }
    }

    private void initItr(Tube<T> upstream) {
        ArrayList<T> list = upstream.collect(new ListCollector<>());
        Comparator<T> comparator = direction == Direction.DESC ? descComparator(toKey) : ascComparator(toKey);
        list.sort(comparator);
        this.itr = list.iterator();
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
