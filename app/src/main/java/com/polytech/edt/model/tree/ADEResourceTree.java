package com.polytech.edt.model.tree;

import com.polytech.edt.model.ADEResource;
import com.polytech.edt.util.LOGGER;

import java.util.ArrayList;
import java.util.List;

public class ADEResourceTree extends TreeNode<String, List<ADEResource>> {

    //region Constructors

    /**
     * Constructor
     *
     * @param resources Resources
     */
    public ADEResourceTree(List<ADEResource> resources) throws Exception {
        super(null, null);

        if (resources == null) {
            build(ADEResource.resources());
        }
        else {
            build(resources);
        }
    }

    /**
     * Constructor
     */
    public ADEResourceTree() throws Exception {
        this(null);
    }

    //endregion

    //region Methods

    /**
     * Method to build tree
     */
    private void build(List<ADEResource> resources) throws Exception {
        // Add resources
        for (ADEResource resource : resources) {
            if (resource == null) {
                LOGGER.warning("Try to build a tree with null resource");
                continue;
            }

            // Check path
            if (resource.path() == null) {
                continue;
            }

            // Split path
            String[] parts = resource.path().split("\\.");
            Node<String, List<ADEResource>> current = this;

            // Add resource in tree
            for (String part : parts) {
                // Skip empty string
                if (part.isEmpty()) {
                    continue;
                }

                // Check if node exists
                if (!current.contains(part)) {
                    current.addChild(new TreeNode<>(part, current));
                }

                // Update current
                current = current.child(part);
            }

            if (current != null) {
                if (current.content() == null) {
                    current.setContent(new ArrayList<ADEResource>());
                }
                current.content().add(resource);
            }
        }
    }

    /**
     * Method to get leaves
     *
     * @return Leaves
     */
    public List<Node<String, List<ADEResource>>> leaves() {
        List<Node<String, List<ADEResource>>> nodes = new ArrayList<>();

        // Get leaves
        _leaves(nodes, this);

        return nodes;
    }

    /**
     * Method to retrieve leaves recursively
     *
     * @param nodes List buffer
     * @param node  Current node
     */
    private void _leaves(List<Node<String, List<ADEResource>>> nodes, Node<String, List<ADEResource>> node) {
        if (node.isLeaf()) {
            nodes.add(node);
            return;
        }

        for (Node<String, List<ADEResource>> child : node.children()) {
            _leaves(nodes, child);
        }
    }

    //endregion
}
