package com.xyzwps.dollar.operator;

import com.xyzwps.dollar.tube.Capsule;
import com.xyzwps.dollar.tube.Tube;

import java.util.ArrayList;
import java.util.List;

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
    public Capsule<List<T>> next(Tube<T> upstream) {
        if (done) {
            return Capsule.done();
        }

        List<T> chunk = new ArrayList<>(this.size);
        for (int i = 0; i < size; i++) {
            switch (upstream.next()) {
                case Capsule.Failure<T> failure -> {
                    return Capsule.failed(failure.cause());
                }
                case Capsule.Done<T> ignored -> {
                    if (chunk.isEmpty()) {
                        return Capsule.done();
                    } else {
                        this.done = true;
                        return Capsule.carry(chunk);
                    }
                }
                case Capsule.Carrier<T> carrier -> chunk.add(carrier.value());
            }
        }
        return Capsule.carry(chunk);
    }
}
