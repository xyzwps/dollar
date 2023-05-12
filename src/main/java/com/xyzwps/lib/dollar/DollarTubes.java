package com.xyzwps.lib.dollar;

import com.xyzwps.lib.dollar.iterator.ArrayIterator;
import com.xyzwps.lib.dollar.iterator.EmptyIterator;
import com.xyzwps.lib.dollar.tube.ListTubeFromIterator;

interface DollarTubes {

    /**
     * Create an empty tube.
     *
     * @param <T> element type
     * @return list tube
     */
    default <T> ListTube<T> empty() {
        return new ListTubeFromIterator<>(EmptyIterator.create());
    }

    /**
     * Create a tube from elements.
     *
     * @param a1  the first element
     * @param <T> element type
     * @return list tube
     */
    default <T> ListTube<T> just(T a1) {
        //noinspection unchecked
        return new ListTubeFromIterator<>(new ArrayIterator<>((T[]) new Object[]{a1}));
    }


    /**
     * Create a tube from elements.
     *
     * @param a1  the first element
     * @param a2  the second element
     * @param <T> element type
     * @return list tube
     */
    default <T> ListTube<T> just(T a1, T a2) {
        //noinspection unchecked
        return new ListTubeFromIterator<>(new ArrayIterator<>((T[]) new Object[]{a1, a2}));
    }


    /**
     * Create a tube from elements.
     *
     * @param a1  the first element
     * @param a2  the second element
     * @param a3  the third element
     * @param <T> element type
     * @return list tube
     */
    default <T> ListTube<T> just(T a1, T a2, T a3) {
        //noinspection unchecked
        return new ListTubeFromIterator<>(new ArrayIterator<>((T[]) new Object[]{a1, a2, a3}));
    }


    /**
     * Create a tube from elements.
     *
     * @param a1  the first element
     * @param a2  the second element
     * @param a3  the third element
     * @param a4  the fourth element
     * @param <T> element type
     * @return list tube
     */
    default <T> ListTube<T> just(T a1, T a2, T a3, T a4) {
        //noinspection unchecked
        return new ListTubeFromIterator<>(new ArrayIterator<>((T[]) new Object[]{a1, a2, a3, a4}));
    }


    /**
     * Create a tube from elements.
     *
     * @param a1  the first element
     * @param a2  the second element
     * @param a3  the third element
     * @param a4  the fourth element
     * @param a5  the fifth element
     * @param <T> element type
     * @return list tube
     */
    default <T> ListTube<T> just(T a1, T a2, T a3, T a4, T a5) {
        //noinspection unchecked
        return new ListTubeFromIterator<>(new ArrayIterator<>((T[]) new Object[]{a1, a2, a3, a4, a5}));
    }


    /**
     * Create a tube from elements.
     *
     * @param a1  the first element
     * @param a2  the second element
     * @param a3  the third element
     * @param a4  the fourth element
     * @param a5  the fifth element
     * @param a6  the sixth element
     * @param <T> element type
     * @return list tube
     */
    default <T> ListTube<T> just(T a1, T a2, T a3, T a4, T a5, T a6) {
        //noinspection unchecked
        return new ListTubeFromIterator<>(new ArrayIterator<>((T[]) new Object[]{a1, a2, a3, a4, a5, a6}));
    }


    /**
     * Create a tube from elements.
     *
     * @param a1  the first element
     * @param a2  the second element
     * @param a3  the third element
     * @param a4  the fourth element
     * @param a5  the fifth element
     * @param a6  the sixth element
     * @param a7  the seventh element
     * @param <T> element type
     * @return list tube
     */
    default <T> ListTube<T> just(T a1, T a2, T a3, T a4, T a5, T a6, T a7) {
        //noinspection unchecked
        return new ListTubeFromIterator<>(new ArrayIterator<>((T[]) new Object[]{a1, a2, a3, a4, a5, a6, a7}));
    }


    /**
     * Create a tube from elements.
     *
     * @param a1  the first element
     * @param a2  the second element
     * @param a3  the third element
     * @param a4  the fourth element
     * @param a5  the fifth element
     * @param a6  the sixth element
     * @param a7  the seventh element
     * @param a8  the eighth element
     * @param <T> element type
     * @return list tube
     */
    default <T> ListTube<T> just(T a1, T a2, T a3, T a4, T a5, T a6, T a7, T a8) {
        //noinspection unchecked
        return new ListTubeFromIterator<>(new ArrayIterator<>((T[]) new Object[]{a1, a2, a3, a4, a5, a6, a7, a8}));
    }


    /**
     * Create a tube from elements.
     *
     * @param a1  the first element
     * @param a2  the second element
     * @param a3  the third element
     * @param a4  the fourth element
     * @param a5  the fifth element
     * @param a6  the sixth element
     * @param a7  the seventh element
     * @param a8  the eighth element
     * @param a9  the ninth element
     * @param <T> element type
     * @return list tube
     */
    default <T> ListTube<T> just(T a1, T a2, T a3, T a4, T a5, T a6, T a7, T a8, T a9) {
        //noinspection unchecked
        return new ListTubeFromIterator<>(new ArrayIterator<>((T[]) new Object[]{a1, a2, a3, a4, a5, a6, a7, a8, a9}));
    }


    /**
     * Create a tube from elements.
     *
     * @param a1  the first element
     * @param a2  the second element
     * @param a3  the third element
     * @param a4  the fourth element
     * @param a5  the fifth element
     * @param a6  the sixth element
     * @param a7  the seventh element
     * @param a8  the eighth element
     * @param a9  the ninth element
     * @param a10 the tenth element
     * @param <T> element type
     * @return list tube
     */
    default <T> ListTube<T> just(T a1, T a2, T a3, T a4, T a5, T a6, T a7, T a8, T a9, T a10) {
        //noinspection unchecked
        return new ListTubeFromIterator<>(new ArrayIterator<>((T[]) new Object[]{a1, a2, a3, a4, a5, a6, a7, a8, a9, a10}));
    }


    /**
     * Handle a range.
     *
     * @param start range start - included
     * @param end   range end - excluded
     * @return list tube
     */
    default ListTube<Integer> range(int start, int end) {
        return new ListTubeFromIterator<>(new Range(start, end).toIterator());
    }
}
