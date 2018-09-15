package com.polytech.edt.calendar;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekViewEvent;
import com.polytech.edt.model.ADECalendar;
import com.polytech.edt.model.ADEEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ADEMonthChangeListener implements MonthLoader.MonthChangeListener {

    private static int currentWeek;

    private ADECalendar calendar;

    static {
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());

        currentWeek = now.get(Calendar.WEEK_OF_YEAR) + (now.get(Calendar.DAY_OF_WEEK) == 7 || now.get(Calendar.DAY_OF_WEEK) == 1 ? 1 : 0);
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
            if (event.month() == newMonth && event.week() == currentWeek) {
                events.add(event);
            }
        }

        return events;
    }
}
