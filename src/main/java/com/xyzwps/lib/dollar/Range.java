package com.xyzwps.lib.dollar;

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class Range implements Iterable<Integer> {

    /**
     * Include
     */
    private final int start;

    /**
     * Exclude
     */
    private final int end;

    public Range(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {

            private int current = start;

            // TODO: 测试

            @Override
            public boolean hasNext() {
                return current < end;
            }

            @Override
            public Integer next() {
                if (hasNext()) {
                    return current++;
                }
                throw new NoSuchElementException();
            }
        };
    }
}
