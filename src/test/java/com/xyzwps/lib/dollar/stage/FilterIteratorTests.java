package com.xyzwps.lib.dollar.stage;

import com.xyzwps.lib.dollar.function.IndexedPredicate;
import com.xyzwps.lib.dollar.iterator.FilterIterator;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static com.xyzwps.lib.dollar.Dollar.*;

@SuppressWarnings("ConstantValue")
class FilterIteratorTests {

    static final IndexedPredicate<Integer> isEven = (i, index) -> i != null && i % 2 == 0;
    static final IndexedPredicate<Integer> isOdd = (i, index) -> i != null && i % 2 == 1;

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

    @Test
    void filterByIndex() {
        List<Integer> list = $.arrayList(1, 2, 3, 4, 5);
        IndexedPredicate<Integer> predicate = (i, index) -> index < 3;

        //  common
        {
            FilterIterator<Integer> itr = new FilterIterator<>(list.iterator(), predicate);

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

        //  just next
        {
            FilterIterator<Integer> itr = new FilterIterator<>(list.iterator(), predicate);

            assertEquals(1, itr.next());
            assertEquals(2, itr.next());
            assertEquals(3, itr.next());
            assertThrows(NoSuchElementException.class, itr::next);
        }
    }
}
