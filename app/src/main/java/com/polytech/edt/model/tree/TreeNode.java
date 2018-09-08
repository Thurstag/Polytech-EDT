package com.polytech.edt.model.tree;

import org.apache.commons.lang.NullArgumentException;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeNode<T, V> implements Node<T, V> {
    //region Fields

    private T id;
    private V content;

    protected Map<T, Node<T, V>> children;

    //endregion

    //region Constructors

    /**
     * Constructor
     *
     * @param id Id
     * @param content Content
     */
    public TreeNode(T id, V content) {
        this.id = id;
        this.content = content;
        this.children = new HashMap<>();
    }

    /**
     * Constructor
     *
     * @param id Id
     */
    public TreeNode(T id) {
        this(id, null);
    }

    //endregion

    //region GET

    @Override
    public T id() {
        return id;
    }

    @Override
    public V content() throws Exception {
        return content;
    }

    @Override
    public void setContent(V content) {
        this.content = content;
    }

    @Override
    public boolean isLeaf() {
        return content != null;
    }

    //endregion

    //region Methods

    @Override
    public List<Node<T, V>> children() {
        return new ArrayList<>(children.values());
    }

    @Override
    public Node<T, V> child(T id) {
        return children.get(id);
    }

    @Override
    public void addChild(Node<T, V> node) throws Exception {
        if (node == null) {
            throw new NullArgumentException("node");
        }
        if (children.containsKey(node.id())) {
            throw new Exception("Node with id: '" + node.id() + "' already exists");
        }
        children.put(node.id(), node);
    }

    @Override
    public boolean contains(T id) {
        return children.containsKey(id);
    }

    //endregion
}
