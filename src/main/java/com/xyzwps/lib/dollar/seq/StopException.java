package com.xyzwps.lib.dollar.seq;


public class StopException extends RuntimeException {
    // TODO: 单例异常
    static <T> Seq<T> stop(Seq<T> seq) {
        return tConsumer -> {
            try {
                seq.forEach(tConsumer);
            } catch (StopException ignored) {
            }
        };
    }

    static void stop(Runnable runnable) {
        try {
            runnable.run();
        } catch (StopException ignored) {
        }
    }
}
