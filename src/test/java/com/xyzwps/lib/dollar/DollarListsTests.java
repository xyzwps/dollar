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
    void arrayList() {
        assertEquals("[]", $.list().toString());
        assertEquals("[1]", $.list(1).toString());
        assertEquals("[1, 2]", $.list(1, 2).toString());
        assertEquals("[1, 2, 3]", $.list(1, 2, 3).toString());
        assertEquals("[1, 2, 3, 4]", $.list(1, 2, 3, 4).toString());
        assertEquals("[1, 2, 3, 4, 5]", $.list(1, 2, 3, 4, 5).toString());
        assertEquals("[1, 2, 3, 4, 5, 6]", $.list(1, 2, 3, 4, 5, 6).toString());
        assertEquals("[1, 2, 3, 4, 5, 6, 7]", $.list(1, 2, 3, 4, 5, 6, 7).toString());
        assertEquals("[1, 2, 3, 4, 5, 6, 7, 8]", $.list(1, 2, 3, 4, 5, 6, 7, 8).toString());
        assertEquals("[1, 2, 3, 4, 5, 6, 7, 8, 9]", $.list(1, 2, 3, 4, 5, 6, 7, 8, 9).toString());
        assertEquals("[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]", $.list(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).toString());
    }

    @Test
    void chunk() {
        assertEquals("[]", $.chunk($.list(), 6).toString());
        assertEquals("[]", $.chunk(null, 6).toString());

        assertThrows(IllegalArgumentException.class, () -> $.chunk($.list(), 0));
        assertThrows(IllegalArgumentException.class, () -> $.chunk(null, 0));

        assertEquals("[[1], [2], [3], [4], [5]]", $.chunk($.list(1, 2, 3, 4, 5), 1).toString());
        assertEquals("[[1, 2], [3, 4], [5]]", $.chunk($.list(1, 2, 3, 4, 5), 2).toString());
        assertEquals("[[1, 2, 3], [4, 5]]", $.chunk($.list(1, 2, 3, 4, 5), 3).toString());
        assertEquals("[[1, 2, 3, 4], [5]]", $.chunk($.list(1, 2, 3, 4, 5), 4).toString());
        assertEquals("[[1, 2, 3, 4, 5]]", $.chunk($.list(1, 2, 3, 4, 5), 5).toString());
        assertEquals("[[1, 2, 3, 4, 5]]", $.chunk($.list(1, 2, 3, 4, 5), 6).toString());

        assertThrows(IllegalArgumentException.class, () -> $.chunk($.list(1, 2, 3, 4, 5), 0));
        assertThrows(IllegalArgumentException.class, () -> $.chunk($.list(1, 2, 3, 4, 5), -1));
    }

    @Test
    void compact() {
        assertEquals("[]", $.compact($.list(null, "", false, 0)).toString());
        assertEquals("[6, 哈哈]", $.compact($.list(null, 6, "", "哈哈", false, 0)).toString());
        assertEquals("[]", $.compact(null).toString());
    }

    @Test
    void filter1() {
        assertEquals("[2, 4]", $.filter($.list(1, 2, 3, 4, 5), i -> i % 2 == 0).toString());

        ;
        assertEquals("[]", $.filter((List<Integer>) null, i -> i % 2 == 0).toString());

        assertThrows(NullPointerException.class, () -> $.filter($.list(1, 2, 3, 4, 5), (Predicate<Integer>) null).toString());
    }

    @Test
    void filter2() {
        assertEquals("[1, 3, 5]", $.filter($.list(1, 2, 3, 4, 5), (e, i) -> i % 2 == 0).toString());

        assertEquals("[]", $.filter(null, (e, i) -> i % 2 == 0).toString());

        assertThrows(NullPointerException.class, () -> $.filter($.list(1, 2, 3, 4, 5), (BiPredicate<Integer, Integer>) null).toString());
    }

    @Test
    void concat() {
        assertEquals("[]", $.concat().toString());
        assertEquals("[]", $.concat(null, null).toString());
        assertEquals("[1, 2, 3, 4]", $.concat($.list(1, 2), $.list(3, 4)).toString());
        assertEquals("[1, 2, 3, 4]", $.concat($.list(1, 2), null, $.list(3, 4)).toString());
    }

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
