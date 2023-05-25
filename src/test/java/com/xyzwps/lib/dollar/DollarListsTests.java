package com.xyzwps.lib.dollar;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static com.xyzwps.lib.dollar.Dollar.$;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class DollarListsTests {




    @Test
    void zip() {

        assertThrows(IllegalArgumentException.class, () -> $.zip($.list(1), $.list(2), null));

        assertEquals($.zip(null, null).size(), 0);
        assertIterableEquals($.zip($.list(1, 2, 3, 4, 5), null), $.list(
                Pair.of(1, null),
                Pair.of(2, null),
                Pair.of(3, null),
                Pair.of(4, null),
                Pair.of(5, null)
        ));
        assertIterableEquals($.zip(null, $.list(1, 2, 3, 4, 5)), $.list(
                Pair.of(null, 1),
                Pair.of(null, 2),
                Pair.of(null, 3),
                Pair.of(null, 4),
                Pair.of(null, 5)
        ));

        // random access
        {
            List<Integer> l1 = new ArrayList<>($.list(1, 2, 3, 4, 5));
            List<String> l2 = new ArrayList<>($.list("x", "-", "+"));
            assertIterableEquals($.zip(l1, l2), $.list(
                    Pair.of(1, "x"),
                    Pair.of(2, "-"),
                    Pair.of(3, "+"),
                    Pair.of(4, null),
                    Pair.of(5, null)
            ));
            assertIterableEquals($.zip(l2, l1), $.list(
                    Pair.of("x", 1),
                    Pair.of("-", 2),
                    Pair.of("+", 3),
                    Pair.of(null, 4),
                    Pair.of(null, 5)
            ));
        }

        // non-random access
        {
            List<Integer> l1 = new LinkedList<>($.list(1, 2, 3, 4, 5));
            List<String> l2 = new LinkedList<>($.list("x", "-", "+"));
            assertIterableEquals($.zip(l1, l2), $.list(
                    Pair.of(1, "x"),
                    Pair.of(2, "-"),
                    Pair.of(3, "+"),
                    Pair.of(4, null),
                    Pair.of(5, null)
            ));
            assertIterableEquals($.zip(l2, l1), $.list(
                    Pair.of("x", 1),
                    Pair.of("-", 2),
                    Pair.of("+", 3),
                    Pair.of(null, 4),
                    Pair.of(null, 5)
            ));
        }
    }
}
