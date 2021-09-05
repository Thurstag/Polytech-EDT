/*
 * Copyright 2018-2021 Polytech-EDT
 * Licensed under Apache License 2.0 or any later version
 * Refer to the LICENSE.md file included.
 */
package com.polytech.edt.model.url;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ResourcesURL implements ADEURL {

    //region Fields

    public static final String HOST = "ade.polytech.u-psud.fr";
    public static final String URL_PATH = "/jsp/webapi";
    public static final int PORT = 8080;
    public static final int PROJECT_ID = CalendarURL.PROJECT_ID;
    public static final String FUNCTION = "getResources";
    public static final String TOKEN = "5e3670a1af64840169d64367705be27e51e7ab85056895b426543d6e5ba99179";
    public static final int DETAIL = 4;

    //endregion

    //region Methods

    /**
     * Get URL
     */
    public URL url() throws MalformedURLException {
        List<NameValuePair> parameters = new ArrayList<>();

        // Build parameters
        parameters.add(new BasicNameValuePair("function", ResourcesURL.FUNCTION));
        parameters.add(new BasicNameValuePair("data", ResourcesURL.TOKEN));
        parameters.add(new BasicNameValuePair("projectId", Integer.toString(ResourcesURL.PROJECT_ID)));
        parameters.add(new BasicNameValuePair("detail", Integer.toString(DETAIL)));

        return new URL("http", ResourcesURL.HOST, ResourcesURL.PORT, ResourcesURL.URL_PATH + "?" + URLEncodedUtils.format(parameters, "UTF-8"));
    }

    //endregion
}
