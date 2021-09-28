/*
 * Copyright 2021-2021 Polytech-EDT
 * Licensed under Apache License 2.0 or any later version
 * Refer to the LICENSE.md file included.
 */
package com.polytech.edt.tools;

import com.polytech.edt.model.ADEResource;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * Set of tools for developers
 */
class Tools {
    private Tools() {
        // Disable instantiation
    }

    /**
     * Tool to get the list of resources to store in assets/resources
     */
    static class ResourcesFetcher {
        public static void main(String[] args) throws Exception {
            // Get resources
            Document resourcesDoc = ADEResource.fetchResources();
            NodeList resourceTags = resourcesDoc.getFirstChild().getChildNodes();

            // Gather resources
            List<Integer> resourceIds = new ArrayList<>();
            for (int i = 0; i < resourceTags.getLength(); i++) {
                Node resourceTag = resourceTags.item(i);
                NamedNodeMap attributes = resourceTag.getAttributes();

                // Skip resources without id attribute
                if (attributes == null || attributes.getNamedItem("id") == null) {
                    continue;
                }

                resourceIds.add(Integer.valueOf(attributes.getNamedItem("id").getNodeValue()));
            }

            // Print resources
            System.out.println(resourceIds.size() + " resource(s) found:");
            for (int i = 0; i < resourceIds.size(); i++) {
                System.out.print(resourceIds.get(i));

                if (i != resourceIds.size() - 1) {
                    System.out.print(',');
                }
            }
        }
    }
}
