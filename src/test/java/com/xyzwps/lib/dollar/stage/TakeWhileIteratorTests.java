package com.xyzwps.lib.dollar.stage;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static com.xyzwps.lib.dollar.Dollar.$;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("ConstantValue")
class TakeWhileIteratorTests {

    private static final List<Integer> list = $.arrayList(1, 2, 3, 4);

    @Test
    void cornerCases() {
        // null predicate
        assertThrows(NullPointerException.class, () -> new TakeWhileIterator<>(list.iterator(), null));

        // null iterator
        {
            Iterator<Integer> itr = new TakeWhileIterator<>((Iterator<Integer>) null, i -> i < 3);
            assertFalse(itr.hasNext());
            assertThrows(NoSuchElementException.class, itr::next);
        }
    }

    @Test
    void takeNothing() {
        Iterator<Integer> itr = new TakeWhileIterator<>(list.iterator(), i -> i > 100);
        assertFalse(itr.hasNext());
        assertThrows(NoSuchElementException.class, itr::next);
    }

    @Test
    void takeSome() {
        // common
        {
            Iterator<Integer> itr = new TakeWhileIterator<>(list.iterator(), i -> i < 4);

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
            Iterator<Integer> itr = new TakeWhileIterator<>(list.iterator(), i -> i < 4);

            assertEquals(1, itr.next());
            assertEquals(2, itr.next());
            assertEquals(3, itr.next());
            assertThrows(NoSuchElementException.class, itr::next);
        }

    }

    @Test
    void takeAll() {
        // common
        {
            Iterator<Integer> itr = new TakeWhileIterator<>(list.iterator(), i -> i < 100);

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
            Iterator<Integer> itr = new TakeWhileIterator<>(list.iterator(), i -> i < 100);

            assertEquals(1, itr.next());
            assertEquals(2, itr.next());
            assertEquals(3, itr.next());
            assertEquals(4, itr.next());
            assertThrows(NoSuchElementException.class, itr::next);
        }

    }
}
