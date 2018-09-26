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

import java.util.ArrayList;

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
    public static void setUp() throws Exception {
        // Mock config
        PowerMockito.mockStatic(AppConfig.class);
        PowerMockito.when(AppConfig.get(eq(AppProperty.RESOURCES_LIST))).thenReturn("13,1777,1630,1838,1857,1739,1746,1953,2020,1732,1795,1823,2117,346,2139,2154,2180,2218");

        // Mocked load
        ArrayList<ADEResource> resources = new ArrayList<>();
        resources.add(new ADEResource(2128).load());
        resources.add(new ADEResource(2011).load());

        PowerMockito.mockStatic(ADEResource.class);
        PowerMockito.when(ADEResource.resources()).thenReturn(resources);
    }

    @Test
    public void loadTest() throws Exception {
        // Simple load
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
        Assert.assertTrue(tree.child(PPS).child(FISA).child(FISA3).child(FISA_INFO).content() != null);
        Assert.assertTrue(tree.child(PPS).child(FISA).child(FISA3).child(FISA_TC).content() != null);
        Assert.assertEquals(1, tree.child(PPS).child(FISA).child(FISA3).child(FISA_INFO).content().size());
        Assert.assertEquals(1, tree.child(PPS).child(FISA).child(FISA3).child(FISA_TC).content().size());
        Assert.assertEquals(2011, tree.child(PPS).child(FISA).child(FISA3).child(FISA_INFO).content().get(0).id());
        Assert.assertEquals(2128, tree.child(PPS).child(FISA).child(FISA3).child(FISA_TC).content().get(0).id());
    }
}
