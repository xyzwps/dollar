package com.xyzwps.lib.dollar;

import com.xyzwps.lib.dollar.tube.ListTube;
import com.xyzwps.lib.dollar.tube.ListTubeFromList;
import com.xyzwps.lib.dollar.tube.MapTube;
import com.xyzwps.lib.dollar.tube.MapTubeFromMap;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * Apis for you.
 */
public final class Dollar {

    /**
     * Create a tube from a {@link List}.
     *
     * @param list source list. Null is acceptable.
     * @param <T>  list element type
     * @return list tube
     */
    public static <T> ListTube<T> $(List<T> list) {
        return new ListTubeFromList<>(list);
    }


    /**
     * Create a tube from an array.
     *
     * @param a   source array
     * @param <T> array element type
     * @return list tube
     */
    @SafeVarargs
    public static <T> ListTube<T> $(T... a) {
        return new ListTubeFromList<>(Arrays.asList(a));
    }


    /**
     * Create a tube from a map.
     *
     * @param map source map. Null is acceptable.
     * @param <K> map key type
     * @param <V> map value type
     * @return map tube
     */
    public static <K, V> MapTube<K, V> $(Map<K, V> map) {
        return new MapTubeFromMap<>(map);
    }


    /**
     * Dollar simple utilities.
     */
    public static final class $ {

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

            if ($.isEmpty(list)) {
                return new ArrayList<>();
            }

            // TODO: handle RandomAccess

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
         * @param predicate Predicate function. Null will be considered as always true.
         * @param <T>       Element type
         * @return new filtered list
         */
        public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
            //noinspection unchecked
            Predicate<T> p = predicate == null ? Utils.ALWAYS_TRUE_PREDICATE : predicate;
            return filter(list, (e, i) -> p.test(e));
        }


        /**
         * Iterate over the list and retaining the elements which are predicated true.
         *
         * @param list      The list to iterate. Null is acceptable.
         * @param predicate Predicate function with element index. Null will be considered as always true.
         * @param <T>       Element type
         * @return new filtered list
         */
        public static <T> List<T> filter(List<T> list, BiPredicate<T, Integer> predicate) {
            if (list == null) {
                return new ArrayList<>();
            }

            //noinspection unchecked
            BiPredicate<T, Integer> p = predicate == null ? Utils.ALWAYS_TRUE_BIPREDICATE : predicate;

            List<T> result = new ArrayList<>();
            int i = 0;
            for (T element : list) {
                if (p.test(element, i++)) {
                    result.add(element);
                }
            }
            return result;
        }

        /**
         * Create a {@link HashMap} with a key-value pair.
         *
         * @param k   pair key
         * @param v   pair value
         * @param <K> key type
         * @param <V> value type
         * @return new HashMap
         */
        public static <K, V> HashMap<K, V> hashMap(K k, V v) {
            HashMap<K, V> map = new HashMap<>();
            map.put(k, v);
            return map;
        }


        /**
         * Create a {@link HashMap} with two key-value pairs.
         *
         * @param k1  the first pair key
         * @param v1  the first pair value
         * @param k2  the second pair key
         * @param v2  the second pair value
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
         * Create a {@link HashMap} with three key-value pairs.
         *
         * @param k1  the first pair key
         * @param v1  the first pair value
         * @param k2  the second pair key
         * @param v2  the second pair value
         * @param k3  the third pair key
         * @param v3  the third pair value
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
         * Create a {@link HashMap} with four key-value pairs.
         *
         * @param k1  the first pair key
         * @param v1  the first pair value
         * @param k2  the second pair key
         * @param v2  the second pair value
         * @param k3  the third pair key
         * @param v3  the third pair value
         * @param k4  the fourth pair key
         * @param v4  the fourth pair value
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
         * Check if the list is empty or not.
         *
         * @param list to be checked
         * @param <T>  list element type
         * @return true if list is null or list has no elements
         */
        public static <T> boolean isEmpty(List<T> list) {
            return list == null || list.isEmpty();
        }


        /**
         * Check value which is falsey or not. The values <code>null</code>, <code>false</code>,
         * <code>0(.0)</code> and <code>""</code> are falsey.
         *
         * @param value value to be checked
         * @return true if the value is falsey
         * @see ListTube#compact
         */
        public static boolean isFalsey(Object value) {
            return value == null || Objects.equals(value, false) || "".equals(value)
                    || Objects.equals(value, 0) || Objects.equals(value, 0L)
                    || Objects.equals(value, 0.0) || Objects.equals(value, 0.0f);
        }


        /**
         * Check if the list is not empty.
         *
         * @param list to be checked
         * @param <T>  list element type
         * @return true if list {@link #isEmpty(List)} is false
         */
        public static <T> boolean isNotEmpty(List<T> list) {
            return !isEmpty(list);
        }


        /**
         * Create a list.
         *
         * @param elements elements of list
         * @param <T>      element type
         * @return new list
         */
        @SafeVarargs
        public static <T> List<T> list(T... elements) {
            return Arrays.asList(elements);
        }


        private $() throws IllegalAccessException {
            throw new IllegalAccessException("???");
        }

    }

    private Dollar() throws IllegalAccessException {
        throw new IllegalAccessException("???");
    }
}
