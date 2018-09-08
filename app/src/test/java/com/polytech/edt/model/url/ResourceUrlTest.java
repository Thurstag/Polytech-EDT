package com.polytech.edt.model.url;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.junit.Assert;
import org.junit.Test;

import java.net.URL;
import java.util.List;
import java.util.function.Predicate;

public class ResourceUrlTest {

    private final static int id = 2128;

    @Test
    public void UrlMethodTest() throws Exception {
        URL url = new ResourceUrl(id).url();

        Assert.assertNotNull(url);
        Assert.assertEquals(ResourceUrl.HOST, url.getHost());
        Assert.assertEquals(ResourceUrl.PORT, url.getPort());
        Assert.assertEquals(ResourceUrl.URL_PATH, url.getPath());

        List<NameValuePair> parameters = URLEncodedUtils.parse(url.toURI(), "UTF-8");

        Assert.assertEquals(1, parameters.stream().filter(new Predicate<NameValuePair>() {
            @Override
            public boolean test(NameValuePair nameValuePair) {
                return nameValuePair.getName().equals("id") && nameValuePair.getValue().equals(id + "");
            }
        }).count());
        Assert.assertEquals(1, parameters.stream().filter(new Predicate<NameValuePair>() {
            @Override
            public boolean test(NameValuePair nameValuePair) {
                return nameValuePair.getName().equals("projectId") && nameValuePair.getValue().equals(ResourceUrl.PROJECT_ID + "");
            }
        }).count());
        Assert.assertEquals(1, parameters.stream().filter(new Predicate<NameValuePair>() {
            @Override
            public boolean test(NameValuePair nameValuePair) {
                return nameValuePair.getName().equals("function") && nameValuePair.getValue().equals(ResourceUrl.FUNCTION);
            }
        }).count());
        Assert.assertEquals(1, parameters.stream().filter(new Predicate<NameValuePair>() {
            @Override
            public boolean test(NameValuePair nameValuePair) {
                return nameValuePair.getName().equals("data") && nameValuePair.getValue().equals(ResourceUrl.TOKEN);
            }
        }).count());
        Assert.assertEquals(1, parameters.stream().filter(new Predicate<NameValuePair>() {
            @Override
            public boolean test(NameValuePair nameValuePair) {
                return nameValuePair.getName().equals("detail") && nameValuePair.getValue().equals(ResourceUrl.DETAIL + "");
            }
        }).count());
    }
}
