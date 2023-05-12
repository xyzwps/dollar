package com.xyzwps.lib.gen;

import java.util.ArrayList;
import java.util.List;

import static com.xyzwps.lib.dollar.Dollar.*;

public class GenerateArrayList {

    private static void println(String s) {
        System.out.println(s);
    }

    public static void main(String[] args) {

        $.range(0, 11).forEach(i -> {
            printListI(i);
            println("\n\n");
        });
    }

    private static void printListI(int argsCount) {

        println("/**");
        println(" * Create a list.");
        println(" *");
        for (int i = 1; i <= argsCount; i++) {
            println(" * @param t" + i + " the " + Nth.get(i) + " element");
        }
        println(" * @param <T>      element type");
        println(" * @return new ArrayList");
        println(" */");
        println("default <T> ArrayList <T> arrayList(" + args(argsCount) + ") {");
        if (argsCount == 0) {
            println("    return new ArrayList<>();");
        } else {
            println("    ArrayList<T> a = new ArrayList<>(" + (argsCount + 1) + ");");
            for (int i = 1; i <= argsCount; i++) {
                println("    a.add(t" + i + ");");
            }
            println("    return a;");
        }


        println("}");
    }

    private static String args(int count) {
        List<String> seg = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            seg.add("T t" + i);
        }
        return String.join(", ", seg);
    }
}
