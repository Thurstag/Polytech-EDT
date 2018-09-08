package com.polytech.edt.model.url;

import com.polytech.edt.model.ADEResource;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Calendar url
 */
public class CalendarUrl implements ADEUrl {

    //region Fields

    public static final String HOST = "ade.polytech.u-psud.fr";
    public static final String URL_PATH = "/jsp/custom/modules/plannings/anonymous_cal.jsp";
    public static final int PORT = 8080;
    public static final String TYPE = "ical";
    public static final int PROJECT_ID = 2;

    private List<ADEResource> resources;
    private int scope;
    private boolean auto;

    //endregion

    //region Constructors

    /**
     * Constructor
     *
     * @param resources Resource list
     * @param scope     Scope (in weeks)
     */
    public CalendarUrl(List<ADEResource> resources, int scope) {
        this.resources = resources;
        this.scope = scope;
        this.auto = false;
    }

    /**
     * Constructor
     *
     * @param resources Resource list
     */
    public CalendarUrl(List<ADEResource> resources) {
        this(resources, 0);
        this.auto = true;

        // Calculate scope
        Calendar calendar = Calendar.getInstance();

        // Today is saturday
        if (calendar.get(Calendar.DAY_OF_WEEK) == 7) {
            auto = false;
            scope = 1;
            return;
        }

        // While day of week isn't Saturday
        while (calendar.get(Calendar.DAY_OF_WEEK) != 7) {
            scope++;

            // Add a day
            calendar.add(Calendar.DATE, 1);
        }
    }

    //endregion

    //region Methods

    /**
     * Method to get url
     */
    public URL url() throws MalformedURLException {
        List<NameValuePair> parameters = new ArrayList<>();

        // Build parameters
        parameters.add(new BasicNameValuePair("resources", StringUtils.join(this.resources, ',')));
        parameters.add(new BasicNameValuePair("projectId", CalendarUrl.PROJECT_ID + ""));
        parameters.add(new BasicNameValuePair("calType", CalendarUrl.TYPE));
        parameters.add(new BasicNameValuePair(auto ? "nbDays" : "nbWeeks", this.scope + ""));

        return new URL("http", CalendarUrl.HOST, CalendarUrl.PORT, CalendarUrl.URL_PATH + "?" + URLEncodedUtils.format(parameters, "UTF-8"));
    }

    //endregion
}
