package com.xyzwps.lib.dollar;

import com.xyzwps.lib.dollar.function.ObjIntPredicate;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.xyzwps.lib.dollar.Dollar.$;
import static org.junit.jupiter.api.Assertions.*;

class ListTests {

    @Test
    void zip2() {
        assertEquals("[(1, 1), (2, 2), (3, null)]", $.just(1, 2, 3).zip($.list(1, 2)).value().toString());
        assertEquals("[(1, 1), (2, 2), (3, 3)]", $.just(1, 2, 3).zip($.list(1, 2, 3)).value().toString());
        assertEquals("[(1, 1), (2, 2), (3, 3), (null, 4), (null, 5)]", $.just(1, 2, 3).zip($.list(1, 2, 3, 4, 5)).value().toString());

        assertEquals("[(1, null), (2, null), (3, null)]", $.just(1, 2, 3).zip($.list()).value().toString());
        assertEquals("[(1, null), (2, null), (3, null)]", $.just(1, 2, 3).zip(null).value().toString());

        assertThrows(NullPointerException.class, () -> $($.list(1)).zip($.list(2), null));

        assertEquals($((List<Integer>) null).zip(null).value().size(), 0);
    }

    @Test
    void unique() {
        assertEquals("[1, 2, 3]", $.just(1, 2, 1, 3).unique().value().toString());
    }

    @Test
    void uniqueBy() {
        assertEquals("[1, 2, 3]", $.just(1, 2, 1, 3, 4).uniqueBy(i -> i % 3).value().toString());
    }

    @Test
    void takeWhile() {
        assertEquals("[1, 2]", $.just(1, 2, 3, 4).takeWhile(i -> i < 3).value().toString());
    }

    @Test
    void take() {
        assertEquals("[1, 2]", $.just(1, 2, 3, 4).take(2).value().toString());
    }

    @Test
    void reverse() {
        assertEquals("[3, 2, 1]", $.just(1, 2, 3).reverse().value().toString());
        assertEquals("[4, 3, 2, 1]", $.just(1, 2, 3, 4).reverse().value().toString());
    }

    @Test
    void reduce() {
        assertEquals(20, $.just(1, 2, 3, 4).reduce(10, Integer::sum));
    }

    @Test
    void orderBy() {
        assertEquals("[1, 2, 3, 4, 5]", $.just(1, 3, 5, 2, 4).orderBy(Function.identity(), Direction.ASC).value().toString());
        assertEquals("[5, 4, 3, 2, 1]", $.just(1, 3, 5, 2, 4).orderBy(Function.identity(), Direction.DESC).value().toString());
    }

    @Test
    void map1() {
        assertEquals("[2, 4, 6]", $.just(1, 2, 3).map(i -> i * 2).value().toString());
    }

    @Test
    void map2() {
        assertEquals("[11, 22, 33]", $.just(1, 2, 3).map((it, i) -> it + 10 * (i + 1)).value().toString());
    }

    @Test
    void keyBy() {
        Map<Integer, Integer> map = $.just(1, 4, 7, 2, 5, 3).keyBy(i -> i % 3).value();
        assertEquals(3, map.size());
        assertEquals(1, map.get(1));
        assertEquals(2, map.get(2));
        assertEquals(3, map.get(0));
    }

    @Test
    void groupBy() {
        // TODO: 改用 HashMap 和 ArrayList。如何理解 HashMap and ArrayList preferred
        Map<Integer, List<Integer>> map = $.just(1, 4, 7, 2, 5, 3).groupBy(i -> i % 3).value();
        assertEquals(3, map.size());
        assertEquals("[1, 4, 7]", map.get(1).toString());
        assertEquals("[2, 5]", map.get(2).toString());
        assertEquals("[3]", map.get(0).toString());
    }

    @Test
    void forEach1() {
        List<Integer> t = new ArrayList<>();
        $.just(1, 2, 3).forEach(i -> t.add(i));
        assertEquals("[1, 2, 3]", t.toString());

    }

    @Test
    void forEach2() {
        List<Integer> t = new ArrayList<>();
        $.just(1, 2, 3).forEach((it, index) -> t.add(it + (index + 1) * 10));
        assertEquals("[11, 22, 33]", t.toString());
    }

    @Test
    void flatMap() {
        assertEquals(
                "[11, 12, 21, 22]",
                $.just(1, 2).flatMap(i -> $.just(i * 10 + 1, i * 10 + 2)).value().toString()
        );
    }

    @Test
    void first() {
        assertEquals(Optional.of(1), $.just(1, 2).first());
        assertEquals(Optional.empty(), $.just(null, 2).first());
        assertEquals(Optional.empty(), $.empty().head());
    }

    @Test
    void filter1() {
        assertEquals("[a,  ]", $.just("a", " ", null).filter(Objects::nonNull).value().toString());
        assertEquals("[2, 4]", $.just(1, 2, 3, 4, 5).filter(i -> i % 2 == 0).value().toString());
        assertThrows(NullPointerException.class, () -> $.just(1, 2, 3, 4, 5).filter((Predicate<Integer>) null).value());
    }

    @Test
    void filter2() {
        assertEquals("[1, 3, 5]", $.just(1, 2, 3, 4, 5).filter((it, i) -> i % 2 == 0).value().toString());
        assertThrows(NullPointerException.class, () -> $.just(1, 2, 3, 4, 5).filter((ObjIntPredicate<Integer>) null).value().toString());
    }

    @Test
    void concat() {
        assertEquals("[a, , null, 1, 2, null, b]", $
                .just("a", "", null)
                .concat(null)
                .concat($.list("1", "2"))
                .concat($.list())
                .concat($.list(null, "b"))
                .value().toString());
        assertEquals("[1, 2, 3, 4]", $.just(1, 2).concat($.list(3, 4)).value().toString());
        assertEquals("[1, 2, 3, 4]", $.just(1, 2).concat(null).concat($.list(3, 4)).value().toString());
    }

    @Test
    void compact() {
        assertEquals("[a]", $.just("a", "", null).compact().value().toString());
        assertEquals("[]", $.just(null, "", false, 0).compact().value().toString());
        assertEquals("[6, 哈哈]", $.just(null, 6, "", "哈哈", false, 0).compact().value().toString());
        assertEquals("[]", $.just((Object) null).compact().value().toString());
    }

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
        List<Integer> list = $.list(1, 2, 3, 4, 5, 6);
        for (int i = 1; i < cases.length; i++) {
            assertEquals(cases[i], $(list).chunk(i).value().toString());
        }

        assertThrows(IllegalArgumentException.class, () -> $(list).chunk(0).value());
    }

    @Nested
    class ConstructListStage {
        @Test
        void nullList() {
            assertEquals(0, $((List<Object>) null).value().size());
        }

        @Test
        void just() {
            assertEquals(1, $.just(1).value().size());
            assertEquals(2, $.just(1, 1).value().size());
            assertEquals(3, $.just(1, 1, 1).value().size());
            assertEquals(4, $.just(1, 1, 1, 1).value().size());

            assertEquals(1, $.just(1).reduce(0, Integer::sum));
            assertEquals(2, $.just(1, 1).reduce(0, Integer::sum));
            assertEquals(3, $.just(1, 1, 1).reduce(0, Integer::sum));
            assertEquals(4, $.just(1, 1, 1, 1).reduce(0, Integer::sum));
        }

        @Test
        void range() {
            assertEquals("[1, 2, 3]", $.range(1, 4).value().toString());
//            assertEquals("[]", $.range(1, 1).value().toString());
//            assertEquals("[]", $.range(1, -1).value().toString());
        }
    }
}
