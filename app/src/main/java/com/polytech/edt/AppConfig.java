package com.polytech.edt;

import com.polytech.edt.util.LOGGER;

import java.io.InputStream;
import java.util.Properties;

public class AppConfig {

    private static Properties properties = new Properties();

    public static void loadConfig() {
        try {
            InputStream rawResource = App.context.getResources().openRawResource(R.raw.application);
            properties.load(rawResource);
        } catch (Exception e) {
            LOGGER.fatal(e);
        }
    }

    /**
     * Method to get a property
     *
     * @param property Property's name
     * @return Property's value
     */
    public static String get(AppProperty property) {
        return properties.getProperty(property.label());
    }

    /**
     * Method to get a property
     *
     * @param property Property's name
     * @param _default Default value
     * @return Property's value
     */
    public static String get(AppProperty property, String _default) {
        return properties.getProperty(property.label(), _default);
    }
}
