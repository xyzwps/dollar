package com.xyzwps.lib.dollar;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static com.xyzwps.lib.dollar.Dollar.*;
import static org.junit.jupiter.api.Assertions.*;

class DollarTests {

    @Test
    void listTube() {
        assertEquals("[]", $((List<Integer>) null).value().toString());
    }

    @Test
    void mapTube() {
        assertEquals("{}", $((Map<String, Integer>) null).value().toString());
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
        assertEquals("[]", $.compact(null).toString());
    }

    @Test
    void defaultTo() {
        assertEquals(1, $.defaultTo(null, 1));
        assertEquals(2, $.defaultTo(2, 1));
        assertNull($.defaultTo(null, null));
    }

    @Test
    void filter1() {
        assertEquals("[2, 4]", $.filter($.list(1, 2, 3, 4, 5), i -> i % 2 == 0).toString());

        List<Integer> nullList = null;
        assertEquals("[]", $.filter(nullList, i -> i % 2 == 0).toString());

        Predicate<Integer> nullPredicate = null;
        assertThrows(NullPointerException.class, () -> $.filter($.list(1, 2, 3, 4, 5), nullPredicate).toString());
    }

    @Test
    void filter2() {
        assertEquals("[1, 3, 5]", $.filter($.list(1, 2, 3, 4, 5), (e, i) -> i % 2 == 0).toString());

        List<Integer> nullList = null;
        assertEquals("[]", $.filter(nullList, (e, i) -> i % 2 == 0).toString());

        BiPredicate<Integer, Integer> nullPredicate = null;
        assertThrows(NullPointerException.class, () -> $.filter($.list(1, 2, 3, 4, 5), nullPredicate).toString());
    }

    @Test
    void concat() {
        assertEquals("[]", $.concat().toString());
        assertEquals("[]", $.concat(null, null).toString());
        assertEquals("[1, 2, 3, 4]", $.concat($.list(1, 2), $.list(3, 4)).toString());
        assertEquals("[1, 2, 3, 4]", $.concat($.list(1, 2), null, $.list(3, 4)).toString());
    }

    @Test
    void pad() {
        assertThrows(IllegalArgumentException.class, () -> $.pad("", -1, " "));

        assertEquals("      ", $.pad(null, 6, null));
        assertEquals("      ", $.pad(null, 6, ""));
        assertEquals("      ", $.pad(null, 6, " "));
        assertEquals("aaaaaa", $.pad(null, 6, "a"));
        assertEquals("ababab", $.pad(null, 6, "ab"));
        assertEquals("abcdab", $.pad(null, 6, "abcd"));

        assertEquals("      ", $.pad("", 6, null));
        assertEquals("      ", $.pad("", 6, ""));
        assertEquals("      ", $.pad("", 6, " "));
        assertEquals("aaaaaa", $.pad("", 6, "a"));
        assertEquals("ababab", $.pad("", 6, "ab"));
        assertEquals("abcdab", $.pad("", 6, "abcd"));

        assertEquals(" +++  ", $.pad("+++", 6, null));
        assertEquals(" +++  ", $.pad("+++", 6, ""));
        assertEquals(" +++  ", $.pad("+++", 6, " "));
        assertEquals("a+++aa", $.pad("+++", 6, "a"));
        assertEquals("a+++ba", $.pad("+++", 6, "ab"));
        assertEquals("a+++bc", $.pad("+++", 6, "abcd"));

        assertEquals("+++---***", $.pad("+++---***", 6, "abcd"));
    }

    @Test
    void padEnd() {
        assertThrows(IllegalArgumentException.class, () -> $.padEnd("", -1, " "));

        assertEquals("      ", $.padEnd(null, 6, null));
        assertEquals("      ", $.padEnd(null, 6, ""));
        assertEquals("      ", $.padEnd(null, 6, " "));
        assertEquals("aaaaaa", $.padEnd(null, 6, "a"));
        assertEquals("ababab", $.padEnd(null, 6, "ab"));
        assertEquals("abcdab", $.padEnd(null, 6, "abcd"));

        assertEquals("      ", $.padEnd("", 6, null));
        assertEquals("      ", $.padEnd("", 6, ""));
        assertEquals("      ", $.padEnd("", 6, " "));
        assertEquals("aaaaaa", $.padEnd("", 6, "a"));
        assertEquals("ababab", $.padEnd("", 6, "ab"));
        assertEquals("abcdab", $.padEnd("", 6, "abcd"));

        assertEquals("+++   ", $.padEnd("+++", 6, null));
        assertEquals("+++   ", $.padEnd("+++", 6, ""));
        assertEquals("+++   ", $.padEnd("+++", 6, " "));
        assertEquals("+++aaa", $.padEnd("+++", 6, "a"));
        assertEquals("+++aba", $.padEnd("+++", 6, "ab"));
        assertEquals("+++abc", $.padEnd("+++", 6, "abcd"));

        assertEquals("+++---***", $.padEnd("+++---***", 6, "abcd"));
    }

    @Test
    void padStart() {
        assertThrows(IllegalArgumentException.class, () -> $.padStart("", -1, " "));

        assertEquals("      ", $.padStart(null, 6, null));
        assertEquals("      ", $.padStart(null, 6, ""));
        assertEquals("      ", $.padStart(null, 6, " "));
        assertEquals("aaaaaa", $.padStart(null, 6, "a"));
        assertEquals("ababab", $.padStart(null, 6, "ab"));
        assertEquals("abcdab", $.padStart(null, 6, "abcd"));

        assertEquals("      ", $.padStart("", 6, null));
        assertEquals("      ", $.padStart("", 6, ""));
        assertEquals("      ", $.padStart("", 6, " "));
        assertEquals("aaaaaa", $.padStart("", 6, "a"));
        assertEquals("ababab", $.padStart("", 6, "ab"));
        assertEquals("abcdab", $.padStart("", 6, "abcd"));

        assertEquals("   +++", $.padStart("+++", 6, null));
        assertEquals("   +++", $.padStart("+++", 6, ""));
        assertEquals("   +++", $.padStart("+++", 6, " "));
        assertEquals("aaa+++", $.padStart("+++", 6, "a"));
        assertEquals("aba+++", $.padStart("+++", 6, "ab"));
        assertEquals("abc+++", $.padStart("+++", 6, "abcd"));

        assertEquals("+++---***", $.padStart("+++---***", 6, "abcd"));
    }


    @Test
    void range() {
        assertEquals("[1, 2, 3]", $.range(1, 4).value().toString());
        assertEquals("[]", $.range(1, 1).value().toString());
        assertEquals("[]", $.range(1, -1).value().toString());
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
