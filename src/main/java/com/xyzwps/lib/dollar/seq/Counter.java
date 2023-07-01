package com.xyzwps.lib.dollar.seq;

final class Counter {
    private int count = 0;

    Counter(int init) {
        this.count = init;
    }

    int getAndIncr() {
        return count++;
    }
}
