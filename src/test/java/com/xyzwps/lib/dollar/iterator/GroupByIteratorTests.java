package com.xyzwps.lib.dollar.iterator;

import com.xyzwps.lib.dollar.Pair;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static com.xyzwps.lib.dollar.Dollar.*;

@SuppressWarnings("ConstantValue")
class GroupByIteratorTests {

    @Test
    void cornerCases() {
        // null iterator
        {
            Iterator<Pair<Integer, List<Integer>>> itr = new GroupByIterator<>(null, Function.identity());
            assertFalse(itr.hasNext());
            assertThrows(NoSuchElementException.class, itr::next);
        }

        // null toKey function
        {
            List<Integer> list = $.list(1, 2, 3);
            assertThrows(NullPointerException.class, () -> new GroupByIterator<>(list.iterator(), null));
        }
    }

    @Test
    void commonCase() {
        List<Integer> list = $.list(1, 2, 3, 11, 12, 21);

        // common
        {
            Iterator<Pair<Integer, List<Integer>>> itr = new GroupByIterator<>(list.iterator(), i -> i % 10);
            Map<Integer, List<Integer>> map = new HashMap<>();

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            Pair<Integer, List<Integer>> p1 = itr.next();
            map.put(p1.key(), p1.value());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            Pair<Integer, List<Integer>> p2 = itr.next();
            map.put(p2.key(), p2.value());

            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            assertTrue(itr.hasNext());
            Pair<Integer, List<Integer>> p3 = itr.next();
            map.put(p3.key(), p3.value());

            assertFalse(itr.hasNext());

            assertEquals(3, map.size());
            assertEquals(map.get(1).toString(), "[1, 11, 21]");
            assertEquals(map.get(2).toString(), "[2, 12]");
            assertEquals(map.get(3).toString(), "[3]");
        }

        // just next
        {
            Iterator<Pair<Integer, List<Integer>>> itr = new GroupByIterator<>(list.iterator(), i -> i % 10);
            Map<Integer, List<Integer>> map = new HashMap<>();

            Pair<Integer, List<Integer>> p1 = itr.next();
            map.put(p1.key(), p1.value());
            Pair<Integer, List<Integer>> p2 = itr.next();
            map.put(p2.key(), p2.value());
            Pair<Integer, List<Integer>> p3 = itr.next();
            map.put(p3.key(), p3.value());
            assertThrows(NoSuchElementException.class, itr::next);

            assertEquals(3, map.size());
            assertEquals(map.get(1).toString(), "[1, 11, 21]");
            assertEquals(map.get(2).toString(), "[2, 12]");
            assertEquals(map.get(3).toString(), "[3]");
        }
    }
}
