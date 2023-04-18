package com.xyzwps.lib.dollar.collector;

import java.util.Objects;

public class JoinCollector<T> implements Collector<T, String> {

    private final String sep;
    private final StringBuilder sb = new StringBuilder();
    private boolean first = true;

    public JoinCollector(String sep) {
        this.sep = Objects.requireNonNull(sep);
    }

    @Override
    public void onRequest(T t) {
        if (this.first) {
            this.first = false;
        } else {
            sb.append(this.sep);
        }
        sb.append(t);
    }

    @Override
    public String result() {
        return sb.toString();
    }
}
