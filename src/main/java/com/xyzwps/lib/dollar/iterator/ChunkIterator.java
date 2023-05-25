package com.xyzwps.lib.dollar.iterator;


import java.util.*;

public class ChunkIterator<T> implements Iterator<List<T>> {

    private final int chunkSize;

    private final Iterator<T> up;

    public ChunkIterator(Iterator<T> up, int chunkSize) {
        if (chunkSize < 1) {
            throw new IllegalArgumentException("Each chunk should have at least one element.");
        }
        this.chunkSize = chunkSize;
        this.up = up == null ? EmptyIterator.create() : up;
    }

    @Override
    public boolean hasNext() {
        return this.up.hasNext();
    }

    @Override
    public List<T> next() {
        if (this.up.hasNext()) {
            List<T> list = new ArrayList<>(this.chunkSize);
            for (int i = 0; i < this.chunkSize && this.up.hasNext(); i++) {
                list.add(this.up.next());
            }
            return list;
        }
        throw new NoSuchElementException();
    }
}
