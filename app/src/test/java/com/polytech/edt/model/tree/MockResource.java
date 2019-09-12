package com.polytech.edt.model.tree;

import com.polytech.edt.model.ADEResource;

import java.net.MalformedURLException;

public class MockResource extends ADEResource {

    MockResource(String name, String path) throws MalformedURLException {
        super(0);

        super.name = name;
        super.path = path;
    }
}
