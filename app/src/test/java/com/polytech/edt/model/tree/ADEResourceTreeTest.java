/*
 * Copyright 2018-2021 Polytech-EDT
 * Licensed under Apache License 2.0 or any later version
 * Refer to the LICENSE.md file included.
 */
package com.polytech.edt.model.tree;

import com.polytech.edt.config.AppConfig;
import com.polytech.edt.model.ADEResource;

import net.fortuna.ical4j.data.ParserException;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ADEResource.class, AppConfig.class})
public class ADEResourceTreeTest { // TODO: Use assertj + git action

    private static final String PPS = "Polytech Paris-Sud";
    private static final String FISA = "FISA";
    private static final String FISA3 = "FISA 3A";
    private static final String FISA_TC = "FISA 3A TC";
    private static final String FISA_INFO = "FISA 3A INFO";

    @BeforeClass
    public static void setUp() throws IOException, ParserException {
        // Generate resource list

        List<ADEResource> resources = new ArrayList<>();
        resources.add(new MockResource(FISA_INFO, PPS + "." + FISA + "." + FISA3 + "." + FISA_INFO));
        resources.add(new MockResource(FISA_TC, PPS + "." + FISA + "." + FISA3 + "." + FISA_TC));

        // Mock resources
        PowerMockito.mockStatic(ADEResource.class);
        PowerMockito.when(ADEResource.resources(any(List.class))).thenReturn(resources);
    }

    @Test
    public void loadAndLeavesTest() throws Exception {
        ADEResourceTree tree = new ADEResourceTree();

        Assert.assertNotNull(tree);
        Assert.assertEquals(1, tree.children.size());
        Assert.assertTrue(tree.contains(PPS));

        Assert.assertEquals(1, tree.child(PPS).children().size());
        Assert.assertTrue(tree.child(PPS).contains(FISA));

        Assert.assertEquals(1, tree.child(PPS).child(FISA).children().size());
        Assert.assertTrue(tree.child(PPS).child(FISA).contains(FISA3));

        Assert.assertEquals(2, tree.child(PPS).child(FISA).child(FISA3).children().size());
        Assert.assertTrue(tree.child(PPS).child(FISA).child(FISA3).contains(FISA_INFO));
        Assert.assertTrue(tree.child(PPS).child(FISA).child(FISA3).contains(FISA_TC));

        Assert.assertTrue(tree.child(PPS).child(FISA).child(FISA3).child(FISA_INFO).isLeaf());
        Assert.assertTrue(tree.child(PPS).child(FISA).child(FISA3).child(FISA_TC).isLeaf());
    }
}
