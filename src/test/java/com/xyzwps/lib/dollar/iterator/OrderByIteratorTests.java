package com.xyzwps.lib.dollar.iterator;

import com.xyzwps.lib.dollar.Direction;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static com.xyzwps.lib.dollar.Dollar.*;

@SuppressWarnings("ConstantValue")
class OrderByIteratorTests {

    private static final List<Integer> list = $.list(1, 3, 5, 2, 4, 6);

    @Test
    void cornerCases() {


        // null direction
        assertThrows(NullPointerException.class, () -> new OrderByIterator<>(list.iterator(), Function.identity(), null));

        // null key function
        assertThrows(NullPointerException.class, () -> new OrderByIterator<>(list.iterator(), null, Direction.ASC));

        // null iterator
        {
            Iterator<Integer> itr = new OrderByIterator<>((Iterator<Integer>) null, Function.identity(), Direction.ASC);
            assertFalse(itr.hasNext());
            assertThrows(NoSuchElementException.class, itr::next);
        }
    }

    @Test
    void asc() {
        // common
        {
            Iterator<Integer> itr = new OrderByIterator<>(list.iterator(), Function.identity(), Direction.ASC);

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

            assertFalse(itr.hasNext());
        }

        // just next
        {
            Iterator<Integer> itr = new OrderByIterator<>(list.iterator(), Function.identity(), Direction.ASC);

            assertEquals(1, itr.next());
            assertEquals(2, itr.next());
            assertEquals(3, itr.next());
            assertEquals(4, itr.next());
            assertEquals(5, itr.next());
            assertEquals(6, itr.next());
            assertThrows(NoSuchElementException.class, itr::next);
        }

    }

    @Test
    void desc() {
        // common
        {
            Iterator<Integer> itr = new OrderByIterator<>(list.iterator(), Function.identity(), Direction.DESC);

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
            Iterator<Integer> itr = new OrderByIterator<>(list.iterator(), Function.identity(), Direction.DESC);

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
