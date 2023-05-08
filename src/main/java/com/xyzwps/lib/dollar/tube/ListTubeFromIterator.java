package com.xyzwps.lib.dollar.tube;

import java.util.Iterator;

/**
 * A list tube created from {@link Iterator}.
 *
 * @param <T> type of iterated elements
 */
public class ListTubeFromIterator<T> extends ListTube<T> {

    private final Iterator<T> iterator;

    /**
     * @param iterator never be null
     */
    public ListTubeFromIterator(Iterator<T> iterator) {
        this.iterator = iterator;
    }

    @Override
    public Capsule<T> next() {
        if (iterator.hasNext()) {
            return Capsule.carry(iterator.next());
        } else {
            return Capsule.done();
        }
    }
}
