/*
 * Copyright 2018-2020 Polytech-EDT
 * Licensed under Apache License 2.0 or any later version
 * Refer to the LICENSE.md file included.
 */
package com.polytech.edt.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.polytech.edt.BuildConfig;
import com.polytech.edt.model.url.ResourceURL;
import com.polytech.edt.util.LOGGER;

import net.fortuna.ical4j.data.ParserException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.InputSource;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Resource
 * <p>
 * Get one resource: http://ade.polytech.u-psud.fr:8080/jsp/webapi?function=getResources&id=2128&projectId=2&data=5e3670a1af64840169d64367705be27e51e7ab85056895b426543d6e5ba99179&detail=4
 * </p>
 */
public class ADEResource implements ADELoadable<ADEResource> {

    //region Fields

    private URL url;

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
    public ADEResource(@JsonProperty("id") int id) throws MalformedURLException {
        this.id = id;
        this.url = new ResourceURL(id).url();
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

    @Override
    public String toString() {
        return id() + "";
    }

    /**
     * Fetch ADE resources based on their IDs
     *
     * @param resourceIds IDs
     * @return ADE resources
     */
    public static List<ADEResource> resources(Collection<Integer> resourceIds) throws InterruptedException {
        final List<ADEResource> resources = new CopyOnWriteArrayList<>();
        final Set<Callable<Object>> tasks = new HashSet<>();
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        // Generate tasks
        for (final Integer resourceId : resourceIds) {
            tasks.add(new Callable<Object>() {
                @Override
                public Object call() {
                    try {
                        resources.add(new ADEResource(resourceId).load());
                    } catch (Exception exception) {
                        LOGGER.error(exception.getMessage());
                    }
                    return null;
                }
            });
        }

        // Execute & join tasks
        executorService.invokeAll(tasks);

        return resources;
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
    public ADEResource load() throws Exception {
        // Fetch resource
        StringBuilder builder = new StringBuilder();
        try (BufferedInputStream resource = fetchResource()) {
            byte[] contents = new byte[1024];
            int bytesRead;

            while ((bytesRead = resource.read(contents)) != -1) {
                builder.append(new String(contents, 0, bytesRead));
            }
        }

        // Decode xml
        final Document document;
        try {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(builder.toString())));
        } catch (Exception e) {
            throw new ParserException("Failed to decode resource", 89, e);
        }

        // Check xml
        if (document.getFirstChild() == null) {
            throw new Exception("Malformed resource xml");
        }
        if (document.getFirstChild().getFirstChild() == null) {
            throw new Exception("Malformed resource xml");
        }
        if (document.getFirstChild().getFirstChild().getNextSibling() == null) {
            throw new Exception("Malformed resource xml");
        }

        // Get node
        NamedNodeMap map = document.getFirstChild().getFirstChild().getNextSibling().getAttributes();

        // Get data
        this.id = Integer.parseInt(map.getNamedItem("id").getNodeValue());
        this.name = map.getNamedItem("name").getNodeValue();
        this.path = map.getNamedItem("path").getNodeValue();

        return this;
    }

    /**
     * Fetch resource
     *
     * @return Resource
     */
    public BufferedInputStream fetchResource() throws IOException {
        return new BufferedInputStream(this.url.openStream());
    }

    //endregion
}
