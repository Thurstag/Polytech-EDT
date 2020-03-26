/*
 * Copyright 2018-2020 Polytech-EDT
 * Licensed under Apache License 2.0 or any later version
 * Refer to the LICENSE.md file included.
 */
package com.polytech.edt.android;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.polytech.edt.App;
import com.polytech.edt.AppCache;
import com.polytech.edt.R;
import com.polytech.edt.config.UserConfig;
import com.polytech.edt.util.LOGGER;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingsListAdapter extends SimpleAdapter {

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
    private SettingsListAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
    }

    public SettingsListAdapter(Context context) {
        this(context, data(), R.layout.list_item_with_icon, new String[]{"1_line", "2_line"}, new int[]{R.id.item_title, R.id.item_subtitle});
    }

    private static List<? extends Map<String, ?>> data() {
        UserConfig config = AppCache.get("config");

        List<Map<String, String>> list = new ArrayList<>();
        String[][] data = new String[][]{
                {App.context.getString(R.string.calendar_unit), config.calendarScope().unit().label()},
                {App.context.getString(R.string.calendar_duration), String.format("%d %s%s",
                        config.calendarScope().duration(),
                        config.calendarScope().unit().label().toLowerCase(),
                        config.calendarScope().duration() > 1 ? (config.calendarScope().unit().label().toLowerCase().endsWith("s") ? "" : "s") : "")},
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        ImageView icon = view.findViewById(R.id.item_icon);
        switch (position) {
            // Calendar unit
            case 0:
                icon.setImageResource(R.drawable.ic_date_range);
                break;

            // Calendar duration
            case 1:
                icon.setImageResource(R.drawable.ic_access_time);
                break;
        }

        return view;
    }
}
