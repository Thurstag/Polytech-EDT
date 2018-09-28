package com.polytech.edt.model.calendar;

import com.polytech.edt.model.BasicEnum;

public enum CalendarUnit implements BasicEnum {

    Week("Week", "nbWeeks"),
    Month("Month", "nbWeeks");

    private String label;
    private String urlParameter;

    CalendarUnit(String label, String urlParameter) {
        this.label = label;
        this.urlParameter = urlParameter;
    }

    public String label() {
        return label;
    }

    /**
     * Method to convert a CalendarUnit into a url parameter (For CalendarURL)
     *
     * @return Parameter
     */
    public String URLParameter() {
        return urlParameter;
    }

    /**
     * Method to transform scope depends on unit
     *
     * @param scope Scope
     * @return Transformed scope
     */
    public int transformScope(int scope) throws Exception {
        switch (this) {
            case Week:
                return scope;

            case Month:
                return scope * 5;

            default:
                throw new Exception("Unknown scope: " + this);
        }
    }
}
