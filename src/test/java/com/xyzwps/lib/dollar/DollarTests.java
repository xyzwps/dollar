package com.xyzwps.lib.dollar;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static com.xyzwps.lib.dollar.Dollar.*;
import static org.junit.jupiter.api.Assertions.*;

class DollarTests {

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
    void filter1() {
        assertEquals("[2, 4]", $.filter($.list(1, 2, 3, 4, 5), i -> i % 2 == 0).toString());

        List<Integer> nullList = null;
        assertEquals("[]", $.filter(nullList, i -> i % 2 == 0).toString());

        Predicate<Integer> nullPredicate = null;
        assertEquals("[1, 2, 3, 4, 5]", $.filter($.list(1, 2, 3, 4, 5), nullPredicate).toString());
    }

    @Test
    void filter2() {
        assertEquals("[1, 3, 5]", $.filter($.list(1, 2, 3, 4, 5), (e, i) -> i % 2 == 0).toString());

        List<Integer> nullList = null;
        assertEquals("[]", $.filter(nullList, (e, i) -> i % 2 == 0).toString());

        BiPredicate<Integer, Integer> nullPredicate = null;
        assertEquals("[1, 2, 3, 4, 5]", $.filter($.list(1, 2, 3, 4, 5), nullPredicate).toString());
    }

    @Test
    void concat() {
        assertEquals("[]", $.concat().toString());
        assertEquals("[]", $.concat(null, null).toString());
        assertEquals("[1, 2, 3, 4]", $.concat($.list(1, 2), $.list(3, 4)).toString());
        assertEquals("[1, 2, 3, 4]", $.concat($.list(1, 2), null, $.list(3, 4)).toString());
    }
}
