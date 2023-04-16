package com.xyzwps.dollar.tube;

public sealed interface Capsule<T> {

    static <T> Done<T> done() {
        return new Done<>();
    }

    static <T> Failure<T> failed(Throwable t) {
        return new Failure<>(t);
    }

    static <T> Carrier<T> carry(T value) {
        return new Carrier<>(value);
    }

    record Done<T>() implements Capsule<T> {
    }

    record Failure<T>(Throwable cause) implements Capsule<T> {
    }

    record Carrier<T>(T value) implements Capsule<T> {
    }
}
