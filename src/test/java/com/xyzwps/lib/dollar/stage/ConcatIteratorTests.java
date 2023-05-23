package com.xyzwps.lib.dollar.stage;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static com.xyzwps.lib.dollar.Dollar.*;

@SuppressWarnings("ConstantValue")
class ConcatIteratorTests {

    @Test
    void bothEmpty() {
        ConcatIterator<Integer> itr = new ConcatIterator<>(null, null);
        assertFalse(itr.hasNext());
        assertThrows(NoSuchElementException.class, itr::next);
    }

    @Test
    void leftEmpty() {
        List<Integer> left = $.arrayList(1, 2, 3, 4);

        // common
        {
            ConcatIterator<Integer> itr = new ConcatIterator<>(left.iterator(), null);

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
            assertThrows(NoSuchElementException.class, itr::next);
        }

        // just next
        {
            ConcatIterator<Integer> itr = new ConcatIterator<>(left.iterator(), null);
            assertEquals(1, itr.next());
            assertEquals(2, itr.next());
            assertEquals(3, itr.next());
            assertEquals(4, itr.next());
            assertThrows(NoSuchElementException.class, itr::next);
        }
    }

    @Test
    void rightEmpty() {
        List<Integer> right = $.arrayList(5, 6, 7);

        // common
        {
            ConcatIterator<Integer> itr = new ConcatIterator<>(null, right.iterator());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals(5, itr.next());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals(6, itr.next());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals(7, itr.next());

            assertFalse(itr.hasNext());
            assertThrows(NoSuchElementException.class, itr::next);
        }

        // just next
        {
            ConcatIterator<Integer> itr = new ConcatIterator<>(null, right.iterator());

            assertEquals(5, itr.next());
            assertEquals(6, itr.next());
            assertEquals(7, itr.next());
            assertThrows(NoSuchElementException.class, itr::next);
        }
    }

    @Test
    void bothNotEmpty() {
        List<Integer> left = $.arrayList(1, 2, 3, 4);
        List<Integer> right = $.arrayList(5, 6, 7);

        // common
        {
            ConcatIterator<Integer> itr = new ConcatIterator<>(left.iterator(), right.iterator());

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

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals(5, itr.next());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals(6, itr.next());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals(7, itr.next());

            assertFalse(itr.hasNext());
            assertThrows(NoSuchElementException.class, itr::next);
        }

        // just next
        {
            ConcatIterator<Integer> itr = new ConcatIterator<>(left.iterator(), right.iterator());

            assertEquals(1, itr.next());
            assertEquals(2, itr.next());
            assertEquals(3, itr.next());
            assertEquals(4, itr.next());
            assertEquals(5, itr.next());
            assertEquals(6, itr.next());
            assertEquals(7, itr.next());
            assertThrows(NoSuchElementException.class, itr::next);
        }
    }
}
