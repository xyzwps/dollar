package com.xyzwps.lib.dollar.tube;

import com.xyzwps.lib.dollar.Utils;

import java.util.Iterator;
import java.util.List;

/**
 * TODO: remove
 *
 * @param <T>
 */
public class ListTubeFromList<T> extends ListTube<T> {

    private final Iterator<T> iterator;

    public ListTubeFromList(List<T> list) {
        this.iterator = list == null ? Utils.emptyIterator() : list.iterator();
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
