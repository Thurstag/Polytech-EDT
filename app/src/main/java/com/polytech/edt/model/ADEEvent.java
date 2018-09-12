package com.polytech.edt.model;

import android.graphics.Color;
import android.support.annotation.NonNull;

import com.alamkanak.weekview.WeekViewEvent;
import com.polytech.edt.App;
import com.polytech.edt.R;

import net.fortuna.ical4j.model.component.VEvent;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

/**
 * Event
 */
public class ADEEvent extends WeekViewEvent implements Comparable<ADEEvent>, Serializable {

    //region Fields

    private final static String LABEL = "SUMMARY";
    private final static String START_DATE = "DTSTART";
    private final static String END_DATE = "DTEND";
    private final static String LOCATION = "LOCATION";
    private static final String DESCRIPTION = "DESCRIPTION";
    private final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'", Locale.FRANCE);

    private final Date startDate;
    private final Date endDate;
    private String description;

    //endregion

    //region Constructors

    static {
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    /**
     * Constructor
     *
     * @param event Event
     * @throws ParseException Date parsing error
     */
    ADEEvent(VEvent event) throws ParseException {
        String undefined = App.context.getString(R.string.undefined);

        setName(event.getProperty(LABEL).getValue());
        startDate = dateFormat.parse(event.getProperty(START_DATE).getValue());
        endDate = dateFormat.parse(event.getProperty(END_DATE).getValue());
        setLocation(event.getProperty(LOCATION).getValue());
        description = event.getProperty(DESCRIPTION).getValue();
        setColor(generateColor(getLocation()));

        if (description.isEmpty()) {
            description = undefined;
        }
        else { // Remove last line
            description = new StringBuffer(description).reverse().toString();
            description = description.substring(description.indexOf('\n', 1));
            description = new StringBuilder(description).reverse().toString();
        }
        if (getLocation().isEmpty()) {
            setLocation(undefined);
        }
        if (getName().isEmpty()) {
            setName(undefined);
        }
    }

    /**
     * Method to generate a color depending on location
     *
     * @param location Location
     * @return Color
     */
    private int generateColor(String location) {
        // Amphi
        if (Pattern.compile("(\\d){3} - Amphi").matcher(location).matches()) {
            return Color.CYAN;
        }
        else if (Pattern.compile("620 - [a-z](\\d){3}").matcher(location.toLowerCase()).matches()) {   // 620 TD
            return Color.LTGRAY;
        }
        else if (Pattern.compile("640 - [a-z](\\d){3}").matcher(location.toLowerCase()).matches()) {   // 640 TD
            return Color.GRAY;
        }
        else if (location.startsWith("Centre Langues")) {   // Language center
            return Color.RED;
        }
        else {  // Other
            return Color.GRAY;
        }
    }

    //endregion

    //region GET

    @Override
    public Calendar getStartTime() {
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);

        if (c.get(Calendar.HOUR_OF_DAY) <= ADEWeekView.MIN_HOUR) {
            c.set(Calendar.HOUR_OF_DAY, ADEWeekView.MIN_HOUR);
        }

        return c;
    }

    @Override
    public Calendar getEndTime() {
        Calendar c = Calendar.getInstance();
        c.setTime(endDate);

        if (c.get(Calendar.HOUR_OF_DAY) >= ADEWeekView.MAX_HOUR) {
            c.set(Calendar.HOUR_OF_DAY, ADEWeekView.MAX_HOUR - 1);
        }

        return c;
    }

    public String description() {
        return description;
    }

    //endregion

    //region Methods

    @Override
    public int compareTo(@NonNull ADEEvent event) {
        return getStartTime().compareTo(event.getStartTime());
    }

    public String span() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.FRANCE);

        return format.format(getStartTime().getTime()) + " - " + format.format(getEndTime().getTime());
    }

    public int month() {
        return getStartTime().get(Calendar.MONTH);
    }

    public int week() {
        return getStartTime().get(Calendar.WEEK_OF_YEAR);
    }

    //endregion
}
