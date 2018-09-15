package com.polytech.edt;

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
