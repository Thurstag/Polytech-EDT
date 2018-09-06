package com.polytech.edt.model;

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
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Calendar
 */
public class ADECalendar {

    //region Fields

    private URL url;
    private List<ADEEvent> events;

    //endregion

    //region Constructors


    /**
     * Constructor
     *
     * @param resources Resource list
     * @param scope     Scope (in days)
     */
    public ADECalendar(List<Resource> resources, int scope) throws MalformedURLException {
        url = new CalendarUrl(resources, scope).url();
    }

    /**
     * Constructor
     *
     * @param resources Resource list
     */
    public ADECalendar(List<Resource> resources) throws MalformedURLException {
        url = new CalendarUrl(resources).url();
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
        Calendar calendar = new CalendarBuilder().build(fetchCalendar());   // TODO:Warning ! Calendar can be malformed

        // Load events
        Set<ADEEvent> set = new TreeSet<>();
        for (Object component : calendar.getComponents()) {
            if (component instanceof VEvent) {
                set.add(new ADEEvent((VEvent) component));
            }
        }
        events = new ArrayList<>(set);
        return this;
    }

    /**
     * Method to fetch calendar
     *
     * @return Calendar
     * @throws IOException Download error
     */
    public BufferedInputStream fetchCalendar() throws IOException {
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
