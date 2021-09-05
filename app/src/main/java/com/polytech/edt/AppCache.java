/*
 * Copyright 2018-2021 Polytech-EDT
 * Licensed under Apache License 2.0 or any later version
 * Refer to the LICENSE.md file included.
 */
package com.polytech.edt;

import java.util.HashMap;
import java.util.Map;

public class AppCache {
    //region Fields

    private static Map<String, Object> cache;

    //endregion

    //region Constructor

    static {
        cache = new HashMap<>();
    }

    //endregion

    //region Methods

    /**
     * Save an object
     *
     * @param key     Key
     * @param _object Object
     */
    public static void save(String key, Object _object) {
        cache.put(key, _object);
    }

    /**
     * Get an object with its key
     *
     * @param key Key
     * @param <T> Object type
     * @return Object or null
     */
    public static <T> T get(String key) {
        return (T) cache.get(key);
    }

    //endregion
}
