package com.polytech.edt;

public enum AppProperty {

    REPORT_MAIL("report.email"),
    RESOURCES_LIST("calendar.resources");

    private String label;

    AppProperty(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
