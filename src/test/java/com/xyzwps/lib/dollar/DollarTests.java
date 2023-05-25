package com.xyzwps.lib.dollar;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static com.xyzwps.lib.dollar.Dollar.*;

class DollarTests {

    @Test
    void forEach1() {
        List<Integer> t = new ArrayList<>();
        $.forEach($.list(1, 2, 3), i -> t.add(i));
        assertEquals("[1, 2, 3]", t.toString());

    }

    @Test
    void forEach2() {
        List<Integer> t = new ArrayList<>();
        $.forEach($.list(1, 2, 3), (it, index) -> t.add(it + (index + 1) * 10));
        assertEquals("[11, 22, 33]", t.toString());
    }

    @Test
    void flatMap() {
        assertEquals(
                "[11, 12, 21, 22]",
                $.flatMap($.list(1, 2), i -> $.just(i * 10 + 1, i * 10 + 2)).toString()
        );
    }

    @Test
    void first() {
        assertEquals(Optional.of(1), $.first($.list(1, 2)));
        assertEquals(Optional.empty(), $.first($.list(null, 2)));
        assertEquals(Optional.empty(), $.head($.list()));
    }

    @Test
    void filter1() {
        assertEquals("[a,  ]", $.filter($.list("a", " ", null), Objects::nonNull).toString());
        assertEquals("[2, 4]", $.filter($.list(1, 2, 3, 4, 5), i -> i % 2 == 0).toString());
        assertEquals("[]", $.filter((List<Integer>) null, i -> i % 2 == 0).toString());
        assertThrows(NullPointerException.class, () -> $.filter($.list(1, 2, 3, 4, 5), (Predicate<Integer>) null).toString());
    }

    @Test
    void filter2() {
        assertEquals("[1, 3, 5]", $.filter($.list(1, 2, 3, 4, 5), (it, i) -> i % 2 == 0).toString());
        assertEquals("[]", $.filter(null, (e, i) -> i % 2 == 0).toString());
        assertThrows(NullPointerException.class, () -> $.filter($.list(1, 2, 3, 4, 5), (BiPredicate<Integer, Integer>) null).toString());
    }

    @Test
    void concat() {
        assertEquals("[a, , null, 1, 2, null, b]", $.concat(
                        $.list("a", "", null),
                        null,
                        $.list("1", "2"),
                        $.list(),
                        $.list(null, "b"))
                .toString());
        assertEquals("[]", $.concat().toString());
        assertEquals("[]", $.concat(null, null).toString());
        assertEquals("[1, 2, 3, 4]", $.concat($.list(1, 2), $.list(3, 4)).toString());
        assertEquals("[1, 2, 3, 4]", $.concat($.list(1, 2), null, $.list(3, 4)).toString());
    }

    @Test
    void compact() {
        assertEquals("[a]", $.compact($.list("a", "", null)).toString());
        assertEquals("[]", $.compact($.list(null, "", false, 0)).toString());
        assertEquals("[6, 哈哈]", $.compact($.list(null, 6, "", "哈哈", false, 0)).toString());
        assertEquals("[]", $.compact(null).toString());
    }

    @Test
    void chunk() {
        String[] cases = new String[]{
                "wont test",
                "[[1], [2], [3], [4], [5], [6]]",
                "[[1, 2], [3, 4], [5, 6]]",
                "[[1, 2, 3], [4, 5, 6]]",
                "[[1, 2, 3, 4], [5, 6]]",
                "[[1, 2, 3, 4, 5], [6]]",
                "[[1, 2, 3, 4, 5, 6]]",
                "[[1, 2, 3, 4, 5, 6]]"
        };
        List<Integer> list = $.list(1, 2, 3, 4, 5, 6);
        for (int i = 1; i < cases.length; i++) {
            assertEquals(cases[i], $.chunk(list, i).toString());
        }

        assertThrows(IllegalArgumentException.class, () -> $.chunk(list, 0));
    }

    @Test
    void defaultTo() {
        assertEquals(1, $.defaultTo(null, 1));
        assertEquals(2, $.defaultTo(2, 1));
        //noinspection ConstantValue
        assertNull($.defaultTo(null, null));
    }

    @Test
    void list() {
        assertEquals("[]", Dollar.$.list().toString());
        assertEquals("[1]", Dollar.$.list(1).toString());
        assertEquals("[1, 2]", Dollar.$.list(1, 2).toString());
        assertEquals("[1, 2, 3]", Dollar.$.list(1, 2, 3).toString());
        assertEquals("[1, 2, 3, 4]", Dollar.$.list(1, 2, 3, 4).toString());
        assertEquals("[1, 2, 3, 4, 5]", Dollar.$.list(1, 2, 3, 4, 5).toString());
        assertEquals("[1, 2, 3, 4, 5, 6]", Dollar.$.list(1, 2, 3, 4, 5, 6).toString());
        assertEquals("[1, 2, 3, 4, 5, 6, 7]", Dollar.$.list(1, 2, 3, 4, 5, 6, 7).toString());
        assertEquals("[1, 2, 3, 4, 5, 6, 7, 8]", Dollar.$.list(1, 2, 3, 4, 5, 6, 7, 8).toString());
        assertEquals("[1, 2, 3, 4, 5, 6, 7, 8, 9]", Dollar.$.list(1, 2, 3, 4, 5, 6, 7, 8, 9).toString());
        assertEquals("[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]", Dollar.$.list(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).toString());
    }

    @Test
    void hashMap() {
        {
            Map<Integer, Integer> map = Dollar.$.hashMap();
            assertEquals(0, map.size());
        }
        {
            Map<Integer, Integer> map = Dollar.$.hashMap(1, 1);
            assertEquals(1, map.size());

            assertEquals(1, map.get(1));
        }
        {
            Map<Integer, Integer> map = Dollar.$.hashMap(1, 1, 2, 2);
            assertEquals(2, map.size());

            assertEquals(1, map.get(1));
            assertEquals(2, map.get(2));
        }
        {
            Map<Integer, Integer> map = Dollar.$.hashMap(1, 1, 2, 2, 3, 3);
            assertEquals(3, map.size());

            assertEquals(1, map.get(1));
            assertEquals(2, map.get(2));
            assertEquals(3, map.get(3));
        }
        {
            Map<Integer, Integer> map = Dollar.$.hashMap(1, 1, 2, 2, 3, 3, 4, 4);
            assertEquals(4, map.size());

            assertEquals(1, map.get(1));
            assertEquals(2, map.get(2));
            assertEquals(3, map.get(3));
            assertEquals(4, map.get(4));
        }
        {
            Map<Integer, Integer> map = Dollar.$.hashMap(1, 1, 2, 2, 3, 3, 4, 4, 5, 5);
            assertEquals(5, map.size());

            assertEquals(1, map.get(1));
            assertEquals(2, map.get(2));
            assertEquals(3, map.get(3));
            assertEquals(4, map.get(4));
            assertEquals(5, map.get(5));
        }
        {
            Map<Integer, Integer> map = Dollar.$.hashMap(1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6);
            assertEquals(6, map.size());

            assertEquals(1, map.get(1));
            assertEquals(2, map.get(2));
            assertEquals(3, map.get(3));
            assertEquals(4, map.get(4));
            assertEquals(5, map.get(5));
            assertEquals(6, map.get(6));
        }
        {
            Map<Integer, Integer> map = Dollar.$.hashMap(1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7);
            assertEquals(7, map.size());

            assertEquals(1, map.get(1));
            assertEquals(2, map.get(2));
            assertEquals(3, map.get(3));
            assertEquals(4, map.get(4));
            assertEquals(5, map.get(5));
            assertEquals(6, map.get(6));
            assertEquals(7, map.get(7));
        }
        {
            Map<Integer, Integer> map = Dollar.$.hashMap(1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8);
            assertEquals(8, map.size());

            assertEquals(1, map.get(1));
            assertEquals(2, map.get(2));
            assertEquals(3, map.get(3));
            assertEquals(4, map.get(4));
            assertEquals(5, map.get(5));
            assertEquals(6, map.get(6));
            assertEquals(7, map.get(7));
            assertEquals(8, map.get(8));
        }
        {
            Map<Integer, Integer> map = Dollar.$.hashMap(1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9);
            assertEquals(9, map.size());

            assertEquals(1, map.get(1));
            assertEquals(2, map.get(2));
            assertEquals(3, map.get(3));
            assertEquals(4, map.get(4));
            assertEquals(5, map.get(5));
            assertEquals(6, map.get(6));
            assertEquals(7, map.get(7));
            assertEquals(8, map.get(8));
            assertEquals(9, map.get(9));
        }
        {
            Map<Integer, Integer> map = Dollar.$.hashMap(1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 10, 10);
            assertEquals(10, map.size());

            assertEquals(1, map.get(1));
            assertEquals(2, map.get(2));
            assertEquals(3, map.get(3));
            assertEquals(4, map.get(4));
            assertEquals(5, map.get(5));
            assertEquals(6, map.get(6));
            assertEquals(7, map.get(7));
            assertEquals(8, map.get(8));
            assertEquals(9, map.get(9));
            assertEquals(10, map.get(10));
        }
    }

    @Test
    void pad() {
        assertThrows(IllegalArgumentException.class, () -> Dollar.$.pad("", -1, " "));

        assertEquals("      ", Dollar.$.pad(null, 6, null));
        assertEquals("      ", Dollar.$.pad(null, 6, ""));
        assertEquals("      ", Dollar.$.pad(null, 6, " "));
        assertEquals("aaaaaa", Dollar.$.pad(null, 6, "a"));
        assertEquals("ababab", Dollar.$.pad(null, 6, "ab"));
        assertEquals("abcdab", Dollar.$.pad(null, 6, "abcd"));

        assertEquals("      ", Dollar.$.pad("", 6, null));
        assertEquals("      ", Dollar.$.pad("", 6, ""));
        assertEquals("      ", Dollar.$.pad("", 6, " "));
        assertEquals("aaaaaa", Dollar.$.pad("", 6, "a"));
        assertEquals("ababab", Dollar.$.pad("", 6, "ab"));
        assertEquals("abcdab", Dollar.$.pad("", 6, "abcd"));

        assertEquals(" +++  ", Dollar.$.pad("+++", 6, null));
        assertEquals(" +++  ", Dollar.$.pad("+++", 6, ""));
        assertEquals(" +++  ", Dollar.$.pad("+++", 6, " "));
        assertEquals("a+++aa", Dollar.$.pad("+++", 6, "a"));
        assertEquals("a+++ba", Dollar.$.pad("+++", 6, "ab"));
        assertEquals("a+++bc", Dollar.$.pad("+++", 6, "abcd"));

        assertEquals("+++---***", Dollar.$.pad("+++---***", 6, "abcd"));
    }

    @Test
    void padEnd() {
        assertThrows(IllegalArgumentException.class, () -> Dollar.$.padEnd("", -1, " "));

        assertEquals("      ", Dollar.$.padEnd(null, 6, null));
        assertEquals("      ", Dollar.$.padEnd(null, 6, ""));
        assertEquals("      ", Dollar.$.padEnd(null, 6, " "));
        assertEquals("aaaaaa", Dollar.$.padEnd(null, 6, "a"));
        assertEquals("ababab", Dollar.$.padEnd(null, 6, "ab"));
        assertEquals("abcdab", Dollar.$.padEnd(null, 6, "abcd"));

        assertEquals("      ", Dollar.$.padEnd("", 6, null));
        assertEquals("      ", Dollar.$.padEnd("", 6, ""));
        assertEquals("      ", Dollar.$.padEnd("", 6, " "));
        assertEquals("aaaaaa", Dollar.$.padEnd("", 6, "a"));
        assertEquals("ababab", Dollar.$.padEnd("", 6, "ab"));
        assertEquals("abcdab", Dollar.$.padEnd("", 6, "abcd"));

        assertEquals("+++   ", Dollar.$.padEnd("+++", 6, null));
        assertEquals("+++   ", Dollar.$.padEnd("+++", 6, ""));
        assertEquals("+++   ", Dollar.$.padEnd("+++", 6, " "));
        assertEquals("+++aaa", Dollar.$.padEnd("+++", 6, "a"));
        assertEquals("+++aba", Dollar.$.padEnd("+++", 6, "ab"));
        assertEquals("+++abc", Dollar.$.padEnd("+++", 6, "abcd"));

        assertEquals("+++---***", Dollar.$.padEnd("+++---***", 6, "abcd"));
    }

    @Test
    void padStart() {
        assertThrows(IllegalArgumentException.class, () -> Dollar.$.padStart("", -1, " "));

        assertEquals("      ", Dollar.$.padStart(null, 6, null));
        assertEquals("      ", Dollar.$.padStart(null, 6, ""));
        assertEquals("      ", Dollar.$.padStart(null, 6, " "));
        assertEquals("aaaaaa", Dollar.$.padStart(null, 6, "a"));
        assertEquals("ababab", Dollar.$.padStart(null, 6, "ab"));
        assertEquals("abcdab", Dollar.$.padStart(null, 6, "abcd"));

        assertEquals("      ", Dollar.$.padStart("", 6, null));
        assertEquals("      ", Dollar.$.padStart("", 6, ""));
        assertEquals("      ", Dollar.$.padStart("", 6, " "));
        assertEquals("aaaaaa", Dollar.$.padStart("", 6, "a"));
        assertEquals("ababab", Dollar.$.padStart("", 6, "ab"));
        assertEquals("abcdab", Dollar.$.padStart("", 6, "abcd"));

        assertEquals("   +++", Dollar.$.padStart("+++", 6, null));
        assertEquals("   +++", Dollar.$.padStart("+++", 6, ""));
        assertEquals("   +++", Dollar.$.padStart("+++", 6, " "));
        assertEquals("aaa+++", Dollar.$.padStart("+++", 6, "a"));
        assertEquals("aba+++", Dollar.$.padStart("+++", 6, "ab"));
        assertEquals("abc+++", Dollar.$.padStart("+++", 6, "abcd"));

        assertEquals("+++---***", Dollar.$.padStart("+++---***", 6, "abcd"));
    }
}
