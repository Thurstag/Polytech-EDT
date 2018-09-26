package com.polytech.edt.model.url;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ResourceURL implements ADEURL {

    //region Fields

    public static final String HOST = "ade.polytech.u-psud.fr";
    public static final String URL_PATH = "/jsp/webapi";
    public static final int PORT = 8080;
    public static final int PROJECT_ID = 2;
    public static final String FUNCTION = "getResources";
    public static final String TOKEN = "5e3670a1af64840169d64367705be27e51e7ab85056895b426543d6e5ba99179";
    public static final int DETAIL = 4;

    private Integer id;
    private int detail;

    //endregion

    //region Constructors

    /**
     * Constructor
     *
     * @param id     ID
     * @param detail Detail
     */
    public ResourceURL(Integer id, int detail) {
        this.id = id;
        this.detail = detail;
    }

    /**
     * Constructor
     *
     * @param id ID
     */
    public ResourceURL(Integer id) {
        this(id, ResourceURL.DETAIL);
    }

    /**
     * Constructor
     */
    public ResourceURL() {
        this(null);
    }

    //endregion

    //region Methods

    /**
     * Method to get url
     */
    public URL url() throws MalformedURLException {
        List<NameValuePair> parameters = new ArrayList<>();

        // Build parameters
        parameters.add(new BasicNameValuePair("function", ResourceURL.FUNCTION));
        if (id != null) {
            parameters.add(new BasicNameValuePair("id", id.toString()));
        }
        parameters.add(new BasicNameValuePair("data", ResourceURL.TOKEN));
        parameters.add(new BasicNameValuePair("projectId", ResourceURL.PROJECT_ID + ""));
        parameters.add(new BasicNameValuePair("detail", detail + ""));

        return new URL("http", ResourceURL.HOST, ResourceURL.PORT, ResourceURL.URL_PATH + "?" + URLEncodedUtils.format(parameters, "UTF-8"));
    }

    //endregion
}
