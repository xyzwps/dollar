package com.xyzwps.lib.dollar;

import com.xyzwps.lib.dollar.iterator.EmptyIterator;
import com.xyzwps.lib.dollar.tube.*;

import java.util.*;

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
        return new ListTubeFromIterator<>(list == null ? EmptyIterator.create() : list.iterator());
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
        return new MapTubeFromMap<>(map == null ? new HashMap<>() : map);
    }


    public static final DollarFunctions $ = DollarFunctions.INSTANCE;


    private Dollar() throws IllegalAccessException {
        throw new IllegalAccessException("???");
    }
}
