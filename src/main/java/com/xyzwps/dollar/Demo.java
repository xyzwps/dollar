package com.xyzwps.dollar;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static com.xyzwps.dollar.Dollar.*;

public class Demo {

    public static void main(String[] args) {
        List<Integer> s = Arrays.asList(2, 3, 4, 5, 6);

        System.out.println($(s)
                .map(i -> {
                    int r = i * 2;
                    System.out.printf("1) map %d to %d \n", i, r);
                    return r;
                })
                .flatMap(i -> {
                    int t1 = i, t2 = i + 2;
                    System.out.printf("  2) flat map %d to %d, %d \n", i, t1, t2);
                    return $(Arrays.asList(t1, t2));
                })
                .orderBy(Function.identity(), Direction.DESC)
                .filter(i -> {
                    boolean r = i > 6;
                    System.out.printf("    3) filter %d and %s it \n", i, r ? "keep" : "DROP");
                    return r;
                }).map(i -> {
                    int r = i * 2;
                    System.out.printf("      4) %d ====> %d \n", i, r);
                    return r;
                })
                .unique()
                .value());

        System.out.println("\n\n\n#### key by ####\n");
        System.out.println($(s)
                .map(i -> {
                    int r = i * 2;
                    System.out.printf("1) map %d to %d \n", i, r);
                    return r;
                })
                .keyBy(it -> it % 3)
                .value());


        System.out.println("\n\n\n#### group by ####\n");
        System.out.println($(s)
                .map(i -> {
                    int r = i * 2;
                    System.out.printf("1) map %d to %d \n", i, r);
                    return r;
                })
                .groupBy(it -> it % 3)
                .mapValues(List::size)
                .value());

        System.out.println("\n\n\n#### chunk ####\n");
        System.out.println($(s)
                .chunk(3)
                .value());

        System.out.println("\n\n\n#### first ####\n");
        System.out.println($(s).first());

        System.out.println("\n\n\n#### reverse ####\n");
        System.out.println($(s).reverse().value());
    }
}

