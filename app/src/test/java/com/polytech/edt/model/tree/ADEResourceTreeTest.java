package com.polytech.edt.model.tree;

import com.polytech.edt.config.AppConfig;
import com.polytech.edt.config.AppProperty;
import com.polytech.edt.model.ADEResource;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.eq;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ADEResource.class, AppConfig.class})
public class ADEResourceTreeTest {

    private static String PPS = "Polytech Paris-Sud";
    private static String FISA = "FISA";
    private static String FISA3 = "FISA 3A";
    private static String FISA_TC = "FISA 3A TC";
    private static String FISA_INFO = "FISA 3A INFO";

    @BeforeClass
    public static void setUp() throws InterruptedException, MalformedURLException {
        // Mock config
        PowerMockito.mockStatic(AppConfig.class);
        PowerMockito.when(AppConfig.get(eq(AppProperty.RESOURCES_LIST))).thenReturn("941,914,960,975,1076,1088,1121,1184,1239,1310,1380,1428,1433,1571,934,1244,1403,1463,971,1305,1431,1068,1200,1190");

        List<ADEResource> resources = new ArrayList<>();
        resources.add(new MockResource(FISA_INFO, PPS + "." + FISA + "." + FISA3 + "." + FISA_INFO));
        resources.add(new MockResource(FISA_TC, PPS + "." + FISA + "." + FISA3 + "." + FISA_TC));

        // Mock resources
        PowerMockito.mockStatic(ADEResource.class);
        PowerMockito.when(ADEResource.resources()).thenReturn(resources);
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
