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
    void orderBy() {
        assertEquals("[1, 2, 3, 4, 5]", $.just(1, 3, 5, 2, 4).orderBy(Function.identity(), Direction.ASC).value().toString());
        assertEquals("[5, 4, 3, 2, 1]", $.just(1, 3, 5, 2, 4).orderBy(Function.identity(), Direction.DESC).value().toString());
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
