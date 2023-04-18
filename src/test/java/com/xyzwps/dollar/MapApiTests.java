package com.xyzwps.dollar;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.TreeMap;

import static com.xyzwps.dollar.Dollar.*;
import static org.junit.jupiter.api.Assertions.*;

class MapApiTests {

    @Test
    void mapValues() {
        Map<Integer, Integer> map = $($.newMap(0, "", 1, "1", 2, "11", 3, "111"))
                .mapValues(String::length)
                .value();
        assertEquals(4, map.size());
        assertEquals(0, map.get(0));
        assertEquals(1, map.get(1));
        assertEquals(2, map.get(2));
        assertEquals(3, map.get(3));
    }

    @Test
    void mapKeys() {
        TreeMap<Integer, Integer> treeMap = new TreeMap<>();
        treeMap.put(1, 1);
        treeMap.put(2, 2);
        treeMap.put(3, 3);
        treeMap.put(4, 4);
        treeMap.put(5, 5);
        treeMap.put(6, 6);
        Map<Integer, Integer> map = $(treeMap)
                .mapKeys(i -> i % 3)
                .value();
        assertEquals(1, map.get(1));
        assertEquals(2, map.get(2));
        assertEquals(3, map.get(0));
    }
}