package com.xyzwps.lib.dollar.operator;

import com.xyzwps.lib.dollar.Direction;
import com.xyzwps.lib.dollar.tube.Capsule;
import com.xyzwps.lib.dollar.tube.Tube;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.function.Function;

import static com.xyzwps.lib.dollar.tube.Utils.ascComparator;
import static com.xyzwps.lib.dollar.tube.Utils.descComparator;

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
            ArrayList<T> list = new ArrayList<>();
            for (boolean go = true; go; ) {

                Capsule<T> c = upstream.next();
                if (c instanceof Capsule.Done) {
                    go = false;
                } else if (c instanceof Capsule.Failure) {
                    return c;
                } else if (c instanceof Capsule.Carrier) {
                    list.add(((Capsule.Carrier<T>) c).value());
                } else {
                    throw new Capsule.UnknownCapsuleException();
                }
            }
            this.drained = true;
            Comparator<T> comparator = direction == Direction.DESC ? descComparator(toKey) : ascComparator(toKey);
            this.itr = list.stream().sorted(comparator).iterator(); // FIXME: 优化
        } // end if

        return itr.hasNext() ? Capsule.carry(itr.next()) : Capsule.done();
    }
}
