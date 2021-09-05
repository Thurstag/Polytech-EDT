/*
 * Copyright 2018-2021 Polytech-EDT
 * Licensed under Apache License 2.0 or any later version
 * Refer to the LICENSE.md file included.
 */
package com.polytech.edt.exceptions;

import android.app.Activity;
import android.content.Intent;

import com.polytech.edt.App;
import com.polytech.edt.activities.ErrorReporterActivity;
import com.polytech.edt.util.LOGGER;

public class CustomExceptionHandler implements Thread.UncaughtExceptionHandler {

    /**
     * Constructor
     *
     */
    public CustomExceptionHandler() {

    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        LOGGER.error(e);

        Intent intent = new Intent(App.currentActivity, ErrorReporterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Pass report to activity
        intent.putExtra("report", new ExceptionReport(t.getId(), e));

        // Start activity
        Activity current = App.currentActivity;
        App.currentActivity.startActivity(intent);
        current.finish();
    }
}
