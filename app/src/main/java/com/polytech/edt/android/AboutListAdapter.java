/*
 * Copyright 2018-2021 Polytech-EDT
 * Licensed under Apache License 2.0 or any later version
 * Refer to the LICENSE.md file included.
 */
package com.polytech.edt.android;

import android.content.Context;
import android.widget.SimpleAdapter;

import com.polytech.edt.App;
import com.polytech.edt.BuildConfig;
import com.polytech.edt.R;
import com.polytech.edt.config.AppConfig;
import com.polytech.edt.config.AppProperty;
import com.polytech.edt.util.LOGGER;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AboutListAdapter extends SimpleAdapter {

    /**
     * Constructor
     *
     * @param context  The context where the View associated with this SimpleAdapter is running
     * @param data     A List of Maps. Each entry in the List corresponds to one row in the list. The
     *                 Maps contain the data for each row, and should include all the entries specified in
     *                 "from"
     * @param resource Resource identifier of a view layout that defines the views for this list
     *                 item. The layout file should include at least those named views defined in "to"
     * @param from     A list of column names that will be added to the Map associated with each
     *                 item.
     * @param to       The views that should display column in the "from" parameter. These should all be
     *                 TextViews. The first N views in this list are given the values of the first N columns
     */
    private AboutListAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
    }

    public AboutListAdapter(Context context) {
        this(context, data(), R.layout.list_item, new String[]{"1_line", "2_line"}, new int[]{R.id.item_title, R.id.item_subtitle});
    }

    private static List<? extends Map<String, ?>> data() {


        List<Map<String, String>> list = new ArrayList<>();
        String[][] data = new String[][]{
                {App.context.getString(R.string.version), BuildConfig.VERSION_NAME},
                {App.context.getString(R.string.build_date), BuildConfig.BUILD_DATE},
                {App.context.getString(R.string.git_repo), "https://github.com"},
                {App.context.getString(R.string.git_revision), BuildConfig.GIT_COMMIT_SHA},
                {App.context.getString(R.string.devs), "Thomas Capodano & Sasha Berthout"},
                {App.context.getString(R.string.license), "GNU General Public License v3.0"},
                {App.context.getString(R.string.contact), AppConfig.get(AppProperty.REPORT_MAIL)}
        };

        for (String[] d : data) {
            if (d.length != 2) {
                LOGGER.warning("Array's length must be equal to 2");
                continue;
            }
            Map<String, String> map = new HashMap<>();

            map.put("1_line", d[0]);
            map.put("2_line", d[1]);

            list.add(map);
        }

        return list;
    }
}
