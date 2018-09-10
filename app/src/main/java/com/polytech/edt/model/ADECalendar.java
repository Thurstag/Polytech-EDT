package com.polytech.edt.model;

import com.polytech.edt.model.url.CalendarURL;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VEvent;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Calendar
 */
public class ADECalendar implements ADELoadable<ADECalendar> {

    //region Fields

    private URL url;
    private List<ADEEvent> events;

    //endregion

    //region Constructors


    /**
     * Constructor
     *
     * @param resources Resource list
     * @param scope     Scope (in weeks)
     */
    public ADECalendar(List<ADEResource> resources, int scope) throws MalformedURLException {
        url = new CalendarURL(resources, scope).url();
    }

    /**
     * Constructor
     *
     * @param resources Resource list
     */
    public ADECalendar(List<ADEResource> resources) throws MalformedURLException {
        url = new CalendarURL(resources).url();
    }

    //endregion

    //region Methods

    /**
     * Method to load calendar from remote
     *
     * @throws IOException     Download error
     * @throws ParserException Calendar parsing error
     * @throws ParseException  Date parsing error
     */
    public ADECalendar load() throws IOException, ParserException, ParseException {
        // Fecth & decode calendar
        Calendar calendar = new CalendarBuilder().build(fetchCalendar());

        // Load events
        List<ADEEvent> list = new ArrayList<>();
        for (Object component : calendar.getComponents()) {
            if (component instanceof VEvent) {
                list.add(new ADEEvent((VEvent) component));
            }
        }
        Collections.sort(list);
        events = list;
        return this;
    }

    /**
     * Method to fetch calendar
     *
     * @return Calendar
     * @throws IOException Download error
     */
    BufferedInputStream fetchCalendar() throws IOException {
        return new BufferedInputStream(this.url.openStream());
    }

    /**
     * Method to get events
     *
     * @return Event list
     */
    public List<ADEEvent> events() {
        return events;
    }

    //endregion
}
