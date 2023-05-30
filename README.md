xyzwps-dollar
======

A [lodash](https://lodash.com/)-like, lazy and powerful java collection utils under MIT license.

For example:

```java
import static com.xyzwps.lib.dollar.Dollar.*;

...

assertIterableEquals(
    $.list(28, 24, 20, 16),
    $.just(2, 3, 4, 5, 6)
        .map(i -> i * 2)
        .flatMap(i -> $.just(i, i + 2))
        .orderBy(Function.identity(), Direction.DESC)
        .filter(i -> i > 6)
        .map(i -> i * 2)
        .unique()
        .value()
);
```

TODO: 原则

如果返回，则总是返回 ArrayList、HashMap、HashSet 