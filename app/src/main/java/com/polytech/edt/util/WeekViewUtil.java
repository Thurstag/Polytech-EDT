package com.polytech.edt.util;

import com.alamkanak.weekview.WeekViewEvent;
import com.polytech.edt.model.ADEWeekView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class WeekViewUtil {

    /**
     * Checks if two times are on the same day.
     *
     * @param dayOne The first day.
     * @param dayTwo The second day.
     * @return Whether the times are on the same day.
     */
    public static boolean isSameDay(Calendar dayOne, Calendar dayTwo) {
        return dayOne.get(Calendar.YEAR) == dayTwo.get(Calendar.YEAR) && dayOne.get(Calendar.DAY_OF_YEAR) == dayTwo.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * Returns a calendar instance at the start of this day
     *
     * @return the calendar instance
     */
    public static Calendar today() {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        return today;
    }

    /**
     * Splits Weekview events
     *
     * @param event Event
     * @return Events
     */
    public static List<WeekViewEvent> splitWeekViewEvents(WeekViewEvent event) {
        //This function splits the WeekViewEvent in WeekViewEvents by day
        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
        if (!WeekViewUtil.isSameDay(event.getStartTime(), event.getEndTime())) {
            Calendar endTime = (Calendar) event.getStartTime().clone();
            endTime.set(Calendar.HOUR_OF_DAY, 23);
            endTime.set(Calendar.MINUTE, 59);
            WeekViewEvent event1 = new WeekViewEvent(event.getId(), event.getName(), event.getLocation(), event.getStartTime(), endTime);
            event1.setColor(event.getColor());
            events.add(event1);
            // Add other days.
            Calendar otherDay = (Calendar) event.getStartTime().clone();
            otherDay.add(Calendar.DATE, 1);
            while (!WeekViewUtil.isSameDay(otherDay, event.getEndTime())) {
                Calendar overDay = (Calendar) otherDay.clone();
                overDay.set(Calendar.HOUR_OF_DAY, 0);
                overDay.set(Calendar.MINUTE, 0);
                Calendar endOfOverDay = (Calendar) overDay.clone();
                endOfOverDay.set(Calendar.HOUR_OF_DAY, 23);
                endOfOverDay.set(Calendar.MINUTE, 59);
                WeekViewEvent eventMore = new WeekViewEvent(event.getId(), event.getName(), null, overDay, endOfOverDay);
                eventMore.setColor(event.getColor());
                events.add(eventMore);
                // Add next day.
                otherDay.add(Calendar.DATE, 1);
            }
            // Add last day.
            Calendar startTime = (Calendar) event.getEndTime().clone();
            startTime.set(Calendar.HOUR_OF_DAY, 0);
            startTime.set(Calendar.MINUTE, 0);
            WeekViewEvent event2 = new WeekViewEvent(event.getId(), event.getName(), event.getLocation(), startTime, event.getEndTime());
            event2.setColor(event.getColor());
            events.add(event2);
        }
        else {
            events.add(event);
        }
        return events;
    }

    public static boolean isAllDay(WeekViewEvent event) {
        return event.getStartTime().get(Calendar.HOUR_OF_DAY) <= ADEWeekView.MIN_HOUR && event.getEndTime().get(Calendar.HOUR_OF_DAY) >= ADEWeekView.MAX_HOUR;
    }
}
