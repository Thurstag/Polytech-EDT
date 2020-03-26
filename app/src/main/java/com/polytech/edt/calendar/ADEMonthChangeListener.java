/*
 * Copyright 2018-2020 Polytech-EDT
 * Licensed under Apache License 2.0 or any later version
 * Refer to the LICENSE.md file included.
 */
package com.polytech.edt.calendar;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekViewEvent;
import com.polytech.edt.model.calendar.ADECalendar;
import com.polytech.edt.model.calendar.ADEEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ADEMonthChangeListener implements MonthLoader.MonthChangeListener {

    private ADECalendar calendar;

    static {
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
    }

    /**
     * Constructor
     *
     * @param calendar Calendar
     */
    public ADEMonthChangeListener(ADECalendar calendar) {
        this.calendar = calendar;
    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        List<WeekViewEvent> events = new ArrayList<>();

        for (ADEEvent event : calendar.events()) {
            if (event.month() == newMonth) {
                events.add(event);
            }
        }

        return events;
    }
}
