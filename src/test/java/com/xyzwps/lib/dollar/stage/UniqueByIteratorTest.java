package com.xyzwps.lib.dollar.stage;

import com.xyzwps.lib.dollar.iterator.UniqueByIterator;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;

import static com.xyzwps.lib.dollar.Dollar.$;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("ConstantValue")
class UniqueByIteratorTest {

    private static final List<Integer> list = $.arrayList(1, 2, 2, 3, 3, 3, 4);

    @Test
    void cornerCases() {
        // null key function
        assertThrows(NullPointerException.class, () -> new UniqueByIterator<>(list.iterator(), null));

        // null iterator
        {
            Iterator<Integer> itr = new UniqueByIterator<>((Iterator<Integer>) null, Function.identity());
            assertFalse(itr.hasNext());
            assertThrows(NoSuchElementException.class, itr::next);
        }
    }

    @Test
    void commonCases() {
        // common
        {
            Iterator<Integer> itr = new UniqueByIterator<>(list.iterator(), Function.identity());

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

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals(4, itr.next());

            assertFalse(itr.hasNext());
        }

        // just next
        {
            Iterator<Integer> itr = new UniqueByIterator<>(list.iterator(), Function.identity());

            assertEquals(1, itr.next());
            assertEquals(2, itr.next());
            assertEquals(3, itr.next());
            assertEquals(4, itr.next());
            assertThrows(NoSuchElementException.class, itr::next);
        }
    }

    @Test
    void commonCases2() {
        // common
        {
            Iterator<Integer> itr = new UniqueByIterator<>(list.iterator(), i -> i % 2);

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals(1, itr.next());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals(2, itr.next());

            assertFalse(itr.hasNext());
        }

        // just next
        {
            Iterator<Integer> itr = new UniqueByIterator<>(list.iterator(), i -> i % 2);

            assertEquals(1, itr.next());
            assertEquals(2, itr.next());
            assertThrows(NoSuchElementException.class, itr::next);
        }
    }
}
