package com.xyzwps.lib.dollar.iterator;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static com.xyzwps.lib.dollar.Dollar.$;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("ConstantValue")
class TakeIteratorTests {

    private static final List<Integer> list = $.listOf(1, 2, 3, 4);

    @Test
    void cornerCases() {
        // n <= 0
        for (int i = 0; i > -100; i--) {
            final int n = i;
            assertThrows(IllegalArgumentException.class, () -> new TakeIterator<>(list.iterator(), n));
        }

        // null iterator
        {
            Iterator<Integer> itr = new TakeIterator<>((Iterator<Integer>) null, 3);
            assertFalse(itr.hasNext());
            assertThrows(NoSuchElementException.class, itr::next);
        }
    }

    @Test
    void take1() {
        // common
        {
            Iterator<Integer> itr = new TakeIterator<>(list.iterator(), 1);

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals(1, itr.next());

            assertFalse(itr.hasNext());
        }

        // just next
        {
            Iterator<Integer> itr = new TakeIterator<>(list.iterator(), 1);

            assertEquals(1, itr.next());
            assertThrows(NoSuchElementException.class, itr::next);
        }
    }

    @Test
    void take2() {
        // common
        {
            Iterator<Integer> itr = new TakeIterator<>(list.iterator(), 2);

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
            Iterator<Integer> itr = new TakeIterator<>(list.iterator(), 2);

            assertEquals(1, itr.next());
            assertEquals(2, itr.next());
            assertThrows(NoSuchElementException.class, itr::next);
        }
    }

    @Test
    void take3() {
        // common
        {
            Iterator<Integer> itr = new TakeIterator<>(list.iterator(), 3);

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
            Iterator<Integer> itr = new TakeIterator<>(list.iterator(), 3);

            assertEquals(1, itr.next());
            assertEquals(2, itr.next());
            assertEquals(3, itr.next());
            assertThrows(NoSuchElementException.class, itr::next);
        }

    }

    @Test
    void take4() {
        // common
        {
            Iterator<Integer> itr = new TakeIterator<>(list.iterator(), 4);

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
            Iterator<Integer> itr = new TakeIterator<>(list.iterator(), 4);

            assertEquals(1, itr.next());
            assertEquals(2, itr.next());
            assertEquals(3, itr.next());
            assertEquals(4, itr.next());
            assertThrows(NoSuchElementException.class, itr::next);
        }
    }

    @Test
    void take5() {
        // common
        {
            Iterator<Integer> itr = new TakeIterator<>(list.iterator(), 5);

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
            Iterator<Integer> itr = new TakeIterator<>(list.iterator(), 5);

            assertEquals(1, itr.next());
            assertEquals(2, itr.next());
            assertEquals(3, itr.next());
            assertEquals(4, itr.next());
            assertThrows(NoSuchElementException.class, itr::next);
        }
    }
}
