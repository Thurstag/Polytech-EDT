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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.emory.mathcs.backport.java.util.Arrays;

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
        private static final Set<String> ALLOWED_CATEGORIES = new HashSet<>(Arrays.asList(new String[]{"trainee", "instructor", "classroom", "category5"}));

        public static void main(String[] args) throws Exception {
            // Get resources
            Document resourcesDoc = ADEResource.fetchResources();
            NodeList resourceTags = resourcesDoc.getFirstChild().getChildNodes();
            Set<String> skippedCategories = new HashSet<>();

            // Gather resources
            List<Integer> resourceIds = new ArrayList<>();
            for (int i = 0; i < resourceTags.getLength(); i++) {
                Node resourceTag = resourceTags.item(i);
                NamedNodeMap attributes = resourceTag.getAttributes();

                if (attributes == null) {   // Skip resources without attributes
                    continue;
                }
                else if (attributes.getNamedItem("id") == null) {   // Skip resources without id attribute
                    continue;
                }
                else if (!ALLOWED_CATEGORIES.contains(attributes.getNamedItem("category").getNodeValue())) {    // Skip resources without allowed categories
                    skippedCategories.add(attributes.getNamedItem("category").getNodeValue());
                    continue;
                }

                resourceIds.add(Integer.valueOf(attributes.getNamedItem("id").getNodeValue()));
            }

            // Print resources
            System.out.println("Skipped categories: " + Arrays.toString(skippedCategories.toArray()));
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
