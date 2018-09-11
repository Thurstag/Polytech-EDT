package com.polytech.edt.util;

import android.util.Log;

public class LOGGER {

    //region Methods

    public static void info(String message) {
        Log.i("INFO", message);
    }

    public static void warning(String message) {
        Log.w("WARNING", message);
    }

    public static void debug(String message) {
        Log.d("DEBUG", message);
    }

    public static void error(String message) {
        Log.e("ERROR", message);
    }

    public static void error(Exception exception) {
        Log.e("ERROR", "", exception);
    }

    public static void fatal(Exception exception) {
        Log.wtf("FATAL", "", exception);
        System.exit(0);
    }

    public static void fatal(String message) {
        Log.wtf("FATAL", message);
        System.exit(0);
    }

    //endregion
}
