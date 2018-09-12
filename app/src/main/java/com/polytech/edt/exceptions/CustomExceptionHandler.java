package com.polytech.edt.exceptions;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import com.polytech.edt.App;
import com.polytech.edt.activities.ErrorReporterActivity;

public class CustomExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Application application;

    /**
     * Constructor
     *
     * @param application Application
     */
    public CustomExceptionHandler(Application application) {
        this.application = application;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Intent intent = new Intent(App.currentActivity, ErrorReporterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle b = new Bundle();

        // Pass report to activity
        b.putSerializable("report", new ExceptionReport(t.getId(), e));
        intent.putExtras(b);

        // Start activity
        Activity current = App.currentActivity;
        App.currentActivity.startActivity(intent);
        current.finish();
    }
}
