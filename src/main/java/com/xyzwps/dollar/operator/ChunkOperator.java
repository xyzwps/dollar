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
            Capsule<T> c = upstream.next();
            if (c instanceof Capsule.Done) {
                if (chunk.isEmpty()) {
                    return Capsule.done();
                } else {
                    this.done = true;
                    return Capsule.carry(chunk);
                }
            } else if (c instanceof Capsule.Failure) {
                return (Capsule<List<T>>) c;
            } else if (c instanceof Capsule.Carrier) {
                chunk.add(((Capsule.Carrier<T>) c).value());
            } else throw new Capsule.UnknownCapsuleException();
        }
        return Capsule.carry(chunk);
    }
}
