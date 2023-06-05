package com.xyzwps.lib.dollar;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static com.xyzwps.lib.dollar.Dollar.*;

class DollarTests {

    @Test
    void zip2() {
        assertEquals("[(1, 1), (2, 2), (3, null)]", $.zip($.listOf(1, 2, 3), $.listOf(1, 2)).toString());
        assertEquals("[(1, 1), (2, 2), (3, 3)]", $.zip($.listOf(1, 2, 3), $.listOf(1, 2, 3)).toString());
        assertEquals("[(1, 1), (2, 2), (3, 3), (null, 4), (null, 5)]", $.zip($.listOf(1, 2, 3), $.listOf(1, 2, 3, 4, 5)).toString());

        assertEquals("[(1, null), (2, null), (3, null)]", $.zip($.listOf(1, 2, 3), $.listOf()).toString());
        assertEquals("[(1, null), (2, null), (3, null)]", $.zip($.listOf(1, 2, 3), null).toString());

        assertThrows(NullPointerException.class, () -> $.zip($.listOf(1), $.listOf(2), null));

        assertEquals($.zip(null, null).size(), 0);
    }

    @Test
    void unique() {
        assertEquals("[1, 2, 3]", $.unique($.listOf(1, 2, 1, 3)).toString());
    }

    @Test
    void uniqueBy() {
        assertEquals("[1, 2, 3]", $.uniqueBy($.listOf(1, 2, 1, 3, 4), i -> i % 3).toString());
    }

    @Test
    void takeWhile() {
        assertEquals("[1, 2]", $.takeWhile($.listOf(1, 2, 3, 4), i -> i < 3).toString());
    }

    @Test
    void take() {
        assertEquals("[1, 2]", $.take($.listOf(1, 2, 3, 4), 2).toString());
    }

    @Test
    void orderBy() {
        assertEquals("[1, 2, 3, 4, 5]", $.orderBy($.listOf(1, 3, 5, 2, 4), Function.identity(), Direction.ASC).toString());
        assertEquals("[5, 4, 3, 2, 1]", $.orderBy($.listOf(1, 3, 5, 2, 4), Function.identity(), Direction.DESC).toString());

        assertEquals("[]", $.orderBy((List<Integer>) null, Function.identity(), Direction.DESC).toString());
    }

    @Test
    void reverse() {
        assertEquals("[3, 2, 1]", $.reverse($.listOf(1, 2, 3)).toString());
        assertEquals("[4, 3, 2, 1]", $.reverse($.listOf(1, 2, 3, 4)).toString());
    }

    @Test
    void reduceIterable() {
        assertEquals(20, $.reduce($.listOf(1, 2, 3, 4), 10, Integer::sum));
    }

    @Test
    void reduceMap() {
        TreeMap<Integer, Integer> treeMap = new TreeMap<>();
        treeMap.put(1, 1);
        treeMap.put(2, 2);
        treeMap.put(3, 3);
        Integer result = $.reduce(treeMap, 100, (sum, k, v) -> sum + k * 10 + v);
        assertEquals(166, result);

        assertEquals(100, $.reduce((Map<Integer, Integer>) null, 100, (sum, k, v) -> sum + k * 10 + v));

        assertThrows(NullPointerException.class, () -> $.reduce(treeMap, 100, null));
    }

    @Test
    void map1() {
        assertEquals("[2, 4, 6]", $.map($.listOf(1, 2, 3), i -> i * 2).toString());
    }

    @Test
    void map2() {
        assertEquals("[11, 22, 33]", $.map($.just(1, 2, 3), (it, i) -> it + 10 * (i + 1)).toString());
    }

    @Test
    void keyBy() {
        Map<Integer, Integer> map = $.keyBy($.listOf(1, 4, 7, 2, 5, 3), i -> i % 3);
        assertEquals(3, map.size());
        assertEquals(1, map.get(1));
        assertEquals(2, map.get(2));
        assertEquals(3, map.get(0));
    }

    @Test
    void groupBy() {
        Map<Integer, List<Integer>> map = $.groupBy($.listOf(1, 4, 7, 2, 5, 3), i -> i % 3);
        assertEquals(3, map.size());
        assertEquals("[1, 4, 7]", map.get(1).toString());
        assertEquals("[2, 5]", map.get(2).toString());
        assertEquals("[3]", map.get(0).toString());
    }

    @Test
    void forEach1() {
        List<Integer> t = new ArrayList<>();
        $.forEach($.listOf(1, 2, 3), i -> t.add(i));
        assertEquals("[1, 2, 3]", t.toString());

    }

    @Test
    void forEach2() {
        List<Integer> t = new ArrayList<>();
        $.forEach($.listOf(1, 2, 3), (it, index) -> t.add(it + (index + 1) * 10));
        assertEquals("[11, 22, 33]", t.toString());
    }

    @Test
    void flatMap() {
        assertEquals(
                "[11, 12, 21, 22]",
                $.flatMap($.listOf(1, 2), i -> $.just(i * 10 + 1, i * 10 + 2)).toString()
        );
    }

    @Test
    void first() {
        assertEquals(Optional.of(1), $.first($.listOf(1, 2)));
        assertEquals(Optional.empty(), $.first($.listOf(null, 2)));
        assertEquals(Optional.empty(), $.head($.listOf()));
    }

    @Test
    void filter1() {
        assertEquals("[a,  ]", $.filter($.listOf("a", " ", null), Objects::nonNull).toString());
        assertEquals("[2, 4]", $.filter($.listOf(1, 2, 3, 4, 5), i -> i % 2 == 0).toString());
        assertEquals("[]", $.filter((List<Integer>) null, i -> i % 2 == 0).toString());
        assertThrows(NullPointerException.class, () -> $.filter($.listOf(1, 2, 3, 4, 5), (Predicate<Integer>) null).toString());
    }

    @Test
    void filter2() {
        assertEquals("[1, 3, 5]", $.filter($.listOf(1, 2, 3, 4, 5), (it, i) -> i % 2 == 0).toString());
        assertEquals("[]", $.filter(null, (e, i) -> i % 2 == 0).toString());
        assertThrows(NullPointerException.class, () -> $.filter($.listOf(1, 2, 3, 4, 5), (BiPredicate<Integer, Integer>) null).toString());
    }

    @Test
    void concat() {
        assertEquals("[a, , null, 1, 2, null, b]", $.concat(
                        $.listOf("a", "", null),
                        null,
                        $.listOf("1", "2"),
                        $.listOf(),
                        $.listOf(null, "b"))
                .toString());
        assertEquals("[]", $.concat().toString());
        assertEquals("[]", $.concat(null, null).toString());
        assertEquals("[1, 2, 3, 4]", $.concat($.listOf(1, 2), $.listOf(3, 4)).toString());
        assertEquals("[1, 2, 3, 4]", $.concat($.listOf(1, 2), null, $.listOf(3, 4)).toString());
    }

    @Test
    void compact() {
        assertEquals("[a]", $.compact($.listOf("a", "", null)).toString());
        assertEquals("[]", $.compact($.listOf(null, "", false, 0)).toString());
        assertEquals("[6, 哈哈]", $.compact($.listOf(null, 6, "", "哈哈", false, 0)).toString());
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
        List<Integer> list = $.listOf(1, 2, 3, 4, 5, 6);
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
    void listOf() {
        assertEquals("[]", Dollar.$.listOf().toString());
        assertEquals("[1]", Dollar.$.listOf(1).toString());
        assertEquals("[1, 2]", Dollar.$.listOf(1, 2).toString());
        assertEquals("[1, 2, 3]", Dollar.$.listOf(1, 2, 3).toString());
        assertEquals("[1, 2, 3, 4]", Dollar.$.listOf(1, 2, 3, 4).toString());
        assertEquals("[1, 2, 3, 4, 5]", Dollar.$.listOf(1, 2, 3, 4, 5).toString());
        assertEquals("[1, 2, 3, 4, 5, 6]", Dollar.$.listOf(1, 2, 3, 4, 5, 6).toString());
        assertEquals("[1, 2, 3, 4, 5, 6, 7]", Dollar.$.listOf(1, 2, 3, 4, 5, 6, 7).toString());
        assertEquals("[1, 2, 3, 4, 5, 6, 7, 8]", Dollar.$.listOf(1, 2, 3, 4, 5, 6, 7, 8).toString());
        assertEquals("[1, 2, 3, 4, 5, 6, 7, 8, 9]", Dollar.$.listOf(1, 2, 3, 4, 5, 6, 7, 8, 9).toString());
        assertEquals("[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]", Dollar.$.listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).toString());
    }

    @Test
    void mapOf() {
        {
            Map<Integer, Integer> map = Dollar.$.mapOf();
            assertEquals(0, map.size());
        }
        {
            Map<Integer, Integer> map = Dollar.$.mapOf(1, 1);
            assertEquals(1, map.size());

            assertEquals(1, map.get(1));
        }
        {
            Map<Integer, Integer> map = Dollar.$.mapOf(1, 1, 2, 2);
            assertEquals(2, map.size());

            assertEquals(1, map.get(1));
            assertEquals(2, map.get(2));
        }
        {
            Map<Integer, Integer> map = Dollar.$.mapOf(1, 1, 2, 2, 3, 3);
            assertEquals(3, map.size());

            assertEquals(1, map.get(1));
            assertEquals(2, map.get(2));
            assertEquals(3, map.get(3));
        }
        {
            Map<Integer, Integer> map = Dollar.$.mapOf(1, 1, 2, 2, 3, 3, 4, 4);
            assertEquals(4, map.size());

            assertEquals(1, map.get(1));
            assertEquals(2, map.get(2));
            assertEquals(3, map.get(3));
            assertEquals(4, map.get(4));
        }
        {
            Map<Integer, Integer> map = Dollar.$.mapOf(1, 1, 2, 2, 3, 3, 4, 4, 5, 5);
            assertEquals(5, map.size());

            assertEquals(1, map.get(1));
            assertEquals(2, map.get(2));
            assertEquals(3, map.get(3));
            assertEquals(4, map.get(4));
            assertEquals(5, map.get(5));
        }
        {
            Map<Integer, Integer> map = Dollar.$.mapOf(1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6);
            assertEquals(6, map.size());

            assertEquals(1, map.get(1));
            assertEquals(2, map.get(2));
            assertEquals(3, map.get(3));
            assertEquals(4, map.get(4));
            assertEquals(5, map.get(5));
            assertEquals(6, map.get(6));
        }
        {
            Map<Integer, Integer> map = Dollar.$.mapOf(1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7);
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
            Map<Integer, Integer> map = Dollar.$.mapOf(1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8);
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
            Map<Integer, Integer> map = Dollar.$.mapOf(1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9);
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
            Map<Integer, Integer> map = Dollar.$.mapOf(1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 10, 10);
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
    void mapKeys1() {
        TreeMap<Integer, Integer> treeMap = new TreeMap<>();
        treeMap.put(1, 1);
        treeMap.put(2, 2);
        treeMap.put(3, 3);
        treeMap.put(4, 4);
        treeMap.put(5, 5);
        treeMap.put(6, 6);
        Map<Integer, Integer> map = $.mapKeys(treeMap, i -> i % 3);
        assertEquals(1, map.get(1));
        assertEquals(2, map.get(2));
        assertEquals(3, map.get(0));
    }

    @Test
    void mapKeys2() {
        TreeMap<Integer, Integer> treeMap = new TreeMap<>();
        treeMap.put(1, 1);
        treeMap.put(2, 2);
        treeMap.put(3, 3);
        treeMap.put(4, 4);
        treeMap.put(5, 5);
        treeMap.put(6, 6);
        Map<Integer, Integer> map = $.mapKeys(treeMap, (key, value) -> (key + value) % 5);
        assertEquals(5, map.size());
        assertEquals(1, map.get(2));
        assertEquals(2, map.get(4));
        assertEquals(3, map.get(1));
        assertEquals(4, map.get(3));
        assertEquals(5, map.get(0));
    }

    @Test
    void mapValues1() {
        Map<Integer, Integer> map = $.mapValues($.mapOf(0, "", 1, "1", 2, "11", 3, "111"), String::length);
        assertEquals(4, map.size());
        assertEquals(0, map.get(0));
        assertEquals(1, map.get(1));
        assertEquals(2, map.get(2));
        assertEquals(3, map.get(3));
    }

    @Test
    void mapValues2() {
        Map<Integer, String> map = $.mapValues(
                $.mapOf(0, "", 1, "1", 2, "11", 3, "111"),
                (value, key) -> String.format("%d: %s", key, value));
        assertEquals(4, map.size());
        assertEquals("0: ", map.get(0));
        assertEquals("1: 1", map.get(1));
        assertEquals("2: 11", map.get(2));
        assertEquals("3: 111", map.get(3));
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
