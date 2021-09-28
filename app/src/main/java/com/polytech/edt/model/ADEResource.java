/*
 * Copyright 2018-2021 Polytech-EDT
 * Licensed under Apache License 2.0 or any later version
 * Refer to the LICENSE.md file included.
 */
package com.polytech.edt.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.polytech.edt.BuildConfig;
import com.polytech.edt.model.url.ResourcesURL;

import net.fortuna.ical4j.data.ParserException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Resource
 * <p>
 * Get all resources: http://ade.polytech.u-psud.fr:8080/jsp/webapi?function=getResources&projectId=2&data=5e3670a1af64840169d64367705be27e51e7ab85056895b426543d6e5ba99179&detail=4
 * </p>
 */
public class ADEResource {

    //region Fields

    @JsonProperty
    protected int id;

    @JsonProperty
    protected String name;

    @JsonProperty
    protected String path;

    //endregion

    //region Constructors

    /**
     * Constructor
     *
     * @param id ID
     */
    @JsonCreator
    public ADEResource(@JsonProperty("id") int id) {
        this.id = id;
    }

    //endregion

    //region Methods

    public String name() {
        return name;
    }

    public int id() {
        return id;
    }

    public String path() {
        return path;
    }

    /**
     * Fetch ADE resources based on their IDs
     *
     * @param resourceIds IDs
     * @return ADE resources
     */
    public static List<ADEResource> resources(Collection<Integer> resourceIds) throws IOException, ParserException {
        List<ADEResource> resources = new ArrayList<>(resourceIds.size());

        // Fetch resources
        Document resourcesDoc = fetchResources();
        NodeList resourceTags = resourcesDoc.getFirstChild().getChildNodes();

        // Search resources
        for (Integer id : resourceIds) {
            for (int i = 0; i < resourceTags.getLength(); i++) {
                Node resourceTag = resourceTags.item(i);
                NamedNodeMap attributes = resourceTag.getAttributes();

                // Skip resources without id attribute
                if (attributes == null || attributes.getNamedItem("id") == null) {
                    continue;
                }

                // Skip unwanted resources
                if (!Objects.equals(Integer.toString(id), attributes.getNamedItem("id").getNodeValue())) {
                    continue;
                }

                // Create a resource
                ADEResource resource = new ADEResource(Integer.parseInt(attributes.getNamedItem("id").getNodeValue()));
                resource.name = attributes.getNamedItem("name").getNodeValue();
                resource.path = attributes.getNamedItem("path").getNodeValue();
                resources.add(resource);
                break;
            }
        }

        return resources;
    }

    /**
     * Fetch XML containing all resources
     *
     * @return XML document
     */
    public static Document fetchResources() throws ParserException, IOException {
        // Fetch resources
        StringBuilder builder = new StringBuilder();
        try (BufferedInputStream resource = new BufferedInputStream(new ResourcesURL().url().openStream())) {
            byte[] contents = new byte[1024];
            int bytesRead;

            while ((bytesRead = resource.read(contents)) != -1) {
                builder.append(new String(contents, 0, bytesRead));
            }
        }

        // Decode xml
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(builder.toString())));

            // Check xml
            if (doc.getFirstChild() == null) {
                throw new Exception("Malformed resource xml");
            }
            if (doc.getFirstChild().getFirstChild() == null) {
                throw new Exception("Malformed resource xml");
            }

            return doc;
        } catch (Exception e) {
            throw new ParserException("Failed to decode resource", 89, e);
        }
    }

    /**
     * Fetch resource IDs stored in git
     *
     * @return IDs
     * @throws Exception Failed to load resource IDs
     */
    public static List<Integer> resourceIds() throws Exception {
        List<Integer> ids = new LinkedList<>();

        // Create URL
        URL url = new URL("https", "raw.githubusercontent.com", 443,
                "/Thurstag/Polytech-EDT/android-" + (Objects.equals(BuildConfig.BUILD_TYPE, "debug") ? "dev" : "master") + "/assets/resources");

        // Fetch resources
        StringBuilder builder = new StringBuilder();
        try (BufferedInputStream resource = new BufferedInputStream(url.openStream())) {
            byte[] contents = new byte[1024];
            int bytesRead;

            while ((bytesRead = resource.read(contents)) != -1) {
                builder.append(new String(contents, 0, bytesRead));
            }
        }

        for (String id : builder.toString().split(",")) {
            ids.add(Integer.parseInt(id));
        }

        return ids;
    }

    @Override
    public String toString() {
        return Integer.toString(id());
    }

    //endregion
}
