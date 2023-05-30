package com.xyzwps.lib.dollar.iterator;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static com.xyzwps.lib.dollar.Dollar.*;

@SuppressWarnings("ConstantValue")
class ArrayListReverseIteratorTests {

    @Test
    void cornerCases() {
        assertThrows(NullPointerException.class, () -> new ArrayListReverseIterator<>(null));
    }

    @Test
    void commonCases() {
        ArrayList<String> strs = (ArrayList<String>) $.list("1", "2", "3");

        // common
        {
            Iterator<String> itr = new ArrayListReverseIterator<>(strs);

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals("3", itr.next());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals("2", itr.next());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals("1", itr.next());

            assertFalse(itr.hasNext());
        }

        // just next
        {
            Iterator<String> itr = new ArrayListReverseIterator<>(strs);
            assertEquals("3", itr.next());
            assertEquals("2", itr.next());
            assertEquals("1", itr.next());
            assertThrows(NoSuchElementException.class, itr::next);
        }
    }
}
