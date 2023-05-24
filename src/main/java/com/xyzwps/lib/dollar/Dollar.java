package com.xyzwps.lib.dollar;

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
    public static <T> ListStage<T> $(List<T> list) {
        return new ListStage<>(list);
    }


    /**
     * Create a tube from a map.
     *
     * @param map source map. Null is acceptable.
     * @param <K> map key type
     * @param <V> map value type
     * @return map tube
     */
    public static <K, V> MapStage<K, V> $(Map<K, V> map) {
        return new MapStage<>(map);
    }


    public static final DollarFunctions $ = DollarFunctions.INSTANCE;


    private Dollar() throws IllegalAccessException {
        throw new IllegalAccessException("???");
    }
}
