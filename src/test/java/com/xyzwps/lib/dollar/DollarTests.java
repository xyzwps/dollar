package com.xyzwps.lib.dollar;

import org.junit.jupiter.api.Test;

import static com.xyzwps.lib.dollar.Dollar.*;
import static org.junit.jupiter.api.Assertions.*;

class DollarTests {

    @Test
    void chunk() {
        assertEquals("[]", $.chunk($.list(), 6).toString());
        assertEquals("[]", $.chunk(null, 6).toString());

        assertThrows(IllegalArgumentException.class, () -> $.chunk($.list(), 0));
        assertThrows(IllegalArgumentException.class, () -> $.chunk(null, 0));

        assertEquals("[[1], [2], [3], [4], [5]]", $.chunk($.list(1, 2, 3, 4, 5), 1).toString());
        assertEquals("[[1, 2], [3, 4], [5]]", $.chunk($.list(1, 2, 3, 4, 5), 2).toString());
        assertEquals("[[1, 2, 3], [4, 5]]", $.chunk($.list(1, 2, 3, 4, 5), 3).toString());
        assertEquals("[[1, 2, 3, 4], [5]]", $.chunk($.list(1, 2, 3, 4, 5), 4).toString());
        assertEquals("[[1, 2, 3, 4, 5]]", $.chunk($.list(1, 2, 3, 4, 5), 5).toString());
        assertEquals("[[1, 2, 3, 4, 5]]", $.chunk($.list(1, 2, 3, 4, 5), 6).toString());

        assertThrows(IllegalArgumentException.class, () -> $.chunk($.list(1, 2, 3, 4, 5), 0));
        assertThrows(IllegalArgumentException.class, () -> $.chunk($.list(1, 2, 3, 4, 5), -1));
    }
}
