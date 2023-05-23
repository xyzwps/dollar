package com.xyzwps.lib.dollar.stage;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static com.xyzwps.lib.dollar.Dollar.*;

@SuppressWarnings("ConstantValue")
class FilterIteratorTests {

    static final Predicate<Integer> isEven = (i) -> i != null && i % 2 == 0;
    static final Predicate<Integer> isOdd = (i) -> i != null && i % 2 == 1;

    @Test
    void cornerCases() {
        // null predicate
        {
            List<Integer> list = $.arrayList(1, 2, 3);
            assertThrows(NullPointerException.class, () -> new FilterIterator<>(list.iterator(), null));
        }

        // null iterator
        {
            FilterIterator<Integer> itr = new FilterIterator<>(null, isEven);
            assertFalse(itr.hasNext());
            assertThrows(NoSuchElementException.class, itr::next);
        }
    }

    @Test
    void commonCases() {
        List<Integer> list = $.arrayList(1, 2, 3, 4, 5);

        // even common
        {
            FilterIterator<Integer> itr = new FilterIterator<>(list.iterator(), isEven);

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals(2, itr.next());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals(4, itr.next());

            assertFalse(itr.hasNext());
        }

        // even just next
        {
            FilterIterator<Integer> itr = new FilterIterator<>(list.iterator(), isEven);

            assertEquals(2, itr.next());
            assertEquals(4, itr.next());
            assertThrows(NoSuchElementException.class, itr::next);
        }

        // odd common
        {
            FilterIterator<Integer> itr = new FilterIterator<>(list.iterator(), isOdd);

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals(1, itr.next());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals(3, itr.next());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals(5, itr.next());

            assertFalse(itr.hasNext());
        }

        // odd just next
        {
            FilterIterator<Integer> itr = new FilterIterator<>(list.iterator(), isOdd);

            assertEquals(1, itr.next());
            assertEquals(3, itr.next());
            assertEquals(5, itr.next());
            assertThrows(NoSuchElementException.class, itr::next);
        }

    }
}
