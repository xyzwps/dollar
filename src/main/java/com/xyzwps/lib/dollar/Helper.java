package com.xyzwps.lib.dollar;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;

class Helper {
    static final class Counter {
        private int count = 0;
        private final int init;

        Counter(int init) {
            this.count = init;
            this.init = init;
        }

        int getAndIncr() {
            return count++;
        }

        int incrAndGet() {
            return ++count;
        }

        int get() {
            return count;
        }

        void reset() {
            this.count = this.init;
        }
    }

    static class Holder<T> {
        T value;

        Holder(T value) {
            this.value = value;
        }
    }

    /**
     * Create an ascending order comparator.
     *
     * @param toKey for getting element key
     * @param <E>   element type
     * @param <K>   element key type
     * @return ascending comparator
     */
    static <E, K extends Comparable<K>> Comparator<E> ascComparator(Function<E, K> toKey) {
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
    static <E, K extends Comparable<K>> Comparator<E> descComparator(Function<E, K> toKey) {
        return (o1, o2) -> toKey.apply(o2).compareTo(toKey.apply(o1));
    }

    /**
     * An empty iterator. You can get nothing here.
     *
     * @param <T> element type. Haha.
     */
    static class EmptyIterator<T> implements Iterator<T> {

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public T next() {
            throw new NoSuchElementException();
        }

        private EmptyIterator() {
        }

        /**
         * Create an empty iterator.
         *
         * @param <T> element type
         * @return an empty iterator
         */
        public static <T> Iterator<T> create() {
            return new EmptyIterator<>();
        }
    }
}
