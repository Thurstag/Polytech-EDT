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
