package com.xyzwps.lib.dollar.stage;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;

import static com.xyzwps.lib.dollar.Dollar.$;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("ConstantValue")
class ZipIteratorTests {

    @Test
    void nullCombineFn() {
        List<Integer> left = $.arrayList(1, 2);
        List<Integer> right = $.arrayList(10, 20);
        assertThrows(NullPointerException.class,
                () -> new ZipIterator<>(left.iterator(), right.iterator(), null));
    }

    @Test
    void bothNull() {
        Iterator<Integer> itr = new ZipIterator<>(null, null, Integer::sum);
        assertFalse(itr.hasNext());
        assertThrows(NoSuchElementException.class, itr::next);
    }

    static final BiFunction<Integer, Integer, String> combineFn =
            (l, r) -> (l == null ? "" : String.valueOf(l)) + "-" + (r == null ? "" : String.valueOf(r));

    @Test
    void leftNull() {
        List<Integer> right = $.arrayList(10, 20);

        // common
        {
            Iterator<String> itr = new ZipIterator<>(null, right.iterator(), combineFn);

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals("-10", itr.next());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals("-20", itr.next());

            assertFalse(itr.hasNext());
        }


        // just next
        {
            Iterator<String> itr = new ZipIterator<>(null, right.iterator(), combineFn);

            assertEquals("-10", itr.next());
            assertEquals("-20", itr.next());

            assertThrows(NoSuchElementException.class, itr::next);
        }
    }

    @Test
    void rightNull() {
        List<Integer> left = $.arrayList(10, 20);

        // common
        {
            Iterator<String> itr = new ZipIterator<>(left.iterator(), null, combineFn);

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals("10-", itr.next());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals("20-", itr.next());

            assertFalse(itr.hasNext());
        }


        // just next
        {
            Iterator<String> itr = new ZipIterator<>(left.iterator(), null, combineFn);

            assertEquals("10-", itr.next());
            assertEquals("20-", itr.next());

            assertThrows(NoSuchElementException.class, itr::next);
        }
    }

    @Test
    void leftShort() {
        List<Integer> left = $.arrayList(1, 2);
        List<Integer> right = $.arrayList(10, 20, 30, 40);

        // common
        {
            Iterator<String> itr = new ZipIterator<>(left.iterator(), right.iterator(), combineFn);

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals("1-10", itr.next());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals("2-20", itr.next());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals("-30", itr.next());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals("-40", itr.next());

            assertFalse(itr.hasNext());
        }


        // just next
        {
            Iterator<String> itr = new ZipIterator<>(left.iterator(), right.iterator(), combineFn);

            assertEquals("1-10", itr.next());
            assertEquals("2-20", itr.next());
            assertEquals("-30", itr.next());
            assertEquals("-40", itr.next());

            assertThrows(NoSuchElementException.class, itr::next);
        }
    }

    @Test
    void rightShort() {
        List<Integer> left = $.arrayList(1, 2, 3, 4);
        List<Integer> right = $.arrayList(10, 20);

        // common
        {
            Iterator<String> itr = new ZipIterator<>(left.iterator(), right.iterator(), combineFn);

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals("1-10", itr.next());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals("2-20", itr.next());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals("3-", itr.next());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals("4-", itr.next());

            assertFalse(itr.hasNext());
        }


        // just next
        {
            Iterator<String> itr = new ZipIterator<>(left.iterator(), right.iterator(), combineFn);

            assertEquals("1-10", itr.next());
            assertEquals("2-20", itr.next());
            assertEquals("3-", itr.next());
            assertEquals("4-", itr.next());

            assertThrows(NoSuchElementException.class, itr::next);
        }
    }

    @Test
    void lengthEqual() {
        List<Integer> left = $.arrayList(1, 2, 3, 4);
        List<Integer> right = $.arrayList(10, 20, 30, 40);

        // common
        {
            Iterator<String> itr = new ZipIterator<>(left.iterator(), right.iterator(), combineFn);

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals("1-10", itr.next());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals("2-20", itr.next());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals("3-30", itr.next());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals("4-40", itr.next());

            assertFalse(itr.hasNext());
        }


        // just next
        {
            Iterator<String> itr = new ZipIterator<>(left.iterator(), right.iterator(), combineFn);

            assertEquals("1-10", itr.next());
            assertEquals("2-20", itr.next());
            assertEquals("3-30", itr.next());
            assertEquals("4-40", itr.next());

            assertThrows(NoSuchElementException.class, itr::next);
        }
    }
}
