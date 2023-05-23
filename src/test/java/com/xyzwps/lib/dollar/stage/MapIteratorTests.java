package com.xyzwps.lib.dollar.stage;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static com.xyzwps.lib.dollar.Dollar.*;

@SuppressWarnings("ConstantValue")
class MapIteratorTests {

    @Test
    void cornerCases() {
        // null map function
        {
            List<Integer> list = $.arrayList(1, 2, 3, 4);
            assertThrows(NullPointerException.class, () -> new MapIterator<>(list.iterator(), null));
        }

        // null iterator
        {
            MapIterator<Integer, String> itr = new MapIterator<>(null, Object::toString);
            assertFalse(itr.hasNext());
            assertThrows(NoSuchElementException.class, itr::next);
        }
    }

    @Test
    void commonCases() {
        List<Integer> list = $.arrayList(1, 2, 3, 4);

        // common
        {
            MapIterator<Integer, String> itr = new MapIterator<>(list.iterator(), Object::toString);

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

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals("4", itr.next());

            assertFalse(itr.hasNext());
        }

        // just next
        {
            MapIterator<Integer, String> itr = new MapIterator<>(list.iterator(), Object::toString);

            assertEquals("1", itr.next());
            assertEquals("2", itr.next());
            assertEquals("3", itr.next());
            assertEquals("4", itr.next());
            assertThrows(NoSuchElementException.class, itr::next);
        }

    }
}
