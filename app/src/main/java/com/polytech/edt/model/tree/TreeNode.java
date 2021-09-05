/*
 * Copyright 2018-2021 Polytech-EDT
 * Licensed under Apache License 2.0 or any later version
 * Refer to the LICENSE.md file included.
 */
package com.polytech.edt.model.tree;

import org.apache.commons.lang.NullArgumentException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeNode<T, V> implements Node<T, V> {
    //region Fields

    private T id;
    private V content;
    private Node<T, V> parent;

    protected Map<T, Node<T, V>> children;

    //endregion

    //region Constructors

    /**
     * Constructor
     *
     * @param id      Id
     * @param parent  Parent
     * @param content Content
     */
    public TreeNode(T id, Node<T, V> parent, V content) {
        this.id = id;
        this.content = content;
        this.children = new HashMap<>();
        this.parent = parent;
    }

    /**
     * Constructor
     *
     * @param id Id
     */
    public TreeNode(T id, Node<T, V> parent) {
        this(id, parent, null);
    }

    //endregion

    //region GET

    @Override
    public T id() {
        return id;
    }

    @Override
    public V content() {
        return content;
    }

    @Override
    public void setContent(V content) {
        this.content = content;
    }

    @Override
    public boolean isLeaf() {
        return this.children.size() == 0;
    }

    @Override
    public Node<T, V> parent() {
        return parent;
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
