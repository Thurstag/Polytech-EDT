/*
 * Copyright 2018-2022 Polytech-EDT
 * Licensed under Apache License 2.0 or any later version
 * Refer to the LICENSE.md file included.
 */
package com.polytech.edt.android;

import android.content.Intent;
import android.net.Uri;

public class WebIntent extends Intent {

    /**
     * Constructor
     *
     * @param url URL
     */
    public WebIntent(String url) {
        super(Intent.ACTION_VIEW, Uri.parse(url));
    }
}
