package com.xyzwps.lib.dollar;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;


interface DollarLists extends DollarCollections, DollarGeneral {

    /**
     * Creates a list of elements split into groups the length of size.
     * If list can't be split evenly, the final chunk will be the remaining elements.
     *
     * @param list The list to handle
     * @param size Chunk size which should be greater than 0.
     * @param <T>  Element type
     * @return new list of chunks
     */
    default <T> List<List<T>> chunk(List<T> list, int size) {
        if (size < 1) {
            throw new IllegalArgumentException("Chunk size should be greater than 0.");
        }

        if (isEmpty(list)) {
            return new ArrayList<>();
        }

        int listSize = list.size();
        int chunksCapacity = listSize / size + 1;
        List<List<T>> chunks = new ArrayList<>(chunksCapacity);
        List<T> chunk = null;
        int counter = 0;
        int i = 0;
        for (T element : list) {
            if (counter == 0) {
                chunk = new ArrayList<>(size);
            }

            chunk.add(element);
            counter++;
            i++;

            if (counter == size || i == listSize) {
                chunks.add(chunk);
                chunk = null;
                counter = 0;
            }
        }

        return chunks;
    }


    /**
     * Filter list with the elements which are not falsey.
     * <p>
     * The definition of falsey can be seen at {@link #isFalsey}
     *
     * @param list The list to filter. Null is acceptable.
     * @param <T>  List element type
     * @return new compacted list
     * @see #isFalsey
     */
    default <T> List<T> compact(List<T> list) {
        return filter(list, it -> !isFalsey(it));
    }


    /**
     * Creates a new list which concatenating all lists in order.
     *
     * @param lists The lists to concatenate
     * @param <T>   Element type
     * @return concatenated new list
     */
    default <T> List<T> concat(List<T>... lists) {
        if (lists.length == 0) {
            return new ArrayList<>();
        }

        int capacity = 0;
        for (List<T> list : lists) {
            if (isNotEmpty(list)) {
                capacity += list.size();
            }
        }

        if (capacity == 0) {
            return new ArrayList<>();
        }

        ArrayList<T> result = new ArrayList<>(capacity);
        for (List<T> list : lists) {
            if (isNotEmpty(list)) {
                result.addAll(list);
            }
        }
        return result;
    }

    /**
     * Iterate over the list and retaining the elements which are predicated true.
     *
     * @param list      The list to iterate. Null is acceptable.
     * @param predicate Predicate function. Cannot be null.
     * @param <T>       Element type
     * @return new filtered list
     */
    default <T> List<T> filter(List<T> list, Predicate<T> predicate) {
        Objects.requireNonNull(predicate);
        return filter(list, (e, i) -> predicate.test(e));
    }


    /**
     * Iterate over the list and retaining the elements which are predicated true.
     *
     * @param list      The list to iterate. Null is acceptable.
     * @param predicate Predicate function with element index. Cannot be null.
     * @param <T>       Element type
     * @return new filtered list
     */
    default <T> List<T> filter(List<T> list, BiPredicate<T, Integer> predicate) {
        Objects.requireNonNull(predicate);
        if (list == null) {
            return new ArrayList<>();
        }

        List<T> result = new ArrayList<>();
        int i = 0;
        for (T element : list) {
            if (predicate.test(element, i++)) {
                result.add(element);
            }
        }
        return result;
    }

    /**
     * Create a list.
     *
     * @param <T> element type
     * @return new ArrayList
     */
    default <T> ArrayList<T> arrayList() {
        return new ArrayList<>();
    }


    /**
     * Create a list.
     *
     * @param t1  the first element
     * @param <T> element type
     * @return new ArrayList
     */
    default <T> ArrayList<T> arrayList(T t1) {
        ArrayList<T> a = new ArrayList<>(2);
        a.add(t1);
        return a;
    }


    /**
     * Create a list.
     *
     * @param t1  the first element
     * @param t2  the second element
     * @param <T> element type
     * @return new ArrayList
     */
    default <T> ArrayList<T> arrayList(T t1, T t2) {
        ArrayList<T> a = new ArrayList<>(3);
        a.add(t1);
        a.add(t2);
        return a;
    }


    /**
     * Create a list.
     *
     * @param t1  the first element
     * @param t2  the second element
     * @param t3  the third element
     * @param <T> element type
     * @return new ArrayList
     */
    default <T> ArrayList<T> arrayList(T t1, T t2, T t3) {
        ArrayList<T> a = new ArrayList<>(4);
        a.add(t1);
        a.add(t2);
        a.add(t3);
        return a;
    }


    /**
     * Create a list.
     *
     * @param t1  the first element
     * @param t2  the second element
     * @param t3  the third element
     * @param t4  the fourth element
     * @param <T> element type
     * @return new ArrayList
     */
    default <T> ArrayList<T> arrayList(T t1, T t2, T t3, T t4) {
        ArrayList<T> a = new ArrayList<>(5);
        a.add(t1);
        a.add(t2);
        a.add(t3);
        a.add(t4);
        return a;
    }


    /**
     * Create a list.
     *
     * @param t1  the first element
     * @param t2  the second element
     * @param t3  the third element
     * @param t4  the fourth element
     * @param t5  the fifth element
     * @param <T> element type
     * @return new ArrayList
     */
    default <T> ArrayList<T> arrayList(T t1, T t2, T t3, T t4, T t5) {
        ArrayList<T> a = new ArrayList<>(6);
        a.add(t1);
        a.add(t2);
        a.add(t3);
        a.add(t4);
        a.add(t5);
        return a;
    }


    /**
     * Create a list.
     *
     * @param t1  the first element
     * @param t2  the second element
     * @param t3  the third element
     * @param t4  the fourth element
     * @param t5  the fifth element
     * @param t6  the sixth element
     * @param <T> element type
     * @return new ArrayList
     */
    default <T> ArrayList<T> arrayList(T t1, T t2, T t3, T t4, T t5, T t6) {
        ArrayList<T> a = new ArrayList<>(7);
        a.add(t1);
        a.add(t2);
        a.add(t3);
        a.add(t4);
        a.add(t5);
        a.add(t6);
        return a;
    }


    /**
     * Create a list.
     *
     * @param t1  the first element
     * @param t2  the second element
     * @param t3  the third element
     * @param t4  the fourth element
     * @param t5  the fifth element
     * @param t6  the sixth element
     * @param t7  the seventh element
     * @param <T> element type
     * @return new ArrayList
     */
    default <T> ArrayList<T> arrayList(T t1, T t2, T t3, T t4, T t5, T t6, T t7) {
        ArrayList<T> a = new ArrayList<>(8);
        a.add(t1);
        a.add(t2);
        a.add(t3);
        a.add(t4);
        a.add(t5);
        a.add(t6);
        a.add(t7);
        return a;
    }


    /**
     * Create a list.
     *
     * @param t1  the first element
     * @param t2  the second element
     * @param t3  the third element
     * @param t4  the fourth element
     * @param t5  the fifth element
     * @param t6  the sixth element
     * @param t7  the seventh element
     * @param t8  the eighth element
     * @param <T> element type
     * @return new ArrayList
     */
    default <T> ArrayList<T> arrayList(T t1, T t2, T t3, T t4, T t5, T t6, T t7, T t8) {
        ArrayList<T> a = new ArrayList<>(9);
        a.add(t1);
        a.add(t2);
        a.add(t3);
        a.add(t4);
        a.add(t5);
        a.add(t6);
        a.add(t7);
        a.add(t8);
        return a;
    }


    /**
     * Create a list.
     *
     * @param t1  the first element
     * @param t2  the second element
     * @param t3  the third element
     * @param t4  the fourth element
     * @param t5  the fifth element
     * @param t6  the sixth element
     * @param t7  the seventh element
     * @param t8  the eighth element
     * @param t9  the ninth element
     * @param <T> element type
     * @return new ArrayList
     */
    default <T> ArrayList<T> arrayList(T t1, T t2, T t3, T t4, T t5, T t6, T t7, T t8, T t9) {
        ArrayList<T> a = new ArrayList<>(10);
        a.add(t1);
        a.add(t2);
        a.add(t3);
        a.add(t4);
        a.add(t5);
        a.add(t6);
        a.add(t7);
        a.add(t8);
        a.add(t9);
        return a;
    }


    /**
     * Create a list.
     *
     * @param t1  the first element
     * @param t2  the second element
     * @param t3  the third element
     * @param t4  the fourth element
     * @param t5  the fifth element
     * @param t6  the sixth element
     * @param t7  the seventh element
     * @param t8  the eighth element
     * @param t9  the ninth element
     * @param t10 the tenth element
     * @param <T> element type
     * @return new ArrayList
     */
    default <T> ArrayList<T> arrayList(T t1, T t2, T t3, T t4, T t5, T t6, T t7, T t8, T t9, T t10) {
        ArrayList<T> a = new ArrayList<>(11);
        a.add(t1);
        a.add(t2);
        a.add(t3);
        a.add(t4);
        a.add(t5);
        a.add(t6);
        a.add(t7);
        a.add(t8);
        a.add(t9);
        a.add(t10);
        return a;
    }

    /**
     * Map a list to another.
     *
     * @param list  which to handle
     * @param mapFn map function
     * @param <T>   element type of list
     * @param <R>   map result type
     * @return new list
     */
    default <T, R> List<R> map(List<T> list, Function<T, R> mapFn) {
        if (mapFn == null) {
            throw new IllegalArgumentException("Argument mapFn cannot be null");
        }

        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }

        List<R> result = new ArrayList<>(list.size());
        list.forEach(it -> result.add(mapFn.apply(it)));
        return result;
    }


    /**
     * {@link #zip(List, List, BiFunction) zip} two lists into a list of pairs.
     *
     * @param list1 first list
     * @param list2 second list
     * @param <E1>  element type of the first list
     * @param <E2>  element type of the second list
     * @return a list of pairs
     */
    default <E1, E2> List<Pair<E1, E2>> zip(List<E1> list1, List<E2> list2) {
        return zip(list1, list2, Pair::new);
    }


    /**
     * Combine the elements at the same position from two lists into one object in order.
     *
     * @param list1   first list
     * @param list2   second list
     * @param combine combine function.
     * @param <E1>    element type of the first list
     * @param <E2>    element type of the second list
     * @return a list of pairs
     */
    default <E1, E2, E> List<E> zip(List<E1> list1, List<E2> list2, BiFunction<E1, E2, E> combine) {
        if (combine == null) {
            throw new IllegalArgumentException("Argument combine cannot be null");
        }

        int l1 = length(list1);
        int l2 = length(list2);

        int state = (l1 > 0 ? 0b10 : 0b00) | (l2 > 0 ? 0b01 : 0b00);
        switch (state) {
            case 0b00:
                return new ArrayList<>();
            case 0b01:
                return map(list2, it -> combine.apply(null, it));
            case 0b10:
                return map(list1, it -> combine.apply(it, null));
            case 0b11: {
                int length = Math.max(l1, l2);
                List<E> result = new ArrayList<>(length);
                if (list1 instanceof RandomAccess && list2 instanceof RandomAccess) {
                    for (int i = 0; i < length; i++) {
                        result.add(combine.apply(
                                i < l1 ? list1.get(i) : null,
                                i < l2 ? list2.get(i) : null
                        ));
                    }
                } else {
                    Iterator<E1> itr1 = list1.listIterator();
                    Iterator<E2> itr2 = list2.listIterator();
                    for (int i = 0; i < length; i++) {
                        result.add(combine.apply(
                                itr1.hasNext() ? itr1.next() : null,
                                itr2.hasNext() ? itr2.next() : null
                        ));
                    }
                }
                return result;
            }
        }
        throw new Unreachable();
    }


}
