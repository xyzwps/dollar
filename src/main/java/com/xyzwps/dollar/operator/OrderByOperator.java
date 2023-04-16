package com.xyzwps.dollar.operator;

import com.xyzwps.dollar.Direction;
import com.xyzwps.dollar.tube.Capsule;
import com.xyzwps.dollar.tube.Tube;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Function;

import static com.xyzwps.dollar.tube.Utils.ascComparator;
import static com.xyzwps.dollar.tube.Utils.descComparator;

public class OrderByOperator<T, K extends Comparable<K>> implements Operator<T, T> {

    private final Function<T, K> toKey;
    private final Direction direction;

    public OrderByOperator(Function<T, K> toKey, Direction direction) {
        this.toKey = toKey;
        this.direction = direction;
    }

    private Iterator<T> itr;

    private boolean drained = false;

    @Override
    public Capsule<T> next(Tube<T> upstream) {
        if (!drained) {
            var list = new ArrayList<T>();
            for (boolean go = true; go; ) {
                switch (upstream.next()) {
                    case Capsule.Done<T> ignored -> go = false;
                    case Capsule.Carrier<T> carrier -> list.add(carrier.value());
                    case Capsule.Failure<T> failure -> {
                        return Capsule.failed(failure.cause());
                    }
                }
            }
            this.drained = true;
            // TODO: 对 list 排序
            var comparator = direction == Direction.DESC ? descComparator(toKey) : ascComparator(toKey);
            this.itr = list.stream().sorted(comparator).iterator();
        } // end if

        return itr.hasNext() ? Capsule.carry(itr.next()) : Capsule.done();
    }
}
