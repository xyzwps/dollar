package com.xyzwps.lib.dollar.stage;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static com.xyzwps.lib.dollar.Dollar.*;

@SuppressWarnings("ConstantValue")
class ReverseIteratorTests {

    private static final List<Integer> list = $.arrayList(1, 2, 3, 4, 5, 6);

    @Test
    void cornerCases() {
        Iterator<Integer> itr = new ReverseIterator<>((Iterator<Integer>) null);
        assertFalse(itr.hasNext());
        assertThrows(NoSuchElementException.class, itr::next);
    }

    @Test
    void commonCases() {
        // common
        {
            Iterator<Integer> itr = new ReverseIterator<>(list.iterator());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals(6, itr.next());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals(5, itr.next());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals(4, itr.next());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals(3, itr.next());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals(2, itr.next());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals(1, itr.next());

            assertFalse(itr.hasNext());
        }

        // just next
        {
            Iterator<Integer> itr = new ReverseIterator<>(list.iterator());

            assertEquals(6, itr.next());
            assertEquals(5, itr.next());
            assertEquals(4, itr.next());
            assertEquals(3, itr.next());
            assertEquals(2, itr.next());
            assertEquals(1, itr.next());
            assertThrows(NoSuchElementException.class, itr::next);
        }

    }

}
