package com.xyzwps.lib.gen;

import java.util.ArrayList;
import java.util.List;

public class GenerateTubeJust {

    private static void println(String s) {
        System.out.println(s);
    }

    public static void main(String[] args) {

        for (int i = 1; i <= 10; i++) {
            printFn(i);
            println("\n");
        }
    }

    static void printFn(int argsCount) {
        println("/**");
        println(" * Create a tube from elements.");
        println(" *");
        for (int i = 1; i <= argsCount; i++) {
            println(" * @param a" + i + " the " + Nth.get(i) + " element");
        }
        println(" * @param <T> element type");
        println(" * @return list tube");
        println(" */");
        println("default <T> ListTube<T> just(" + args(argsCount) + ") {");
        println("    //noinspection unchecked");
        println("    return new ListTubeFromIterator<>(new ArrayIterator<>((T[])new Object[]{" + elements(argsCount) + "}));");
        println("}");
    }

    private static String args(int count) {
        List<String> seg = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            seg.add("T a" + i);
        }
        return String.join(", ", seg);
    }

    private static String elements(int count) {
        List<String> seg = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            seg.add("a" + i);
        }
        return String.join(", ", seg);
    }

}
