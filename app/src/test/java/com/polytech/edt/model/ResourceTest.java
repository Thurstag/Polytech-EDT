/*
 * Copyright 2018-2020 Polytech-EDT
 * Licensed under Apache License 2.0 or any later version
 * Refer to the LICENSE.md file included.
 */
package com.polytech.edt.model;

import com.polytech.edt.config.AppConfig;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ADEResource.class, AppConfig.class})
public class ResourceTest {

    private static InputStream buffer;

    @BeforeClass
    public static void setUp() throws FileNotFoundException {
        buffer = new FileInputStream("src/test/res/raw/resource.xml");
    }

    @Test
    public void loadTest() throws Exception {
        // Mock static methods
        mockStatic(ADEResource.class);
        when(ADEResource.fetchResources()).thenReturn(DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new BufferedInputStream(buffer)));
        when(ADEResource.resources(any(List.class))).thenCallRealMethod();

        // Simple load
        ADEResource resource = new ADEResource(2128);
        Assert.assertNotNull(resource);

        resource = ADEResource.resources(Collections.singletonList(2128)).get(0);
        Assert.assertEquals(2128, resource.id());
        Assert.assertEquals("APP3 TC GrC", resource.name());
        Assert.assertEquals("Polytech Paris-Sud.FISA.FISA 3A.FISA 3A TC.", resource.path());
    }
}
