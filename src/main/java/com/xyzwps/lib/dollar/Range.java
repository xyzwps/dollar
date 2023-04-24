package com.xyzwps.lib.dollar;

import java.util.Iterator;

public final class Range {

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

    public Iterator<Integer> toIterator() {
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
