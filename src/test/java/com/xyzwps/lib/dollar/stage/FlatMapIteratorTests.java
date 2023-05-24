package com.xyzwps.lib.dollar.stage;

import com.xyzwps.lib.dollar.iterator.FlatMapIterator;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static com.xyzwps.lib.dollar.Dollar.*;

@SuppressWarnings("ConstantValue")
class FlatMapIteratorTests {

    static final Function<Integer, Iterable<Integer>> fn = (i) -> {
        if (i == null) return null;
        if (i == 1) return $.arrayList(100, 101);
        if (i == 2) return $.arrayList(200, 201);
        if (i % 3 == 0) return null;
        if (i % 4 == 0) return $.arrayList();
        return $.arrayList(i);
    };

    @Test
    void cornerCases() {
        // null iterator
        {
            FlatMapIterator<Integer, Integer> itr = new FlatMapIterator<>(null, fn);
            assertFalse(itr.hasNext());
            assertThrows(NoSuchElementException.class, itr::next);
        }

        // null flatmap function
        {
            List<Integer> list = $.arrayList(1, 2, 3);
            assertThrows(NullPointerException.class, () -> new FlatMapIterator<>(list.iterator(), null));
        }
    }

    @Test
    void commonCases() {
        List<Integer> list = $.arrayList(1, 2, 3, 4, 5);

        // common
        {
            FlatMapIterator<Integer, Integer> itr = new FlatMapIterator<>(list.iterator(), fn);

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals(100, itr.next());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals(101, itr.next());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals(200, itr.next());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals(201, itr.next());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals(5, itr.next());

            assertFalse(itr.hasNext());
        }

        // just next
        {
            FlatMapIterator<Integer, Integer> itr = new FlatMapIterator<>(list.iterator(), fn);

            assertEquals(100, itr.next());
            assertEquals(101, itr.next());
            assertEquals(200, itr.next());
            assertEquals(201, itr.next());
            assertEquals(5, itr.next());
            assertThrows(NoSuchElementException.class, itr::next);
        }
    }

    static final Function<Integer, Iterable<Integer>> fn2 = (i) -> {
        if (i == null) return null;
        if (i == 1) return $.arrayList(1);
        if (i == 2) return $.arrayList(1, 2);
        if (i == 3) return $.arrayList(1, 2, 3);
        return $.arrayList(i, i, i);
    };

    @Test
    void commonCases2() {
        List<Integer> list = $.arrayList(1, 2, 3);

        // common
        {
            FlatMapIterator<Integer, Integer> itr = new FlatMapIterator<>(list.iterator(), fn2);

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals(1, itr.next());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals(1, itr.next());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals(2, itr.next());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals(1, itr.next());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals(2, itr.next());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals(3, itr.next());

            assertFalse(itr.hasNext());
        }

        // just next
        {
            FlatMapIterator<Integer, Integer> itr = new FlatMapIterator<>(list.iterator(), fn2);

            assertEquals(1, itr.next());
            assertEquals(1, itr.next());
            assertEquals(2, itr.next());
            assertEquals(1, itr.next());
            assertEquals(2, itr.next());
            assertEquals(3, itr.next());
            assertThrows(NoSuchElementException.class, itr::next);
        }
    }
}
