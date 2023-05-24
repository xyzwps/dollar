package com.xyzwps.lib.dollar.stage;


import com.xyzwps.lib.dollar.iterator.TakeIterator;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static com.xyzwps.lib.dollar.Dollar.$;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("ConstantValue")
class TakeIteratorTests {

    private static final List<Integer> list = $.arrayList(1, 2, 3, 4);

    @Test
    void cornerCases() {
        // invalid take size
        $.range(-50, 1).forEach(size -> assertThrows(
                IllegalArgumentException.class,
                () -> new TakeIterator<>((Iterator<Integer>) null, size)
        ));

        // null iterator
        {
            Iterator<Integer> itr = new TakeIterator<>((Iterator<Integer>) null, 10);
            assertFalse(itr.hasNext());
            assertThrows(NoSuchElementException.class, itr::next);
        }
    }

    @Test
    void commonCases() {
        // take 1 common
        {
            Iterator<Integer> itr = new TakeIterator<>(list.iterator(), 1);

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals(1, itr.next());

            assertFalse(itr.hasNext());
        }

        // take 1 just next
        {
            Iterator<Integer> itr = new TakeIterator<>(list.iterator(), 1);

            assertEquals(1, itr.next());
            assertThrows(NoSuchElementException.class, itr::next);
        }

        // take 2 common
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

        // take 2 just next
        {
            Iterator<Integer> itr = new TakeIterator<>(list.iterator(), 2);

            assertEquals(1, itr.next());
            assertEquals(2, itr.next());
            assertThrows(NoSuchElementException.class, itr::next);
        }

        // take 3 common
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

        // take 3 just next
        {
            Iterator<Integer> itr = new TakeIterator<>(list.iterator(), 3);

            assertEquals(1, itr.next());
            assertEquals(2, itr.next());
            assertEquals(3, itr.next());
            assertThrows(NoSuchElementException.class, itr::next);
        }

        // take 4 common
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

        // take 4 just next
        {
            Iterator<Integer> itr = new TakeIterator<>(list.iterator(), 4);

            assertEquals(1, itr.next());
            assertEquals(2, itr.next());
            assertEquals(3, itr.next());
            assertEquals(4, itr.next());
            assertThrows(NoSuchElementException.class, itr::next);
        }

        // take 5 common
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

        // take 5 just next
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
