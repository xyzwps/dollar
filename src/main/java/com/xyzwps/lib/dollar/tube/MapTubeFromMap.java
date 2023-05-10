package com.xyzwps.lib.dollar.tube;

import com.xyzwps.lib.dollar.Pair;
import com.xyzwps.lib.dollar.iterator.MapEntryIterator;

import java.util.Map;

/**
 * A map tube.
 *
 * @param <K> key type
 * @param <V> value type
 */
public class MapTubeFromMap<K, V> extends MapTube<K, V> {

    private final MapEntryIterator<K, V> itr;

    /**
     * @param map never be null
     */
    public MapTubeFromMap(Map<K, V> map) {
        this.itr = new MapEntryIterator<>(map);
    }

    @Override
    public Pair<K, V> next() throws EndException {
        if (itr.hasNext()) {
            return itr.next();
        } else {
            throw new EndException();
        }
    }
}
