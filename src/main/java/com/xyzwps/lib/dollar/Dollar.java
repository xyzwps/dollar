package com.xyzwps.lib.dollar;

import com.xyzwps.lib.dollar.function.ObjIntFunction;
import com.xyzwps.lib.dollar.iterable.ArrayIterable;
import com.xyzwps.lib.dollar.iterable.EmptyIterable;
import com.xyzwps.lib.dollar.iterable.Range;

import java.util.*;
import java.util.function.*;

/**
 * Apis for you.
 * <p>
 * TODO: 重新测试，测试和 *stage 对齐
 */
public final class Dollar {

    /**
     * Create a stage chain from a {@link List}.
     *
     * @param list source list. Null is acceptable.
     * @param <T>  list element type
     * @return a list stage
     */
    public static <T> ListStage<T> $(List<T> list) {
        return new ListStage<>(list);
    }


    /**
     * Create a stage chain from a map.
     *
     * @param map source map. Null is acceptable.
     * @param <K> map key type
     * @param <V> map value type
     * @return a map stage
     */
    public static <K, V> MapStage<K, V> $(Map<K, V> map) {
        return new MapStage<>(map);
    }


    public static class $ {
        /**
         * Check if the collection is empty or not.
         *
         * @param collection to be checked
         * @param <T>        type of elements
         * @return true if collection is null or has no elements
         */
        public static <T> boolean isEmpty(Collection<T> collection) {
            return collection == null || collection.isEmpty();
        }


        /**
         * Check if the collection is not empty.
         *
         * @param collection to be checked
         * @param <T>        type of elements
         * @return true if collection {@link #isEmpty(Collection)} is false
         */
        public static <T> boolean isNotEmpty(Collection<T> collection) {
            return !isEmpty(collection);
        }

        /**
         * Count the element of a <code>collection</code>.
         * Return 0 if <code>collection</code> is <code>null</code>.
         *
         * @param collection which to handle
         * @param <E>        collection element type
         * @return count of elements in collection
         */
        public static <E> int length(Collection<E> collection) {
            return collection == null ? 0 : collection.size();
        }


        /**
         * Checks <code>value</code> to determine whether a public static value
         * should be returned in its place. The <code>defaultValue</code>
         * is returned when <code>value</code> is <code>null</code>.
         *
         * @param value        The value to check
         * @param defaultValue The public static value
         * @param <T>          value type
         * @return resolved value
         */
        public static <T> T defaultTo(T value, T defaultValue) {
            return value == null ? defaultValue : value;
        }


        /**
         * Check value which is falsey or not. The values <code>null</code>, <code>false</code>,
         * <code>0(.0)</code> and <code>""</code> are falsey.
         *
         * @param value value to be checked
         * @return true if the value is falsey
         * @see #compact
         */
        public static boolean isFalsey(Object value) {
            return value == null
                    || Objects.equals(value, false)
                    || "".equals(value)
                    || Objects.equals(value, 0)
                    || Objects.equals(value, 0L)
                    || Objects.equals(value, 0.0)
                    || Objects.equals(value, 0.0f);
        }

        /**
         * Creates a list of elements split into groups the length of size.
         * If list can't be split evenly, the final chunk will be the remaining elements.
         *
         * @param list The list to handle
         * @param size Chunk size which should be greater than 0.
         * @param <T>  Element type
         * @return new list of chunks
         */
        public static <T> List<List<T>> chunk(List<T> list, int size) {
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
        public static <T> List<T> compact(List<T> list) {
            return filter(list, it -> !isFalsey(it));
        }


        /**
         * Creates a new list which concatenating all lists in order.
         *
         * @param lists The lists to concatenate
         * @param <T>   Element type
         * @return concatenated new list
         */
        @SafeVarargs
        public static <T> List<T> concat(List<T>... lists) {
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
        public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
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
        public static <T> List<T> filter(List<T> list, BiPredicate<T, Integer> predicate) {
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
         * @param args TODO: 添加描述
         * @param <T>  element type
         * @return new ArrayList
         */
        @SafeVarargs
        public static <T> ArrayList<T> list(T... args) {
            List<T> list = Arrays.asList(args);
            return list instanceof ArrayList ? (ArrayList<T>) list : new ArrayList<>(list);
        }


        // TODO: 添加 doc
        public static <T> ArrayList<T> listFrom(Iterator<T> itr) {
            ArrayList<T> list = new ArrayList<>();
            if (itr != null) {
                while (itr.hasNext()) list.add(itr.next());
            }
            return list;
        }

        /**
         * Map a list to another.
         *
         * @param iterable which to handle
         * @param mapFn    map function
         * @param <T>      element type of list
         * @param <R>      map result type
         * @return new list
         */
        public static <T, R> ArrayList<R> map(Iterable<T> iterable, Function<T, R> mapFn) {
            Objects.requireNonNull(mapFn);

            if (iterable == null) {
                return new ArrayList<>();
            }

            int capacity = 16;
            if (iterable instanceof List) {
                List<T> list = (List<T>) iterable;
                capacity = list.size();
            }

            ArrayList<R> result = new ArrayList<>(capacity);
            for (T t : iterable) {
                result.add(mapFn.apply(t));
            }
            return result;
        }

        // TODO: add doc
        public static <T, R> ArrayList<R> map(Iterable<T> iterable, ObjIntFunction<T, R> mapFn) {
            Objects.requireNonNull(mapFn);

            if (iterable == null) {
                return new ArrayList<>();
            }

            int capacity = 16;
            if (iterable instanceof List) {
                List<T> list = (List<T>) iterable;
                capacity = list.size();
            }

            ArrayList<R> result = new ArrayList<>(capacity);
            int index = 0;
            for (T t : iterable) {
                result.add(mapFn.apply(t, index++));
            }
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
        public static <E1, E2> List<Pair<E1, E2>> zip(List<E1> list1, List<E2> list2) {
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
        public static <E1, E2, E> List<E> zip(List<E1> list1, List<E2> list2, BiFunction<E1, E2, E> combine) {
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


        /**
         * Create a {@link HashMap} with key-value pairs.
         *
         * @param <K> key type
         * @param <V> value type
         * @return new HashMap
         */
        public static <K, V> HashMap<K, V> hashMap() {
            return new HashMap<>();
        }


        /**
         * Create a {@link HashMap} with key-value pairs.
         *
         * @param k1  the first key
         * @param v1  the first value
         * @param <K> key type
         * @param <V> value type
         * @return new HashMap
         */
        public static <K, V> HashMap<K, V> hashMap(K k1, V v1) {
            HashMap<K, V> map = new HashMap<>();
            map.put(k1, v1);
            return map;
        }


        /**
         * Create a {@link HashMap} with key-value pairs.
         *
         * @param k1  the first key
         * @param v1  the first value
         * @param k2  the second key
         * @param v2  the second value
         * @param <K> key type
         * @param <V> value type
         * @return new HashMap
         */
        public static <K, V> HashMap<K, V> hashMap(K k1, V v1, K k2, V v2) {
            HashMap<K, V> map = new HashMap<>();
            map.put(k1, v1);
            map.put(k2, v2);
            return map;
        }


        /**
         * Create a {@link HashMap} with key-value pairs.
         *
         * @param k1  the first key
         * @param v1  the first value
         * @param k2  the second key
         * @param v2  the second value
         * @param k3  the third key
         * @param v3  the third value
         * @param <K> key type
         * @param <V> value type
         * @return new HashMap
         */
        public static <K, V> HashMap<K, V> hashMap(K k1, V v1, K k2, V v2, K k3, V v3) {
            HashMap<K, V> map = new HashMap<>();
            map.put(k1, v1);
            map.put(k2, v2);
            map.put(k3, v3);
            return map;
        }


        /**
         * Create a {@link HashMap} with key-value pairs.
         *
         * @param k1  the first key
         * @param v1  the first value
         * @param k2  the second key
         * @param v2  the second value
         * @param k3  the third key
         * @param v3  the third value
         * @param k4  the fourth key
         * @param v4  the fourth value
         * @param <K> key type
         * @param <V> value type
         * @return new HashMap
         */
        public static <K, V> HashMap<K, V> hashMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
            HashMap<K, V> map = new HashMap<>();
            map.put(k1, v1);
            map.put(k2, v2);
            map.put(k3, v3);
            map.put(k4, v4);
            return map;
        }


        /**
         * Create a {@link HashMap} with key-value pairs.
         *
         * @param k1  the first key
         * @param v1  the first value
         * @param k2  the second key
         * @param v2  the second value
         * @param k3  the third key
         * @param v3  the third value
         * @param k4  the fourth key
         * @param v4  the fourth value
         * @param k5  the fifth key
         * @param v5  the fifth value
         * @param <K> key type
         * @param <V> value type
         * @return new HashMap
         */
        public static <K, V> HashMap<K, V> hashMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
            HashMap<K, V> map = new HashMap<>();
            map.put(k1, v1);
            map.put(k2, v2);
            map.put(k3, v3);
            map.put(k4, v4);
            map.put(k5, v5);
            return map;
        }


        /**
         * Create a {@link HashMap} with key-value pairs.
         *
         * @param k1  the first key
         * @param v1  the first value
         * @param k2  the second key
         * @param v2  the second value
         * @param k3  the third key
         * @param v3  the third value
         * @param k4  the fourth key
         * @param v4  the fourth value
         * @param k5  the fifth key
         * @param v5  the fifth value
         * @param k6  the sixth key
         * @param v6  the sixth value
         * @param <K> key type
         * @param <V> value type
         * @return new HashMap
         */
        public static <K, V> HashMap<K, V> hashMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
            HashMap<K, V> map = new HashMap<>();
            map.put(k1, v1);
            map.put(k2, v2);
            map.put(k3, v3);
            map.put(k4, v4);
            map.put(k5, v5);
            map.put(k6, v6);
            return map;
        }


        /**
         * Create a {@link HashMap} with key-value pairs.
         *
         * @param k1  the first key
         * @param v1  the first value
         * @param k2  the second key
         * @param v2  the second value
         * @param k3  the third key
         * @param v3  the third value
         * @param k4  the fourth key
         * @param v4  the fourth value
         * @param k5  the fifth key
         * @param v5  the fifth value
         * @param k6  the sixth key
         * @param v6  the sixth value
         * @param k7  the seventh key
         * @param v7  the seventh value
         * @param <K> key type
         * @param <V> value type
         * @return new HashMap
         */
        public static <K, V> HashMap<K, V> hashMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
            HashMap<K, V> map = new HashMap<>();
            map.put(k1, v1);
            map.put(k2, v2);
            map.put(k3, v3);
            map.put(k4, v4);
            map.put(k5, v5);
            map.put(k6, v6);
            map.put(k7, v7);
            return map;
        }


        /**
         * Create a {@link HashMap} with key-value pairs.
         *
         * @param k1  the first key
         * @param v1  the first value
         * @param k2  the second key
         * @param v2  the second value
         * @param k3  the third key
         * @param v3  the third value
         * @param k4  the fourth key
         * @param v4  the fourth value
         * @param k5  the fifth key
         * @param v5  the fifth value
         * @param k6  the sixth key
         * @param v6  the sixth value
         * @param k7  the seventh key
         * @param v7  the seventh value
         * @param k8  the eighth key
         * @param v8  the eighth value
         * @param <K> key type
         * @param <V> value type
         * @return new HashMap
         */
        public static <K, V> HashMap<K, V> hashMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8) {
            HashMap<K, V> map = new HashMap<>();
            map.put(k1, v1);
            map.put(k2, v2);
            map.put(k3, v3);
            map.put(k4, v4);
            map.put(k5, v5);
            map.put(k6, v6);
            map.put(k7, v7);
            map.put(k8, v8);
            return map;
        }


        /**
         * Create a {@link HashMap} with key-value pairs.
         *
         * @param k1  the first key
         * @param v1  the first value
         * @param k2  the second key
         * @param v2  the second value
         * @param k3  the third key
         * @param v3  the third value
         * @param k4  the fourth key
         * @param v4  the fourth value
         * @param k5  the fifth key
         * @param v5  the fifth value
         * @param k6  the sixth key
         * @param v6  the sixth value
         * @param k7  the seventh key
         * @param v7  the seventh value
         * @param k8  the eighth key
         * @param v8  the eighth value
         * @param k9  the ninth key
         * @param v9  the ninth value
         * @param <K> key type
         * @param <V> value type
         * @return new HashMap
         */
        public static <K, V> HashMap<K, V> hashMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9) {
            HashMap<K, V> map = new HashMap<>();
            map.put(k1, v1);
            map.put(k2, v2);
            map.put(k3, v3);
            map.put(k4, v4);
            map.put(k5, v5);
            map.put(k6, v6);
            map.put(k7, v7);
            map.put(k8, v8);
            map.put(k9, v9);
            return map;
        }


        /**
         * Create a {@link HashMap} with key-value pairs.
         *
         * @param k1  the first key
         * @param v1  the first value
         * @param k2  the second key
         * @param v2  the second value
         * @param k3  the third key
         * @param v3  the third value
         * @param k4  the fourth key
         * @param v4  the fourth value
         * @param k5  the fifth key
         * @param v5  the fifth value
         * @param k6  the sixth key
         * @param v6  the sixth value
         * @param k7  the seventh key
         * @param v7  the seventh value
         * @param k8  the eighth key
         * @param v8  the eighth value
         * @param k9  the ninth key
         * @param v9  the ninth value
         * @param k10 the tenth key
         * @param v10 the tenth value
         * @param <K> key type
         * @param <V> value type
         * @return new HashMap
         */
        public static <K, V> HashMap<K, V> hashMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10) {
            HashMap<K, V> map = new HashMap<>();
            map.put(k1, v1);
            map.put(k2, v2);
            map.put(k3, v3);
            map.put(k4, v4);
            map.put(k5, v5);
            map.put(k6, v6);
            map.put(k7, v7);
            map.put(k8, v8);
            map.put(k9, v9);
            map.put(k10, v10);
            return map;
        }

        /**
         * Check if the string is empty or not.
         *
         * @param string to be checked
         * @return true if string is null, or it's length is 0
         */
        public static boolean isEmpty(String string) {
            return string == null || string.isEmpty();
        }

        // TODO: 写 doc
        public static boolean isEmpty(Map<?, ?> map) {
            return map == null || map.isEmpty();
        }

        // TODO: 写 doc
        public static boolean isNotEmpty(Map<?, ?> map) {
            return !isEmpty(map);
        }


        /**
         * Check if the string is not empty.
         *
         * @param string to be checked
         * @return true if string {@link #isEmpty(String)} is false
         */
        public static boolean isNotEmpty(String string) {
            return !isEmpty(string);
        }


        /**
         * Pads <code>string</code> on the left and right sides if it's shorter than <code>length</code>.
         * Padding characters are truncated if they can't be evenly divided by <code>length</code>.
         *
         * @param string The string to pad
         * @param length The padding length
         * @param chars  The string used as padding
         * @return Padded string
         */
        public static String pad(String string, int length, String chars) {
            if (length < 0) {
                throw new IllegalArgumentException("Argument length cannot be less than 0");
            }

            string = defaultTo(string, "");
            if (string.length() >= length) {
                return string;
            }

            char[] padChars = (isEmpty(chars) ? " " : chars).toCharArray();
            StringBuilder sb = new StringBuilder();
            int padLength = length - string.length();
            int padHalf = padLength / 2;
            for (int i = 0; i < padHalf; i++) {
                sb.append(padChars[i % padChars.length]);
            }
            sb.append(string);
            for (int i = padHalf; i < padLength; i++) {
                sb.append(padChars[i % padChars.length]);
            }
            return sb.toString();

        }

        /**
         * Pads <code>string</code> on the right side if it's shorter than <code>length</code>.
         * Padding characters are truncated if they exceed <code>length</code>.
         *
         * @param string The string to pad
         * @param length The padding length
         * @param chars  The string used as padding
         * @return Padded string
         */
        public static String padEnd(String string, int length, String chars) {
            if (length < 0) {
                throw new IllegalArgumentException("Argument length cannot be less than 0");
            }

            string = defaultTo(string, "");
            if (string.length() >= length) {
                return string;
            }

            char[] padChars = (isEmpty(chars) ? " " : chars).toCharArray();
            StringBuilder sb = new StringBuilder(string);
            int padLength = length - string.length();
            for (int i = 0; i < padLength; i++) {
                sb.append(padChars[i % padChars.length]);
            }
            return sb.toString();
        }

        /**
         * Pads <code>string</code> on the left side if it's shorter than <code>length</code>.
         * Padding characters are truncated if they exceed <code>length</code>.
         *
         * @param string The string to pad
         * @param length The padding length
         * @param chars  The string used as padding
         * @return Padded string
         */
        public static String padStart(String string, int length, String chars) {
            if (length < 0) {
                throw new IllegalArgumentException("Argument length cannot be less than 0");
            }

            string = defaultTo(string, "");
            if (string.length() >= length) {
                return string;
            }

            char[] padChars = (isEmpty(chars) ? " " : chars).toCharArray();
            StringBuilder sb = new StringBuilder();
            int padLength = length - string.length();
            for (int i = 0; i < padLength; i++) {
                sb.append(padChars[i % padChars.length]);
            }
            sb.append(string);
            return sb.toString();
        }

        /**
         * Create an empty list stage.
         *
         * @param <T> element type
         * @return list stage
         */
        public static <T> ListStage<T> empty() {
            return new ListStage<T>(EmptyIterable.create());
        }


        /**
         * Create a stage from elements.
         *
         * @param args TODO: 添加描述
         * @param <T>  element type
         * @return list stage
         */
        @SafeVarargs
        public static <T> ListStage<T> just(T... args) {
            return new ListStage<T>(new ArrayIterable<>(args));
        }


        /**
         * Handle a range.
         *
         * @param start range start - included
         * @param end   range end - excluded
         * @return list stage
         */
        public static ListStage<Integer> range(int start, int end) {
            return new ListStage<>(new Range(start, end));
        }

        /**
         * TODO: 写 doc
         *
         * @param iterable
         * @param <T>
         * @return
         */
        public static <T> Optional<T> first(Iterable<T> iterable) {
            if (iterable == null) {
                return Optional.empty();
            }
            if (iterable instanceof List) {
                List<T> list = (List<T>) iterable;
                return list.isEmpty() ? Optional.empty() : Optional.ofNullable(list.get(0));
            }
            Iterator<T> itr = iterable.iterator();
            return itr.hasNext() ? Optional.ofNullable(itr.next()) : Optional.empty();
        }

        /**
         * TODO: 写 doc
         *
         * @param iterable
         * @param <T>
         * @return
         */
        public static <T> Optional<T> head(Iterable<T> iterable) {
            return first(iterable);
        }

        /**
         * TODO: 写 doc
         *
         * @param flatMapFn which map an element to an {@link Iterable}
         * @param <R>       flatted elements type
         * @return next stage
         */
        public static <T, R> ArrayList<R> flatMap(Iterable<T> iterable, Function<T, Iterable<R>> flatMapFn) {
            Objects.requireNonNull(flatMapFn);

            if (iterable == null) {
                return new ArrayList<>();
            }

            ArrayList<R> result = new ArrayList<>();
            for (T t : iterable) {
                Iterable<R> itr = flatMapFn.apply(t);
                if (itr != null) {
                    itr.forEach(result::add);
                }
            }
            return result;
        }

        // TODO: 写 doc
        public static <K, V, V2> HashMap<K, V2> mapValues(Map<K, V> map, Function<V, V2> mapFn) {
            Objects.requireNonNull(mapFn);

            if (isEmpty(map)) {
                return new HashMap<>();
            }

            HashMap<K, V2> result = new HashMap<>();
            map.forEach((k, v) -> result.put(k, mapFn.apply(v)));
            return result;
        }

        // TODO: 写 doc
        public static <K, V, V2> HashMap<K, V2> mapValues(Map<K, V> map, BiFunction<V, K, V2> mapFn) {
            Objects.requireNonNull(mapFn);

            if (isEmpty(map)) {
                return new HashMap<>();
            }

            HashMap<K, V2> result = new HashMap<>();
            map.forEach((k, v) -> result.put(k, mapFn.apply(v, k)));
            return result;
        }

        // TODO: 写 doc
        public static <K, V, K2> HashMap<K2, V> mapKeys(Map<K, V> map, Function<K, K2> mapFn) {
            Objects.requireNonNull(mapFn);

            if (isEmpty(map)) {
                return new HashMap<>();
            }

            HashMap<K2, V> result = new HashMap<>();
            map.forEach((k, v) -> result.put(mapFn.apply(k), v));
            return result;
        }

        // TODO: 写 doc
        public static <K, V, K2> HashMap<K2, V> mapKeys(Map<K, V> map, BiFunction<K, V, K2> mapFn) {
            Objects.requireNonNull(mapFn);

            if (isEmpty(map)) {
                return new HashMap<>();
            }

            HashMap<K2, V> result = new HashMap<>();
            map.forEach((k, v) -> result.put(mapFn.apply(k, v), v));
            return result;
        }

        // TODO: 写 doc
        // 如果 key 冲突，取最先遇到的那个
        public static <T, K> HashMap<K, T> keyBy(Iterable<T> iterable, Function<T, K> toKey) {
            Objects.requireNonNull(toKey);

            if (iterable == null) {
                return new HashMap<>();
            }

            HashMap<K, T> result = new HashMap<>();
            iterable.forEach(it -> result.computeIfAbsent(toKey.apply(it), k -> it));
            return result;
        }

        // TODO: 写 doc
        public static <T, K> HashMap<K, ArrayList<T>> groupBy(Iterable<T> iterable, Function<T, K> toKey) {
            Objects.requireNonNull(toKey);

            if (iterable == null) {
                return new HashMap<>();
            }

            HashMap<K, ArrayList<T>> result = new HashMap<>();
            iterable.forEach(it -> result.computeIfAbsent(toKey.apply(it), k -> new ArrayList<>()).add(it));
            return result;
        }

        // TODO: orderBy

        // TODO: 写 doc
        public static <T, R> R reduce(Iterable<T> iterable, R identity, BiFunction<R, T, R> reducer) {
            Objects.requireNonNull(reducer);

            if (iterable == null) {
                return identity;
            }

            R result = identity;
            for (T t : iterable) {
                result = reducer.apply(result, t);
            }
            return result;
        }

        // TODO: 写 doc
        public static <T> ArrayList<T> reverse(Iterable<T> iterable) {
            ArrayList<T> list = reduce(iterable, new ArrayList<>(), (li, it) -> {
                li.add(it);
                return li;
            });

            int last = list.size() - 1;
            int half = list.size() / 2;
            for (int i = 0; i < half; i++) {
                T t1 = list.get(i);
                T t2 = list.get(last - i);
                list.set(last - i, t1);
                list.set(i, t2);
            }
            return list;
        }

        // TODO: 写 doc
        public static <T> ArrayList<T> take(Iterable<T> iterable, int n) {
            if (n < 1) {
                throw new IllegalArgumentException("You should take at least one element.");
            }

            if (iterable == null) {
                return new ArrayList<>();
            }

            ArrayList<T> list = new ArrayList<>();
            int i = 0;
            for (T t : iterable) {
                if (i < n) {
                    list.add(t);
                    i++;
                } else {
                    break;
                }
            }
            return list;
        }

        // TODO: 写 doc
        public static <T> ArrayList<T> takeWhile(Iterable<T> iterable, Predicate<T> predicate) {
            Objects.requireNonNull(predicate);

            if (iterable == null) {
                return new ArrayList<>();
            }

            ArrayList<T> list = new ArrayList<>();
            for (T t : iterable) {
                if (predicate.test(t)) {
                    list.add(t);
                } else {
                    break;
                }
            }
            return list;
        }

        // TODO: 写 doc
        public static <T> HashSet<T> toSet(Iterable<T> iterable) {
            if (iterable == null) {
                return new HashSet<>();
            }

            HashSet<T> set = new HashSet<>();
            iterable.forEach(set::add);
            return set;
        }

        // TODO: 写 doc
        public static <T> void forEach(Iterable<T> iterable, Consumer<T> handler) {
            if (iterable == null) {
                return;
            }

            iterable.forEach(handler::accept);
        }

        // TODO: 写 doc
        public static <T> void forEach(Iterable<T> iterable, ObjIntConsumer<T> handler) {
            if (iterable == null) {
                return;
            }

            int i = 0;
            for (T it : iterable) {
                handler.accept(it, i++);
            }
        }

        // TODO: unique
        // TODO: uniqueBy
        // TODO: zip
    }


    private Dollar() throws IllegalAccessException {
        throw new IllegalAccessException("???");
    }
}
