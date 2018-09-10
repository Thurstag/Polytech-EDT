package com.polytech.edt.model.url;

import com.polytech.edt.model.ADEResource;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Calendar url
 */
public class CalendarURL implements ADEURL {

    //region Fields

    public static final String HOST = "ade.polytech.u-psud.fr";
    public static final String URL_PATH = "/jsp/custom/modules/plannings/anonymous_cal.jsp";
    public static final int PORT = 8080;
    public static final String TYPE = "ical";
    public static final int PROJECT_ID = 2;

    private List<ADEResource> resources;
    private int scope;

    //endregion

    //region Constructors

    /**
     * Constructor
     *
     * @param resources Resource list
     * @param scope     Scope (in weeks)
     */
    public CalendarURL(List<ADEResource> resources, int scope) {
        this.resources = resources;
        this.scope = scope;
    }

    /**
     * Constructor
     *
     * @param resources Resource list
     */
    public CalendarURL(List<ADEResource> resources) {
        this(resources, 1);
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
        parameters.add(new BasicNameValuePair("projectId", CalendarURL.PROJECT_ID + ""));
        parameters.add(new BasicNameValuePair("calType", CalendarURL.TYPE));
        parameters.add(new BasicNameValuePair("nbWeeks", this.scope + ""));

        return new URL("http", CalendarURL.HOST, CalendarURL.PORT, CalendarURL.URL_PATH + "?" + URLEncodedUtils.format(parameters, "UTF-8"));
    }

    //endregion
}
