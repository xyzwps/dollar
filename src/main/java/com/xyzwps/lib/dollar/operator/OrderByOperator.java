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

/**
 * Used by orderBy method.
 *
 * @param <T> source element type
 * @param <K> sorting key type
 */
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
                } else if (c instanceof Capsule.Carrier) {
                    list.add(((Capsule.Carrier<T>) c).value());
                } else {
                    throw new Capsule.UnknownCapsuleException();
                }
            }
            this.drained = true;
            Comparator<T> comparator = direction == Direction.DESC ? descComparator(toKey) : ascComparator(toKey);
            list.sort(comparator);
            this.itr = list.iterator();
        } // end if

        return itr.hasNext() ? Capsule.carry(itr.next()) : Capsule.done();
    }
}
