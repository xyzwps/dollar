package com.xyzwps.lib.dollar;

import java.util.Iterator;

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

            @Override
            public boolean hasNext() {
                return current < end;
            }

            @Override
            public Integer next() {
                return current++;
            }
        };
    }
}
