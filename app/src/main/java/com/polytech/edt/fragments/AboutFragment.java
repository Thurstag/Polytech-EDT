/*
 * Copyright 2018-2022 Polytech-EDT
 * Licensed under Apache License 2.0 or any later version
 * Refer to the LICENSE.md file included.
 */
package com.polytech.edt.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.polytech.edt.BuildConfig;
import com.polytech.edt.R;
import com.polytech.edt.android.AboutListAdapter;
import com.polytech.edt.android.MailIntent;
import com.polytech.edt.android.WebIntent;
import com.polytech.edt.config.AppConfig;
import com.polytech.edt.config.AppProperty;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends NamedFragment {


    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        // Build list view
        ListView listView = view.findViewById(R.id.about_list_view);
        listView.setAdapter(new AboutListAdapter(getContext()));

        // Define listener on click
        listView.setOnItemClickListener(listener());

        return view;
    }

    @Override
    public int name() {
        return R.string.about;
    }

    /**
     * Define on item click listener
     *
     * @return Listener
     */
    public AdapterView.OnItemClickListener listener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    // Git repo
                    case 2:
                        startActivity(new WebIntent("https://github.com/Thurstag/Polytech-EDT"));
                        break;

                    // Git rev
                    case 3:
                        startActivity(new WebIntent("https://github.com/Thurstag/Polytech-EDT/commit/" + BuildConfig.GIT_COMMIT_SHA));
                        break;

                    // Contact
                    case 6:
                        startActivity(new MailIntent(new String[]{AppConfig.get(AppProperty.REPORT_MAIL)}, "", ""));
                        break;
                }
            }
        };
    }
}
