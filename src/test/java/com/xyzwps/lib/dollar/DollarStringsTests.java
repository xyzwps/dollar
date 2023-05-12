package com.xyzwps.lib.dollar;

import org.junit.jupiter.api.Test;

import static com.xyzwps.lib.dollar.Dollar.$;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DollarStringsTests {


    @Test
    void pad() {
        assertThrows(IllegalArgumentException.class, () -> $.pad("", -1, " "));

        assertEquals("      ", $.pad(null, 6, null));
        assertEquals("      ", $.pad(null, 6, ""));
        assertEquals("      ", $.pad(null, 6, " "));
        assertEquals("aaaaaa", $.pad(null, 6, "a"));
        assertEquals("ababab", $.pad(null, 6, "ab"));
        assertEquals("abcdab", $.pad(null, 6, "abcd"));

        assertEquals("      ", $.pad("", 6, null));
        assertEquals("      ", $.pad("", 6, ""));
        assertEquals("      ", $.pad("", 6, " "));
        assertEquals("aaaaaa", $.pad("", 6, "a"));
        assertEquals("ababab", $.pad("", 6, "ab"));
        assertEquals("abcdab", $.pad("", 6, "abcd"));

        assertEquals(" +++  ", $.pad("+++", 6, null));
        assertEquals(" +++  ", $.pad("+++", 6, ""));
        assertEquals(" +++  ", $.pad("+++", 6, " "));
        assertEquals("a+++aa", $.pad("+++", 6, "a"));
        assertEquals("a+++ba", $.pad("+++", 6, "ab"));
        assertEquals("a+++bc", $.pad("+++", 6, "abcd"));

        assertEquals("+++---***", $.pad("+++---***", 6, "abcd"));
    }

    @Test
    void padEnd() {
        assertThrows(IllegalArgumentException.class, () -> $.padEnd("", -1, " "));

        assertEquals("      ", $.padEnd(null, 6, null));
        assertEquals("      ", $.padEnd(null, 6, ""));
        assertEquals("      ", $.padEnd(null, 6, " "));
        assertEquals("aaaaaa", $.padEnd(null, 6, "a"));
        assertEquals("ababab", $.padEnd(null, 6, "ab"));
        assertEquals("abcdab", $.padEnd(null, 6, "abcd"));

        assertEquals("      ", $.padEnd("", 6, null));
        assertEquals("      ", $.padEnd("", 6, ""));
        assertEquals("      ", $.padEnd("", 6, " "));
        assertEquals("aaaaaa", $.padEnd("", 6, "a"));
        assertEquals("ababab", $.padEnd("", 6, "ab"));
        assertEquals("abcdab", $.padEnd("", 6, "abcd"));

        assertEquals("+++   ", $.padEnd("+++", 6, null));
        assertEquals("+++   ", $.padEnd("+++", 6, ""));
        assertEquals("+++   ", $.padEnd("+++", 6, " "));
        assertEquals("+++aaa", $.padEnd("+++", 6, "a"));
        assertEquals("+++aba", $.padEnd("+++", 6, "ab"));
        assertEquals("+++abc", $.padEnd("+++", 6, "abcd"));

        assertEquals("+++---***", $.padEnd("+++---***", 6, "abcd"));
    }

    @Test
    void padStart() {
        assertThrows(IllegalArgumentException.class, () -> $.padStart("", -1, " "));

        assertEquals("      ", $.padStart(null, 6, null));
        assertEquals("      ", $.padStart(null, 6, ""));
        assertEquals("      ", $.padStart(null, 6, " "));
        assertEquals("aaaaaa", $.padStart(null, 6, "a"));
        assertEquals("ababab", $.padStart(null, 6, "ab"));
        assertEquals("abcdab", $.padStart(null, 6, "abcd"));

        assertEquals("      ", $.padStart("", 6, null));
        assertEquals("      ", $.padStart("", 6, ""));
        assertEquals("      ", $.padStart("", 6, " "));
        assertEquals("aaaaaa", $.padStart("", 6, "a"));
        assertEquals("ababab", $.padStart("", 6, "ab"));
        assertEquals("abcdab", $.padStart("", 6, "abcd"));

        assertEquals("   +++", $.padStart("+++", 6, null));
        assertEquals("   +++", $.padStart("+++", 6, ""));
        assertEquals("   +++", $.padStart("+++", 6, " "));
        assertEquals("aaa+++", $.padStart("+++", 6, "a"));
        assertEquals("aba+++", $.padStart("+++", 6, "ab"));
        assertEquals("abc+++", $.padStart("+++", 6, "abcd"));

        assertEquals("+++---***", $.padStart("+++---***", 6, "abcd"));
    }
}
