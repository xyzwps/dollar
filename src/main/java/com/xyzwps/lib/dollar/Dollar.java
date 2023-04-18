package com.xyzwps.lib.dollar;

import com.xyzwps.lib.dollar.tube.ListTube;
import com.xyzwps.lib.dollar.tube.ListTubeFromList;
import com.xyzwps.lib.dollar.tube.MapTube;
import com.xyzwps.lib.dollar.tube.MapTubeFromMap;

import java.util.*;

/**
 * Apis for you.
 * <p>
 * 用户 API。
 */
public final class Dollar {

    public static <T> ListTube<T> $(List<T> c) {
        return new ListTubeFromList<>(c);
    }

    public static <T> ListTube<T> $(T... a) {
        return new ListTubeFromList<>(Arrays.asList(a));
    }

    public static <K, V> MapTube<K, V> $(Map<K, V> m) {
        return new MapTubeFromMap<>(m);
    }

    public static class $ {
        public static <T> boolean isEmpty(List<T> list) {
            return list == null || list.isEmpty();
        }

        /**
         * Check value which is falsey or not. The values <code>null</code>, <code>false</code>,
         * <code>0(.0)</code> and <code>""</code> are falsey.
         *
         * @see ListTube#compact
         */
        public static boolean isFalsey(Object value) {
            return value == null || Objects.equals(value, false) || "".equals(value)
                    || Objects.equals(value, 0) || Objects.equals(value, 0L)
                    || Objects.equals(value, 0.0) || Objects.equals(value, 0.0f);
        }

        @SafeVarargs
        public static <T> List<T> list(T... elements) {
            return Arrays.asList(elements);
        }

        public static <K, V> HashMap<K, V> hashMap(K k, V v) {
            HashMap<K, V> map = new HashMap<>();
            map.put(k, v);
            return map;
        }

        public static <K, V> HashMap<K, V> hashMap(K k1, V v1, K k2, V v2) {
            HashMap<K, V> map = new HashMap<>();
            map.put(k1, v1);
            map.put(k2, v2);
            return map;
        }

        public static <K, V> HashMap<K, V> hashMap(K k1, V v1, K k2, V v2, K k3, V v3) {
            HashMap<K, V> map = new HashMap<>();
            map.put(k1, v1);
            map.put(k2, v2);
            map.put(k3, v3);
            return map;
        }

        public static <K, V> HashMap<K, V> hashMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
            HashMap<K, V> map = new HashMap<>();
            map.put(k1, v1);
            map.put(k2, v2);
            map.put(k3, v3);
            map.put(k4, v4);
            return map;
        }
    }

    private Dollar() throws IllegalAccessException {
        throw new IllegalAccessException("???");
    }
}
