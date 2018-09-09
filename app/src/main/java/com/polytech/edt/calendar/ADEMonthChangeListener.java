package com.polytech.edt.calendar;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekViewEvent;
import com.polytech.edt.model.ADECalendar;
import com.polytech.edt.model.ADEEvent;

import java.util.ArrayList;
import java.util.List;

public class ADEMonthChangeListener implements MonthLoader.MonthChangeListener {

    private ADECalendar calendar;

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
            if (event.month() == (newMonth)) {
                events.add(event);
            }
        }

        return events;
    }
}
