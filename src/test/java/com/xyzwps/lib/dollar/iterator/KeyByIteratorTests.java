package com.xyzwps.lib.dollar.iterator;

import com.xyzwps.lib.dollar.Pair;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static com.xyzwps.lib.dollar.Dollar.*;

@SuppressWarnings("ConstantValue")
class KeyByIteratorTests {

    @Test
    void cornerCases() {
        // null iterator
        {
            Iterator<Pair<Integer, Integer>> itr = new KeyByIterator<>(null, Function.identity());
            assertFalse(itr.hasNext());
            assertThrows(NoSuchElementException.class, itr::next);
        }

        // null toKey function
        {
            List<Integer> list = $.list(1, 2, 3);
            assertThrows(NullPointerException.class, () -> new KeyByIterator<>(list.iterator(), null));
        }
    }

    @Test
    void commonCase() {
        List<Integer> list = $.list(1, 1, 1111, 2, 2222, 3, 11, 12, 21);

        // common
        {
            Iterator<Pair<Integer, Integer>> itr = new KeyByIterator<>(list.iterator(), i -> i % 10);

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals("(1, 1)", itr.next().toString());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals("(2, 2)", itr.next().toString());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals("(3, 3)", itr.next().toString());

            assertFalse(itr.hasNext());
        }

        // just next
        {
            Iterator<Pair<Integer, Integer>> itr = new KeyByIterator<>(list.iterator(), i -> i % 10);

            assertEquals("(1, 1)", itr.next().toString());
            assertEquals("(2, 2)", itr.next().toString());
            assertEquals("(3, 3)", itr.next().toString());
            assertThrows(NoSuchElementException.class, itr::next);
        }
    }
}
