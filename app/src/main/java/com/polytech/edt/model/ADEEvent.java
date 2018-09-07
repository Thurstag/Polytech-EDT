package com.polytech.edt.model;

import android.support.annotation.NonNull;

import com.alamkanak.weekview.WeekViewEvent;

import net.fortuna.ical4j.model.component.VEvent;

import org.apache.commons.lang.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Event
 */
public class ADEEvent extends WeekViewEvent implements Comparable<ADEEvent> {

    //region Fields

    private final static String LABEL = "SUMMARY";
    private final static String START_DATE = "DTSTART";
    private final static String END_DATE = "DTEND";
    private final static String LOCATION = "LOCATION";
    private final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'", Locale.FRANCE);

    private final Date startDate;
    private final Date endDate;

    //endregion

    //region Constructors

    static {
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    /**
     * Constructor
     *
     * @param event Event
     * @throws ParseException Date parsing error
     */
    ADEEvent(VEvent event) throws ParseException {
        this.setName(event.getProperty(LABEL).getValue());
        this.startDate = dateFormat.parse(event.getProperty(START_DATE).getValue());
        this.endDate = dateFormat.parse(event.getProperty(END_DATE).getValue());
        this.setLocation(event.getProperty(LOCATION).getValue());
    }

    //endregion

    //region GET

    public int month() {
        return getEndTime().get(Calendar.MONTH);
    }

    @Override
    public Calendar getStartTime() {
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        return c;
    }

    @Override
    public Calendar getEndTime() {
        Calendar c = Calendar.getInstance();
        c.setTime(endDate);
        return c;
    }

    private String span() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.FRANCE);

        return format.format(getStartTime().getTime()) + " - " + format.format(getEndTime().getTime());
    }

    //endregion

    //region Methods

    @Override
    public int compareTo(@NonNull ADEEvent event) {
        return getStartTime().compareTo(event.getStartTime());
    }

    //endregion
}
