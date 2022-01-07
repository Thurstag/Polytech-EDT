/*
 * Copyright 2018-2022 Polytech-EDT
 * Licensed under Apache License 2.0 or any later version
 * Refer to the LICENSE.md file included.
 */
package com.polytech.edt.model.url;

import com.polytech.edt.model.ADEResource;
import com.polytech.edt.model.calendar.CalendarUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

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
    public static final int PROJECT_ID = 7;

    private List<ADEResource> resources;
    private CalendarUnit unit;
    private int viewScope;

    //endregion

    //region Constructors

    /**
     * Constructor
     *
     * @param resources Resource list
     * @param unit      Calendar unit
     * @param viewScope View unit
     */
    public CalendarURL(List<ADEResource> resources, CalendarUnit unit, int viewScope) {
        this.resources = resources;
        this.unit = unit;
        this.viewScope = viewScope;
    }

    /**
     * Constructor
     *
     * @param resources Resource list
     */
    public CalendarURL(List<ADEResource> resources) {
        this(resources, CalendarUnit.Week, 1);
    }

    //endregion

    //region Methods

    /**
     * Get URL
     */
    public URL url() throws Exception {
        List<NameValuePair> parameters = new ArrayList<>();

        // Build parameters
        parameters.add(new BasicNameValuePair("resources", StringUtils.join(resources, ',')));
        parameters.add(new BasicNameValuePair("projectId", CalendarURL.PROJECT_ID + ""));
        parameters.add(new BasicNameValuePair("calType", CalendarURL.TYPE));
        parameters.add(new BasicNameValuePair(unit.URLParameter(), unit.transformScope(viewScope) + ""));

        return new URL("http", CalendarURL.HOST, CalendarURL.PORT, CalendarURL.URL_PATH + "?" + URLEncodedUtils.format(parameters, "UTF-8"));
    }

    //endregion
}
