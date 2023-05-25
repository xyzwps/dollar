package com.xyzwps.lib.dollar;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;

import static com.xyzwps.lib.dollar.Dollar.*;
import static org.junit.jupiter.api.Assertions.*;

// TODO: reorganize all of test cases
class ListStageTests {

    // TODO: test laziness

    @Test
    void groupBy() {
        Map<Integer, List<Integer>> map = $.just(1, 4, 7, 2, 5, 3).groupBy(i -> i % 3).value();
        assertEquals(3, map.size());
        assertEquals("[1, 4, 7]", map.get(1).toString());
        assertEquals("[2, 5]", map.get(2).toString());
        assertEquals("[3]", map.get(0).toString());
    }

    @Test
    void keyBy() {
        Map<Integer, Integer> map = $.just(1, 4, 7, 2, 5, 3).keyBy(i -> i % 3).value();
        assertEquals(3, map.size());
        assertEquals(1, map.get(1));
        assertEquals(2, map.get(2));
        assertEquals(3, map.get(0));
    }

    @Test
    void map() {
        assertEquals("[2, 4, 6]", $.just(1, 2, 3).map(i -> i * 2).value().toString());
        assertEquals("[11, 22, 33]", $.just(1, 2, 3).map((it, i) -> it + 10 * (i + 1)).value().toString());
    }

    @Test
    void orderBy() {
        assertEquals("[1, 2, 3, 4, 5]", $.just(1, 3, 5, 2, 4).orderBy(Function.identity(), Direction.ASC).value().toString());
        assertEquals("[5, 4, 3, 2, 1]", $.just(1, 3, 5, 2, 4).orderBy(Function.identity(), Direction.DESC).value().toString());
    }

    @Test
    void reduce() {
        assertEquals(20, $.just(1, 2, 3, 4).reduce(10, Integer::sum));
    }

    @Test
    void reverse() {
        assertEquals("[3, 2, 1]", $.just(1, 2, 3).reverse().value().toString());
    }

    @Test
    void take() {
        assertEquals("[1, 2]", $.just(1, 2, 3, 4).take(2).value().toString());
    }

    @Test
    void takeWhile() {
        assertEquals("[1, 2]", $.just(1, 2, 3, 4).takeWhile(i -> i < 3).value().toString());
    }

    @Test
    void unique() {
        assertEquals("[1, 2, 3]", $.just(1, 2, 1, 3).unique().value().toString());
    }

    @Test
    void uniqueBy() {
        assertEquals("[1, 2, 3]", $.just(1, 2, 1, 3, 4).uniqueBy(i -> i % 3).value().toString());
    }

    @Test
    void zip() {
        assertEquals("[(1, 1), (2, 2), (3, null)]", $.just(1, 2, 3).zip($.list(1, 2)).value().toString());
        assertEquals("[(1, 1), (2, 2), (3, 3)]", $.just(1, 2, 3).zip($.list(1, 2, 3)).value().toString());
        assertEquals("[(1, 1), (2, 2), (3, 3), (null, 4), (null, 5)]", $.just(1, 2, 3).zip($.list(1, 2, 3, 4, 5)).value().toString());

        assertEquals("[(1, null), (2, null), (3, null)]", $.just(1, 2, 3).zip($.list()).value().toString());
        assertEquals("[(1, null), (2, null), (3, null)]", $.just(1, 2, 3).zip(null).value().toString());
    }
}
