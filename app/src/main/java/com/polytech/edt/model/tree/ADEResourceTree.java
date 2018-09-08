package com.polytech.edt.model.tree;

import com.polytech.edt.model.ADEResource;

import java.util.ArrayList;
import java.util.List;

public class ADEResourceTree extends TreeNode<String, List<ADEResource>> {

    //region Constructors

    /**
     * Constructor
     */
    public ADEResourceTree() throws Exception {
        super(null, null);

        build();
    }

    //endregion

    //region Methods

    /**
     * Method to build tree
     */
    private void build() throws Exception {
        // Get resources
        List<ADEResource> resources = ADEResource.resources();

        // Add resources
        for (ADEResource resource : resources) {
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
                    current.addChild(new TreeNode<String, List<ADEResource>>(part));
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

    //endregion
}
