package com.xyzwps.lib.dollar.tube;

import java.util.Iterator;
import java.util.Objects;

public class ListTubeFromIterator<T> extends ListTube<T> {

    private final Iterator<T> iterator;

    public ListTubeFromIterator(Iterator<T> iterator) {
        this.iterator = Objects.requireNonNull(iterator);
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
