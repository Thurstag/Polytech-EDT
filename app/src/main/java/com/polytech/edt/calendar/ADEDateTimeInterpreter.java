/*
 * Copyright 2018-2020 Polytech-EDT
 * Licensed under Apache License 2.0 or any later version
 * Refer to the LICENSE.md file included.
 */
package com.polytech.edt.calendar;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.polytech.edt.model.calendar.ADEWeekView;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ADEDateTimeInterpreter implements DateTimeInterpreter {

    private ADEWeekView weekView;

    /**
     * Constructor
     *
     * @param weekView Calendar
     */
    public ADEDateTimeInterpreter(ADEWeekView weekView) {
        this.weekView = weekView;
    }

    @Override
    public String interpretDate(java.util.Calendar date) {
        String day = new SimpleDateFormat("EEE", Locale.getDefault()).format(date.getTime());

        // If scope is a week
        if (weekView.getNumberOfVisibleDays() == 7) {
            return day.toUpperCase();
        }
        return day.toUpperCase() + new SimpleDateFormat("d/M", Locale.getDefault()).format(date.getTime());
    }

    @Override
    public String interpretTime(int hour) {
        return hour + ":00 -";
    }
}
