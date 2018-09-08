package com.polytech.edt.model;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ResourceTest {

    private static InputStream buffer;

    @BeforeClass
    public static void setUp() throws FileNotFoundException {
        buffer = new FileInputStream("src/test/resources/resource.xml");
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
