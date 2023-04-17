package com.xyzwps.dollar;

import com.xyzwps.dollar.tube.ListTube;
import com.xyzwps.dollar.tube.ListTubeFromList;
import com.xyzwps.dollar.tube.MapTube;
import com.xyzwps.dollar.tube.MapTubeFromMap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户 API。
 * <p>
 * Apis for you.
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

        public static <K, V> HashMap<K, V> newMap(K k, V v) {
            HashMap<K, V> map = new HashMap<>();
            map.put(k, v);
            return map;
        }

        public static <K, V> HashMap<K, V> newMap(K k1, V v1, K k2, V v2) {
            HashMap<K, V> map = new HashMap<>();
            map.put(k1, v1);
            map.put(k2, v2);
            return map;
        }

        public static <K, V> HashMap<K, V> newMap(K k1, V v1, K k2, V v2, K k3, V v3) {
            HashMap<K, V> map = new HashMap<>();
            map.put(k1, v1);
            map.put(k2, v2);
            map.put(k3, v3);
            return map;
        }

        public static <K, V> HashMap<K, V> newMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
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
