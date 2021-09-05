/*
 * Copyright 2018-2021 Polytech-EDT
 * Licensed under Apache License 2.0 or any later version
 * Refer to the LICENSE.md file included.
 */
package com.polytech.edt.model.url;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.junit.Assert;
import org.junit.Test;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.function.Predicate;

public class ResourceURLTest {

    @Test
    public void UrlMethodTest() throws Exception {
        URL url = new ResourcesURL().url();

        Assert.assertNotNull(url);
        Assert.assertEquals(ResourcesURL.HOST, url.getHost());
        Assert.assertEquals(ResourcesURL.PORT, url.getPort());
        Assert.assertEquals(ResourcesURL.URL_PATH, url.getPath());

        List<NameValuePair> parameters = URLEncodedUtils.parse(url.toURI(), Charset.forName("UTF-8"));

        Assert.assertEquals(1, parameters.stream().filter(new Predicate<NameValuePair>() {
            @Override
            public boolean test(NameValuePair nameValuePair) {
                return nameValuePair.getName().equals("projectId") && nameValuePair.getValue().equals(ResourcesURL.PROJECT_ID + "");
            }
        }).count());
        Assert.assertEquals(1, parameters.stream().filter(new Predicate<NameValuePair>() {
            @Override
            public boolean test(NameValuePair nameValuePair) {
                return nameValuePair.getName().equals("function") && nameValuePair.getValue().equals(ResourcesURL.FUNCTION);
            }
        }).count());
        Assert.assertEquals(1, parameters.stream().filter(new Predicate<NameValuePair>() {
            @Override
            public boolean test(NameValuePair nameValuePair) {
                return nameValuePair.getName().equals("data") && nameValuePair.getValue().equals(ResourcesURL.TOKEN);
            }
        }).count());
        Assert.assertEquals(1, parameters.stream().filter(new Predicate<NameValuePair>() {
            @Override
            public boolean test(NameValuePair nameValuePair) {
                return nameValuePair.getName().equals("detail") && nameValuePair.getValue().equals(ResourcesURL.DETAIL + "");
            }
        }).count());
    }
}
