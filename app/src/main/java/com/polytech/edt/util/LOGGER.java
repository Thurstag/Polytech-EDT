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

    public static void error(Throwable exception) {
        Log.e("ERROR", "", exception);
    }

    public static void fatal(Throwable exception) {
        Log.e("FATAL", "", exception);

        if (Thread.getDefaultUncaughtExceptionHandler() != null) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), exception);
        }
    }

    public static void fatal(String message) {
        Log.e("FATAL", message);

        if (Thread.getDefaultUncaughtExceptionHandler() != null) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), new Exception(message));
        }
    }

    //endregion
}
