package com.polytech.edt.model.tree;

import java.util.List;

/**
 * Node interface
 *
 * @param <T> Id class
 * @param <V> Content class
 */
public interface Node<T, V> {

    List<Node<T, V>> children();

    Node<T, V> child(T id);

    void addChild(Node<T, V> node) throws Exception;

    boolean contains(T id);

    T id();

    V content() throws Exception;

    void setContent(V content);

    boolean isLeaf();
}
