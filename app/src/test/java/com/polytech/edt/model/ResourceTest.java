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
        new ADEResource(2128).load();

        // Mocked load
        ADEResource mock = mock(ADEResource.class);
        when(mock.fetchResource()).thenReturn(new BufferedInputStream(buffer));
        when(mock.id()).thenCallRealMethod();
        when(mock.name()).thenCallRealMethod();
        when(mock.path()).thenCallRealMethod();
        when(mock.load()).thenCallRealMethod();
        mock = mock.load();

        Assert.assertEquals(2128, mock.id());
        Assert.assertEquals("APP3 TC GrC", mock.name());
        Assert.assertEquals("Polytech Paris-Sud.FISA.FISA 3A.FISA 3A TC.", mock.path());
    }

    @Test
    public void resourcesTest() throws Exception {
        Assert.assertEquals(1537, ADEResource.resources().size());
    }
}
