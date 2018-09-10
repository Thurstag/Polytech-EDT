package com.polytech.edt.model.url;

import com.polytech.edt.model.ADEResource;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.junit.Assert;
import org.junit.Test;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class CalendarURLTest {

    private final static int scope = 3;
    private final static int id = 2128;

    @Test
    public void UrlMethodTest() throws Exception {
        URL url = new CalendarURL(Collections.singletonList(new ADEResource(id)), scope).url();

        Assert.assertNotNull(url);
        Assert.assertEquals(CalendarURL.HOST, url.getHost());
        Assert.assertEquals(CalendarURL.PORT, url.getPort());
        Assert.assertEquals(CalendarURL.URL_PATH, url.getPath());

        List<NameValuePair> parameters = URLEncodedUtils.parse(url.toURI(), "UTF-8");

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
