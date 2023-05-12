package com.xyzwps.lib.dollar;

import java.util.Collection;

interface DollarCollections {

    /**
     * Check if the collection is empty or not.
     *
     * @param collection to be checked
     * @param <T>        type of elements
     * @return true if collection is null or has no elements
     */
    default <T> boolean isEmpty(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }


    /**
     * Check if the collection is not empty.
     *
     * @param collection to be checked
     * @param <T>        type of elements
     * @return true if collection {@link #isEmpty(Collection)} is false
     */
    default <T> boolean isNotEmpty(Collection<T> collection) {
        return !isEmpty(collection);
    }

    /**
     * Count the element of a <code>collection</code>.
     * Return 0 if <code>collection</code> is <code>null</code>.
     *
     * @param collection which to handle
     * @param <E>        collection element type
     * @return count of elements in collection
     */
    default <E> int length(Collection<E> collection) {
        return collection == null ? 0 : collection.size();
    }


}
