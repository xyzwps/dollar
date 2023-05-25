package com.xyzwps.lib.dollar.function;

/**
 * Indexed version of {@link java.util.function.Predicate Predicate}.
 *
 * // TODO: 修改描述
 *
 * @param <E> the type of the element to the predicate
 */
@FunctionalInterface
public interface ObjIntPredicate<E> {

    /**
     * Evaluates this predicate on the given argument.
     *
     * @param element the tested element
     * @param index   the index of <code>element</code>
     * @return {@code true} if the arguments matches the predicate, otherwise {@code false}
     */
    boolean test(E element, int index);
}
