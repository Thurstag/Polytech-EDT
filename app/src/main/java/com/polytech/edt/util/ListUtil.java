/*
 * Copyright 2018-2021 Polytech-EDT
 * Licensed under Apache License 2.0 or any later version
 * Refer to the LICENSE.md file included.
 */
package com.polytech.edt.util;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ListUtil {

    public static <T> void remove(Collection<T> list, Predicate<T> predicate) {
        Iterator<T> iterator = list.iterator();

        while (iterator.hasNext()) {
            T object = iterator.next();

            // Test predicate
            if (predicate.apply(object)) {
                iterator.remove();
            }
        }
    }

    public static <T> boolean contains(Collection<T> list, Predicate<T> predicate) {
        for (T object : list) {
            // Test predicate
            if (predicate.apply(object)) {
                return true;
            }
        }
        return false;
    }

    public static <T, V> List<V> select(Collection<T> list, Function<T, V> selector) {
        List<V> result = new ArrayList<>();

        for (T item : list) {
            result.add(selector.apply(item));
        }

        return result;
    }

    public static <T> int indexOf(Collection<T> list, T value) {
        int i = 0;

        for (T item : list) {
            if (item.equals(value)) {
                return i;
            }
            i++;
        }
        return -1;
    }
}
