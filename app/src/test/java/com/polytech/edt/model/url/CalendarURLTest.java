/*
 * Copyright 2018-2020 Polytech-EDT
 * Licensed under Apache License 2.0 or any later version
 * Refer to the LICENSE.md file included.
 */
package com.polytech.edt.model.url;

import android.content.Context;

import com.polytech.edt.App;
import com.polytech.edt.R;
import com.polytech.edt.config.AppConfig;
import com.polytech.edt.model.ADEResource;
import com.polytech.edt.model.calendar.CalendarUnit;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AppConfig.class})
public class CalendarURLTest {

    private final static int scope = 3;
    private final static int id = 2128;

    @Before
    public void setUp() {
        // Mock context
        Context mMockContext = PowerMockito.mock(Context.class);
        PowerMockito.when(mMockContext.getString(R.string.week)).thenReturn("Week");
        App.context = mMockContext;
    }

    @Test
    public void UrlMethodTest() throws Exception {
        URL url = new CalendarURL(Collections.singletonList(new ADEResource(id)), CalendarUnit.Week, scope).url();

        Assert.assertNotNull(url);
        Assert.assertEquals(CalendarURL.HOST, url.getHost());
        Assert.assertEquals(CalendarURL.PORT, url.getPort());
        Assert.assertEquals(CalendarURL.URL_PATH, url.getPath());

        List<NameValuePair> parameters = URLEncodedUtils.parse(url.toURI(), Charset.forName("UTF-8"));

        Assert.assertEquals(1, parameters.stream().filter(new Predicate<NameValuePair>() {
            @Override
            public boolean test(NameValuePair nameValuePair) {
                return nameValuePair.getName().equals("resources") && nameValuePair.getValue().equals(id + "");
            }
        }).count());
        Assert.assertEquals(1, parameters.stream().filter(new Predicate<NameValuePair>() {
            @Override
            public boolean test(NameValuePair nameValuePair) {
                return nameValuePair.getName().equals("projectId") && nameValuePair.getValue().equals(CalendarURL.PROJECT_ID + "");
            }
        }).count());
        Assert.assertEquals(1, parameters.stream().filter(new Predicate<NameValuePair>() {
            @Override
            public boolean test(NameValuePair nameValuePair) {
                return nameValuePair.getName().equals("calType") && nameValuePair.getValue().equals(CalendarURL.TYPE);
            }
        }).count());
        Assert.assertEquals(1, parameters.stream().filter(new Predicate<NameValuePair>() {
            @Override
            public boolean test(NameValuePair nameValuePair) {
                return nameValuePair.getName().equals("nbWeeks") && nameValuePair.getValue().equals(scope + "");
            }
        }).count());
    }
}
