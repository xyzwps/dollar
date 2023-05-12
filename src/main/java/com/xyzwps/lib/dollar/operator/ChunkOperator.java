package com.xyzwps.lib.dollar.operator;

import com.xyzwps.lib.dollar.Tube;
import com.xyzwps.lib.dollar.tube.EndException;

import java.util.ArrayList;
import java.util.List;

/**
 * Used by chunk method.
 *
 * @param <T> element type
 */
public class ChunkOperator<T> implements Operator<T, List<T>> {

    private final int size;
    private boolean done = false;

    public ChunkOperator(int size) {
        if (size < 1) {
            throw new IllegalArgumentException("Chunk size should be greater than 0");
        }
        this.size = size;
    }

    @Override
    public List<T> next(Tube<T> upstream) throws EndException {
        if (done) {
            throw new EndException();
        }

        List<T> chunk = new ArrayList<>(this.size);
        for (int i = 0; i < size; i++) {
            try {
                chunk.add(upstream.next());
            } catch (EndException e) {
                if (chunk.isEmpty()) {
                    throw e;
                }
                this.done = true;
                return chunk;
            }
        }
        return chunk;
    }
}
