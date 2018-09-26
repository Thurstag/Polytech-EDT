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
     * Method to save an object
     *
     * @param key     Key
     * @param _object Object
     */
    public static void save(String key, Object _object) {
        cache.put(key, _object);
    }

    /**
     * Method to get an object from cast
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
