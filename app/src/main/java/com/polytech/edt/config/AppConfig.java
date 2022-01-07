/*
 * Copyright 2018-2022 Polytech-EDT
 * Licensed under Apache License 2.0 or any later version
 * Refer to the LICENSE.md file included.
 */
package com.polytech.edt.config;

import com.polytech.edt.App;
import com.polytech.edt.R;
import com.polytech.edt.util.LOGGER;

import java.io.InputStream;
import java.util.Properties;

public class AppConfig {

    private static Properties properties = new Properties();

    public static void load() {
        try {
            InputStream rawResource = App.context.getResources().openRawResource(R.raw.application);
            properties.load(rawResource);
        } catch (Exception e) {
            LOGGER.fatal(e);
        }
    }

    /**
     * Get a property
     *
     * @param property Property's name
     * @return Property's value
     */
    public static String get(AppProperty property) {
        return properties.getProperty(property.label(), null);
    }

    /**
     * Get a property
     *
     * @param property Property's name
     * @param _default Default value
     * @return Property's value
     */
    public static String get(AppProperty property, String _default) {
        String o = properties.getProperty(property.label(), _default);

        if (o == null) {
            LOGGER.warning(String.format("Fail to retrieve property: %s", property.label()));
        }
        return o;
    }
}
