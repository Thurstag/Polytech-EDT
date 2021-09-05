/*
 * Copyright 2018-2021 Polytech-EDT
 * Licensed under Apache License 2.0 or any later version
 * Refer to the LICENSE.md file included.
 */
package com.polytech.edt.exceptions;

import android.os.Build;

import com.polytech.edt.App;
import com.polytech.edt.BuildConfig;
import com.polytech.edt.model.IniReport;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

public class ExceptionReport implements Serializable {

    private Throwable throwable;
    private long threadId;

    /**
     * Constructor
     *
     * @param threadId  Thread ID
     * @param throwable Throwable
     */
    public ExceptionReport(long threadId, Throwable throwable) {
        this.threadId = threadId;
        this.throwable = throwable;
    }

    public Throwable exception() {
        return throwable;
    }

    public String stackTrace() {
        StringBuilder builder = new StringBuilder();

        for (StackTraceElement element : throwable.getStackTrace()) {
            builder.append(element);
            builder.append("\n");
        }
        return builder.toString();
    }

    public IniReport report() {
        IniReport report = new IniReport();

        String appKey = "App";
        report.addCategory(appKey);
        report.getCategory(appKey).put("version", BuildConfig.VERSION_NAME);
        report.getCategory(appKey).put("git revision", BuildConfig.GIT_COMMIT_SHA);

        String contextKey = "Context";
        report.addCategory(contextKey);
        report.getCategory(contextKey).put("crash date", (new Date()).toString());
        report.getCategory(contextKey).put("thread id", threadId + "");
        if (App.activityHistory.size() > 1) {
            report.getCategory(contextKey).put("activity", App.activityHistory.get(App.activityHistory.size() - 2));
        }

        String deviceKey = "Device";
        report.addCategory(deviceKey);
        report.getCategory(deviceKey).put("brand", Build.BRAND);
        report.getCategory(deviceKey).put("device", Build.DEVICE);
        report.getCategory(deviceKey).put("model", Build.MODEL);
        report.getCategory(deviceKey).put("manufacturer", Build.MANUFACTURER);
        report.getCategory(deviceKey).put("product", Build.PRODUCT);
        report.getCategory(deviceKey).put("sdk", Build.VERSION.SDK_INT + "");
        report.getCategory(deviceKey).put("language", Locale.getDefault().getDisplayName());

        String errorKey = "Error";
        report.addCategory(errorKey);
        report.getCategory(errorKey).put("exception", throwable.getClass().getName());
        report.getCategory(errorKey).put("message", throwable.getMessage());
        if (throwable.getCause() != null) {
            report.getCategory(errorKey).put("exception cause", throwable.getCause().getClass().getName());
        }
        report.getCategory(errorKey).put("stack trace", stackTrace());

        return report;
    }
}
