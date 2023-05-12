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
        assertEquals("[]", $.arrayList().toString());
        assertEquals("[1]", $.arrayList(1).toString());
        assertEquals("[1, 2]", $.arrayList(1, 2).toString());
        assertEquals("[1, 2, 3]", $.arrayList(1, 2, 3).toString());
        assertEquals("[1, 2, 3, 4]", $.arrayList(1, 2, 3, 4).toString());
        assertEquals("[1, 2, 3, 4, 5]", $.arrayList(1, 2, 3, 4, 5).toString());
        assertEquals("[1, 2, 3, 4, 5, 6]", $.arrayList(1, 2, 3, 4, 5, 6).toString());
        assertEquals("[1, 2, 3, 4, 5, 6, 7]", $.arrayList(1, 2, 3, 4, 5, 6, 7).toString());
        assertEquals("[1, 2, 3, 4, 5, 6, 7, 8]", $.arrayList(1, 2, 3, 4, 5, 6, 7, 8).toString());
        assertEquals("[1, 2, 3, 4, 5, 6, 7, 8, 9]", $.arrayList(1, 2, 3, 4, 5, 6, 7, 8, 9).toString());
        assertEquals("[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]", $.arrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).toString());
    }

    @Test
    void chunk() {
        assertEquals("[]", $.chunk($.arrayList(), 6).toString());
        assertEquals("[]", $.chunk(null, 6).toString());

        assertThrows(IllegalArgumentException.class, () -> $.chunk($.arrayList(), 0));
        assertThrows(IllegalArgumentException.class, () -> $.chunk(null, 0));

        assertEquals("[[1], [2], [3], [4], [5]]", $.chunk($.arrayList(1, 2, 3, 4, 5), 1).toString());
        assertEquals("[[1, 2], [3, 4], [5]]", $.chunk($.arrayList(1, 2, 3, 4, 5), 2).toString());
        assertEquals("[[1, 2, 3], [4, 5]]", $.chunk($.arrayList(1, 2, 3, 4, 5), 3).toString());
        assertEquals("[[1, 2, 3, 4], [5]]", $.chunk($.arrayList(1, 2, 3, 4, 5), 4).toString());
        assertEquals("[[1, 2, 3, 4, 5]]", $.chunk($.arrayList(1, 2, 3, 4, 5), 5).toString());
        assertEquals("[[1, 2, 3, 4, 5]]", $.chunk($.arrayList(1, 2, 3, 4, 5), 6).toString());

        assertThrows(IllegalArgumentException.class, () -> $.chunk($.arrayList(1, 2, 3, 4, 5), 0));
        assertThrows(IllegalArgumentException.class, () -> $.chunk($.arrayList(1, 2, 3, 4, 5), -1));
    }

    @Test
    void compact() {
        assertEquals("[]", $.compact($.arrayList(null, "", false, 0)).toString());
        assertEquals("[6, 哈哈]", $.compact($.arrayList(null, 6, "", "哈哈", false, 0)).toString());
        assertEquals("[]", $.compact(null).toString());
    }

    @Test
    void filter1() {
        assertEquals("[2, 4]", $.filter($.arrayList(1, 2, 3, 4, 5), i -> i % 2 == 0).toString());

        ;
        assertEquals("[]", $.filter((List<Integer>) null, i -> i % 2 == 0).toString());

        assertThrows(NullPointerException.class, () -> $.filter($.arrayList(1, 2, 3, 4, 5), (Predicate<Integer>) null).toString());
    }

    @Test
    void filter2() {
        assertEquals("[1, 3, 5]", $.filter($.arrayList(1, 2, 3, 4, 5), (e, i) -> i % 2 == 0).toString());

        assertEquals("[]", $.filter(null, (e, i) -> i % 2 == 0).toString());

        assertThrows(NullPointerException.class, () -> $.filter($.arrayList(1, 2, 3, 4, 5), (BiPredicate<Integer, Integer>) null).toString());
    }

    @Test
    void concat() {
        assertEquals("[]", $.concat().toString());
        assertEquals("[]", $.concat(null, null).toString());
        assertEquals("[1, 2, 3, 4]", $.concat($.arrayList(1, 2), $.arrayList(3, 4)).toString());
        assertEquals("[1, 2, 3, 4]", $.concat($.arrayList(1, 2), null, $.arrayList(3, 4)).toString());
    }

    @Test
    void zip() {

        assertThrows(IllegalArgumentException.class, () -> $.zip($.arrayList(1), $.arrayList(2), null));

        assertEquals($.zip(null, null).size(), 0);
        assertIterableEquals($.zip($.arrayList(1, 2, 3, 4, 5), null), $.arrayList(
                Pair.of(1, null),
                Pair.of(2, null),
                Pair.of(3, null),
                Pair.of(4, null),
                Pair.of(5, null)
        ));
        assertIterableEquals($.zip(null, $.arrayList(1, 2, 3, 4, 5)), $.arrayList(
                Pair.of(null, 1),
                Pair.of(null, 2),
                Pair.of(null, 3),
                Pair.of(null, 4),
                Pair.of(null, 5)
        ));

        // random access
        {
            List<Integer> l1 = new ArrayList<>($.arrayList(1, 2, 3, 4, 5));
            List<String> l2 = new ArrayList<>($.arrayList("x", "-", "+"));
            assertIterableEquals($.zip(l1, l2), $.arrayList(
                    Pair.of(1, "x"),
                    Pair.of(2, "-"),
                    Pair.of(3, "+"),
                    Pair.of(4, null),
                    Pair.of(5, null)
            ));
            assertIterableEquals($.zip(l2, l1), $.arrayList(
                    Pair.of("x", 1),
                    Pair.of("-", 2),
                    Pair.of("+", 3),
                    Pair.of(null, 4),
                    Pair.of(null, 5)
            ));
        }

        // non-random access
        {
            List<Integer> l1 = new LinkedList<>($.arrayList(1, 2, 3, 4, 5));
            List<String> l2 = new LinkedList<>($.arrayList("x", "-", "+"));
            assertIterableEquals($.zip(l1, l2), $.arrayList(
                    Pair.of(1, "x"),
                    Pair.of(2, "-"),
                    Pair.of(3, "+"),
                    Pair.of(4, null),
                    Pair.of(5, null)
            ));
            assertIterableEquals($.zip(l2, l1), $.arrayList(
                    Pair.of("x", 1),
                    Pair.of("-", 2),
                    Pair.of("+", 3),
                    Pair.of(null, 4),
                    Pair.of(null, 5)
            ));
        }
    }
}
