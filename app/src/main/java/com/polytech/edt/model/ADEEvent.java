package com.polytech.edt.model;

import android.support.annotation.NonNull;

import net.fortuna.ical4j.model.component.VEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ADEEvent implements Comparable<ADEEvent> {

    //region Fields

    private final static String LABEL = "SUMMARY";
    private final static String START_DATE = "DTSTART";
    private final static String END_DATE = "DTEND";
    private final static String LOCATION = "LOCATION";
    private final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'", Locale.FRANCE);

    private final Date startDate;
    private final Date endDate;
    private String label;
    private String location;

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
        this.label = event.getProperty(LABEL).getValue();
        this.startDate = dateFormat.parse(event.getProperty(START_DATE).getValue());
        this.endDate = dateFormat.parse(event.getProperty(END_DATE).getValue());
        this.location = event.getProperty(LOCATION).getValue();
    }

    //endregion

    //region GET

    public Date startDate() {
        return startDate;
    }

    public Date endDate() {
        return endDate;
    }

    public String label() {
        return label;
    }

    public String location() {
        return location;
    }

    //endregion

    //region Methods

    @Override
    public int compareTo(@NonNull ADEEvent event) {
        if (event.startDate == this.startDate) {
            return 0;
        }
        if (event.startDate.after(this.startDate)) {
            return -1;
        }
        if (event.startDate.before(this.startDate)) {
            return 1;
        }
        return 0;
    }

    //endregion
}
