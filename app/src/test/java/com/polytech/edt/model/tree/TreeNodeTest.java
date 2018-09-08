package com.polytech.edt.model.tree;

import org.apache.commons.lang.NullArgumentException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TreeNodeTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void ConstructorTest() throws Exception {
        TreeNode<String, String> node = new TreeNode<>("a", "b");

        Assert.assertEquals("a", node.id());
        Assert.assertEquals("b", node.content());
        Assert.assertTrue(node.isLeaf());

        node = new TreeNode<>("a");

        Assert.assertEquals("a", node.id());
        Assert.assertNull(node.content());
        Assert.assertNull(node.content());
        Assert.assertFalse(node.isLeaf());
    }

    @Test
    public void addChildTest() throws Exception {
        TreeNode<String, String> node = new TreeNode<>("a");

        node.addChild(new TreeNode<String, String>("a"));

        exception.expect(NullArgumentException.class);
        node.addChild(null);

        exception.expect(Exception.class);
        node.addChild(new TreeNode<String, String>("a"));
    }
}
