package com.polytech.edt.model.url;

import com.polytech.edt.config.AppConfig;
import com.polytech.edt.config.AppProperty;
import com.polytech.edt.model.ADEResource;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import static org.mockito.Matchers.eq;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AppConfig.class})
public class CalendarURLTest {

    private final static int scope = 3;
    private final static int id = 2128;

    @BeforeClass
    public static void setUp() {
        // Mock config
        PowerMockito.mockStatic(AppConfig.class);
        PowerMockito.when(AppConfig.get(eq(AppProperty.RESOURCES_LIST))).thenReturn("13,1777,1630,1838,1857,1739,1746,1953,2020,1732,1795,1823,2117,346,2139,2154,2180,2218");
    }

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
