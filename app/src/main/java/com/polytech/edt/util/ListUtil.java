package com.polytech.edt.util;

import com.google.common.base.Predicate;

import java.util.Collection;
import java.util.Iterator;

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
}
