package com.polytech.edt.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.polytech.edt.config.AppConfig;
import com.polytech.edt.config.AppProperty;
import com.polytech.edt.model.url.ResourceURL;

import net.fortuna.ical4j.data.ParserException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.InputSource;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Resource
 * <p>
 * Get all resources: http://ade.polytech.u-psud.fr:8080/jsp/webapi?function=getResources&projectId=2&data=5e3670a1af64840169d64367705be27e51e7ab85056895b426543d6e5ba99179
 * Get one resource: http://ade.polytech.u-psud.fr:8080/jsp/webapi?function=getResources&id=2128&projectId=2&data=5e3670a1af64840169d64367705be27e51e7ab85056895b426543d6e5ba99179&detail=4
 * </p>
 */
public class ADEResource implements ADELoadable<ADEResource> {

    //region Fields

    private static List<String> idList;

    private URL url;

    @JsonProperty
    private int id;

    @JsonProperty
    private String name;

    @JsonProperty
    private String path;

    //endregion

    //region Constructors

    static {
        idList = Arrays.asList(AppConfig.get(AppProperty.RESOURCES_LIST).split(","));
    }

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

    @Override
    public ADEResource load() throws Exception {
        BufferedInputStream resource = fetchResource();
        byte[] contents = new byte[1024];
        int bytesRead;

        // Buffer to string
        StringBuilder builder = new StringBuilder();
        while ((bytesRead = resource.read(contents)) != -1) {
            builder.append(new String(contents, 0, bytesRead));
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
     * Method to fetch resource
     *
     * @return Resource
     */
    public BufferedInputStream fetchResource() throws IOException {
        return new BufferedInputStream(this.url.openStream());
    }

    /**
     * Method to fetch resources
     *
     * @return Resources xml
     */
    private static BufferedInputStream fetchResources() throws IOException {
        return new BufferedInputStream(new ResourceURL(null, 0).url().openStream());
    }

    /**
     * Method to get all resources
     *
     * @return Resources
     */
    public static List<ADEResource> resources() throws InterruptedException {
        final List<ADEResource> resources = new ArrayList<>();
        final Set<Callable<Object>> tasks = new HashSet<>();
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        // Generate tasks
        for (final String key : idList) {
            tasks.add(new Callable<Object>() {
                @Override
                public Object call() {
                    try {
                        resources.add(new ADEResource(Integer.parseInt(key)).load());
                    } catch (Exception ignored) {
                    }
                    return null;
                }
            });
        }

        // Execute & join tasks
        executorService.invokeAll(tasks);

        return resources;
    }

    //endregion
}
