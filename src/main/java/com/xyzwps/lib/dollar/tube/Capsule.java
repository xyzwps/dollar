package com.xyzwps.lib.dollar.tube;

import java.util.function.Function;

public interface Capsule<T> {

    static <T> Done<T> done() {
        return new Done<>();
    }

    static <T> Carrier<T> carry(T value) {
        return new Carrier<>(value);
    }

    class Done<T> implements Capsule<T> {
    }

    class Carrier<T> implements Capsule<T> {

        private final T value;

        public Carrier(T value) {
            this.value = value;
        }

        public T value() {
            return this.value;
        }
    }

    static <T, R> R map(Capsule<T> c,
                        Function<Capsule.Carrier<T>, R> mapItemFn,
                        Function<Capsule.Done<T>, R> mapDoneFn) {
        if (c instanceof Capsule.Done) {
            return mapDoneFn.apply((Done<T>) c);
        } else if (c instanceof Capsule.Carrier) {
            return mapItemFn.apply((Carrier<T>) c);
        } else throw new UnknownCapsuleException();
    }

    static <T, R> Capsule<R> map(Capsule<T> c, Function<Capsule.Carrier<T>, Capsule<R>> mapItemFn) {
        return map(c,
                mapItemFn,
                done -> (Capsule.Done<R>) done);
    }

    class UnknownCapsuleException extends RuntimeException {
        public UnknownCapsuleException() {
            super("Unknown type of capsule");
        }
    }
}
