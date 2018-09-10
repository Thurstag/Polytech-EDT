package com.polytech.edt.model.url;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.junit.Assert;
import org.junit.Test;

import java.net.URL;
import java.util.List;
import java.util.function.Predicate;

public class ResourceURLTest {

    private final static int id = 2128;

    @Test
    public void UrlMethodTest() throws Exception {
        URL url = new ResourceURL(id).url();

        Assert.assertNotNull(url);
        Assert.assertEquals(ResourceURL.HOST, url.getHost());
        Assert.assertEquals(ResourceURL.PORT, url.getPort());
        Assert.assertEquals(ResourceURL.URL_PATH, url.getPath());

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
                return nameValuePair.getName().equals("projectId") && nameValuePair.getValue().equals(ResourceURL.PROJECT_ID + "");
            }
        }).count());
        Assert.assertEquals(1, parameters.stream().filter(new Predicate<NameValuePair>() {
            @Override
            public boolean test(NameValuePair nameValuePair) {
                return nameValuePair.getName().equals("function") && nameValuePair.getValue().equals(ResourceURL.FUNCTION);
            }
        }).count());
        Assert.assertEquals(1, parameters.stream().filter(new Predicate<NameValuePair>() {
            @Override
            public boolean test(NameValuePair nameValuePair) {
                return nameValuePair.getName().equals("data") && nameValuePair.getValue().equals(ResourceURL.TOKEN);
            }
        }).count());
        Assert.assertEquals(1, parameters.stream().filter(new Predicate<NameValuePair>() {
            @Override
            public boolean test(NameValuePair nameValuePair) {
                return nameValuePair.getName().equals("detail") && nameValuePair.getValue().equals(ResourceURL.DETAIL + "");
            }
        }).count());
    }
}
