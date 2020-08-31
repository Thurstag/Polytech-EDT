/*
 * Copyright 2018-2020 Polytech-EDT
 * Licensed under Apache License 2.0 or any later version
 * Refer to the LICENSE.md file included.
 */
package com.polytech.edt.config;

public enum AppProperty {

    REPORT_MAIL("report.email");

    private String label;

    AppProperty(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
