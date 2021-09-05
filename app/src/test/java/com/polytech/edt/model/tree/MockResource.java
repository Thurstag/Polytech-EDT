/*
 * Copyright 2018-2021 Polytech-EDT
 * Licensed under Apache License 2.0 or any later version
 * Refer to the LICENSE.md file included.
 */
package com.polytech.edt.model.tree;

import com.polytech.edt.model.ADEResource;

public class MockResource extends ADEResource {

    MockResource(String name, String path) {
        super(0);

        super.name = name;
        super.path = path;
    }
}
