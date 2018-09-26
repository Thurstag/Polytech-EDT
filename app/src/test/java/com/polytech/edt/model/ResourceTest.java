package com.polytech.edt.model;

import com.polytech.edt.AppConfig;
import com.polytech.edt.AppProperty;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AppConfig.class})
public class ResourceTest {

    private static InputStream buffer;

    @BeforeClass
    public static void setUp() throws FileNotFoundException {
        buffer = new FileInputStream("src/test/res/raw/resource.xml");

        // Mock config
        PowerMockito.mockStatic(AppConfig.class);
        PowerMockito.when(AppConfig.get(eq(AppProperty.RESOURCES_LIST))).thenReturn("13,1777,1630,1838,1857,1739,1746,1953,2020,1732,1795,1823,2117,346,2139,2154,2180,2218");
    }

    @Test
    public void loadTest() throws Exception {
        // Simple load
        ADEResource resource = new ADEResource(2128).load();
        Assert.assertNotNull(resource);

        // Mocked load
        resource = mock(ADEResource.class);
        when(resource.fetchResource()).thenReturn(new BufferedInputStream(buffer));
        when(resource.id()).thenCallRealMethod();
        when(resource.name()).thenCallRealMethod();
        when(resource.path()).thenCallRealMethod();
        when(resource.load()).thenCallRealMethod();
        resource = resource.load();

        Assert.assertEquals(2128, resource.id());
        Assert.assertEquals("APP3 TC GrC", resource.name());
        Assert.assertEquals("Polytech Paris-Sud.FISA.FISA 3A.FISA 3A TC.", resource.path());
    }
}
