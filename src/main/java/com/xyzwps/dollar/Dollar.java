package com.xyzwps.dollar;

import com.xyzwps.dollar.tube.ListTube;
import com.xyzwps.dollar.tube.ListTubeFromList;
import com.xyzwps.dollar.tube.MapTube;
import com.xyzwps.dollar.tube.MapTubeFromMap;

import java.util.Arrays;
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
    }

    private Dollar() throws IllegalAccessException {
        throw new IllegalAccessException("???");
    }
}
