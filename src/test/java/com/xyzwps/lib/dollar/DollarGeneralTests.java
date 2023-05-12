package com.xyzwps.lib.dollar;

import org.junit.jupiter.api.Test;

import static com.xyzwps.lib.dollar.Dollar.$;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DollarGeneralTests {

    @Test
    void defaultTo() {
        assertEquals(1, $.defaultTo(null, 1));
        assertEquals(2, $.defaultTo(2, 1));
        assertNull($.defaultTo(null, null));
    }

}
