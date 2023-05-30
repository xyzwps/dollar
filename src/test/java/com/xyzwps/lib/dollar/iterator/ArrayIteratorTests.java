package com.xyzwps.lib.dollar.iterator;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("ConstantValue")
class ArrayIteratorTests {

    @Test
    void cornerCases() {
        assertThrows(NullPointerException.class, () -> new ArrayIterator<>(null));
    }

    @Test
    void commonCases() {
        String[] strs = new String[]{"1", "2", "3"};
        // common
        {
            Iterator<String> itr = new ArrayIterator<>(strs);

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals("1", itr.next());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals("2", itr.next());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals("3", itr.next());

            assertFalse(itr.hasNext());
        }

        // just next
        {
            Iterator<String> itr = new ArrayIterator<>(strs);
            assertEquals("1", itr.next());
            assertEquals("2", itr.next());
            assertEquals("3", itr.next());
            assertThrows(NoSuchElementException.class, itr::next);
        }
    }
}
