package com.polytech.edt.model.android;

import android.content.Intent;

public class MailIntent extends Intent {

    /**
     * Constructor
     *
     * @param to      Mail address
     * @param subject Subject
     * @param body    Body
     */
    public MailIntent(String[] to, String subject, String body) {
        super(Intent.ACTION_SEND);

        setType("plain/text");
        putExtra(Intent.EXTRA_EMAIL, to);
        putExtra(Intent.EXTRA_SUBJECT, subject);
        putExtra(Intent.EXTRA_TEXT, body);
    }
}
