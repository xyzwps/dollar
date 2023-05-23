package com.xyzwps.lib.dollar.stage;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static com.xyzwps.lib.dollar.Dollar.*;

@SuppressWarnings("ConstantValue")
class ChunkIteratorTests {

    @Test
    void cornerCases() {
        // invalid chunk size
        {
            List<Integer> list = $.arrayList(1, 2, 3, 4, 5, 6, 7);
            $.range(-50, 1).forEach(i -> assertThrows(
                    IllegalArgumentException.class,
                    () -> new ChunkIterator<>(list.iterator(), i)
            ));
        }

        // null iterator
        {
            ChunkIterator<Integer> itr = new ChunkIterator<>(null, 10);
            assertFalse(itr.hasNext());
            assertThrows(NoSuchElementException.class, itr::next);
        }
    }

    @Test
    void commonCases() {
        List<Integer> list = $.arrayList(1, 2, 3, 4, 5, 6, 7);

        // chunk 1 common
        {
            ChunkIterator<Integer> itr = new ChunkIterator<>(list.iterator(), 1);

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals("[1]", itr.next().toString());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals("[2]", itr.next().toString());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals("[3]", itr.next().toString());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals("[4]", itr.next().toString());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals("[5]", itr.next().toString());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals("[6]", itr.next().toString());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals("[7]", itr.next().toString());

            assertFalse(itr.hasNext());
        }

        // chunk 1 just next
        {
            ChunkIterator<Integer> itr = new ChunkIterator<>(list.iterator(), 1);
            assertEquals("[1]", itr.next().toString());
            assertEquals("[2]", itr.next().toString());
            assertEquals("[3]", itr.next().toString());
            assertEquals("[4]", itr.next().toString());
            assertEquals("[5]", itr.next().toString());
            assertEquals("[6]", itr.next().toString());
            assertEquals("[7]", itr.next().toString());
            assertThrows(NoSuchElementException.class, itr::next);
        }

        // chunk 2 common
        {
            ChunkIterator<Integer> itr = new ChunkIterator<>(list.iterator(), 2);

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals("[1, 2]", itr.next().toString());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals("[3, 4]", itr.next().toString());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals("[5, 6]", itr.next().toString());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals("[7]", itr.next().toString());

            assertFalse(itr.hasNext());
        }

        // chunk 2 just next
        {
            ChunkIterator<Integer> itr = new ChunkIterator<>(list.iterator(), 2);
            assertEquals("[1, 2]", itr.next().toString());
            assertEquals("[3, 4]", itr.next().toString());
            assertEquals("[5, 6]", itr.next().toString());
            assertEquals("[7]", itr.next().toString());
            assertThrows(NoSuchElementException.class, itr::next);
        }

        // chunk 3 common
        {
            ChunkIterator<Integer> itr = new ChunkIterator<>(list.iterator(), 3);

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals("[1, 2, 3]", itr.next().toString());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals("[4, 5, 6]", itr.next().toString());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals("[7]", itr.next().toString());

            assertFalse(itr.hasNext());
        }

        // chunk 3 just next
        {
            ChunkIterator<Integer> itr = new ChunkIterator<>(list.iterator(), 3);
            assertEquals("[1, 2, 3]", itr.next().toString());
            assertEquals("[4, 5, 6]", itr.next().toString());
            assertEquals("[7]", itr.next().toString());
            assertThrows(NoSuchElementException.class, itr::next);
        }

        // chunk 4 common
        {
            ChunkIterator<Integer> itr = new ChunkIterator<>(list.iterator(), 4);

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals("[1, 2, 3, 4]", itr.next().toString());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals("[5, 6, 7]", itr.next().toString());

            assertFalse(itr.hasNext());
        }

        // chunk 4 just next
        {
            ChunkIterator<Integer> itr = new ChunkIterator<>(list.iterator(), 4);
            assertEquals("[1, 2, 3, 4]", itr.next().toString());
            assertEquals("[5, 6, 7]", itr.next().toString());
            assertThrows(NoSuchElementException.class, itr::next);
        }

        // chunk 5 common
        {
            ChunkIterator<Integer> itr = new ChunkIterator<>(list.iterator(), 5);

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals("[1, 2, 3, 4, 5]", itr.next().toString());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals("[6, 7]", itr.next().toString());

            assertFalse(itr.hasNext());
        }

        // chunk 5 just next
        {
            ChunkIterator<Integer> itr = new ChunkIterator<>(list.iterator(), 5);
            assertEquals("[1, 2, 3, 4, 5]", itr.next().toString());
            assertEquals("[6, 7]", itr.next().toString());
            assertThrows(NoSuchElementException.class, itr::next);
        }

        // chunk 6 common
        {
            ChunkIterator<Integer> itr = new ChunkIterator<>(list.iterator(), 6);

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals("[1, 2, 3, 4, 5, 6]", itr.next().toString());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals("[7]", itr.next().toString());

            assertFalse(itr.hasNext());
        }

        // chunk 6 just next
        {
            ChunkIterator<Integer> itr = new ChunkIterator<>(list.iterator(), 6);
            assertEquals("[1, 2, 3, 4, 5, 6]", itr.next().toString());
            assertEquals("[7]", itr.next().toString());
            assertThrows(NoSuchElementException.class, itr::next);
        }

        // chunk 7 common
        {
            ChunkIterator<Integer> itr = new ChunkIterator<>(list.iterator(), 7);

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals("[1, 2, 3, 4, 5, 6, 7]", itr.next().toString());

            assertFalse(itr.hasNext());
        }

        // chunk 7 just next
        {
            ChunkIterator<Integer> itr = new ChunkIterator<>(list.iterator(), 7);
            assertEquals("[1, 2, 3, 4, 5, 6, 7]", itr.next().toString());
            assertThrows(NoSuchElementException.class, itr::next);
        }

        // chunk 8 common
        {
            ChunkIterator<Integer> itr = new ChunkIterator<>(list.iterator(), 8);

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertEquals("[1, 2, 3, 4, 5, 6, 7]", itr.next().toString());

            assertFalse(itr.hasNext());
        }

        // chunk 8 just next
        {
            ChunkIterator<Integer> itr = new ChunkIterator<>(list.iterator(), 8);
            assertEquals("[1, 2, 3, 4, 5, 6, 7]", itr.next().toString());
            assertThrows(NoSuchElementException.class, itr::next);
        }

    }
}
