package com.xyzwps.dollar;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;

import static com.xyzwps.dollar.Dollar.*;
import static org.junit.jupiter.api.Assertions.*;

class ListApiTests {

    @Test
    void chunk() {
        String[] cases = new String[]{
                "wont test",
                "[[1], [2], [3], [4], [5], [6]]",
                "[[1, 2], [3, 4], [5, 6]]",
                "[[1, 2, 3], [4, 5, 6]]",
                "[[1, 2, 3, 4], [5, 6]]",
                "[[1, 2, 3, 4, 5], [6]]",
                "[[1, 2, 3, 4, 5, 6]]",
                "[[1, 2, 3, 4, 5, 6]]"
        };
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6);
        for (int i = 1; i < cases.length; i++) {
            assertEquals(cases[i], $(list).chunk(i).value().toString());
        }

        assertThrows(IllegalArgumentException.class, () -> $(list).chunk(0).value());
    }

    @Test
    void compact() {
        assertEquals("[a, ]", $("a", "", null).compact().value().toString());
    }

    @Test
    void filter() {
        assertEquals("[a, ]", $("a", "", null).filter(Objects::nonNull).value().toString());
    }

    @Test
    void first() {
        assertEquals(Optional.of(1), $(1, 2).first());
        assertEquals(Optional.empty(), $().first());
    }

    @Test
    void flatMap() {
        assertEquals(
                "[11, 12, 21, 22]",
                $(1, 2).flatMap(i -> $(i * 10 + 1, i * 10 + 2)).value().toString()
        );
    }

    @Test
    void flatten() {
        assertEquals(
                "[11, 12, 21, 22]",
                $(1, 2).flatten(i -> Arrays.asList(i * 10 + 1, i * 10 + 2)).value().toString()
        );
    }

    @Test
    void forEach() {
        List<Integer> t = new ArrayList<>();
        int count = $(1, 2, 3).forEach(t::add);
        assertEquals(3, count);
        assertEquals("[1, 2, 3]", t.toString());
    }

    @Test
    void groupBy() {
        Map<Integer, List<Integer>> map = $(1, 4, 7, 2, 5, 3).groupBy(i -> i % 3).value();
        assertEquals(3, map.size());
        assertEquals("[1, 4, 7]", map.get(1).toString());
        assertEquals("[2, 5]", map.get(2).toString());
        assertEquals("[3]", map.get(0).toString());
    }

    @Test
    void keyBy() {
        Map<Integer, Integer> map = $(1, 4, 7, 2, 5, 3).keyBy(i -> i % 3).value();
        assertEquals(3, map.size());
        assertEquals(1, map.get(1));
        assertEquals(2, map.get(2));
        assertEquals(3, map.get(0));
    }

    @Test
    void map() {
        assertEquals("[2, 4, 6]", $(1, 2, 3).map(i -> i * 2).value().toString());
    }

    @Test
    void orderBy() {
        assertEquals("[1, 2, 3, 4, 5]", $(1, 3, 5, 2, 4).orderBy(Function.identity(), Direction.ASC).value().toString());
        assertEquals("[5, 4, 3, 2, 1]", $(1, 3, 5, 2, 4).orderBy(Function.identity(), Direction.DESC).value().toString());
    }

    @Test
    void reduce() {
        assertEquals(20, $(1, 2, 3, 4).reduce(10, Integer::sum));
    }

    @Test
    void reverse() {
        assertEquals("[3, 2, 1]", $(1, 2, 3).reverse().value().toString());
    }

    @Test
    void take() {
        assertEquals("[1, 2]", $(1, 2, 3, 4).take(2).value().toString());
    }

    @Test
    void takeWhile() {
        assertEquals("[1, 2]", $(1, 2, 3, 4).takeWhile(i -> i < 3).value().toString());
    }

    @Test
    void unique() {
        assertEquals("[1, 2, 3]", $(1, 2, 1, 3).unique().value().toString());
    }

    @Test
    void uniqueBy() {
        assertEquals("[1, 2, 3]", $(1, 2, 1, 3, 4).uniqueBy(i -> i % 3).value().toString());
    }
}
