/*
 * Copyright 2018-2020 Polytech-EDT
 * Licensed under Apache License 2.0 or any later version
 * Refer to the LICENSE.md file included.
 */
package com.polytech.edt.model.calendar;

import com.polytech.edt.App;
import com.polytech.edt.R;
import com.polytech.edt.model.BasicEnum;

public enum CalendarUnit implements BasicEnum {

    Week(App.context.getString(R.string.week), "nbWeeks"),
    Month(App.context.getString(R.string.month), "nbWeeks");

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
