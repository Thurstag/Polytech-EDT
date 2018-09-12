package com.polytech.edt;

import android.content.Context;
import android.widget.SimpleAdapter;

import com.polytech.edt.util.LOGGER;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AboutListAdaptater extends SimpleAdapter {

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
    private AboutListAdaptater(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
    }

    public AboutListAdaptater(Context context) {
        this(context, data(), R.layout.list_item, new String[]{"1_line", "2_line"}, new int[]{R.id.item_title, R.id.item_subtitle});
    }

    private static List<? extends Map<String, ?>> data() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss", Locale.FRANCE);
        
        List<Map<String, String>> list = new ArrayList<>();
        String[][] data = new String[][]{
                {App.context.getString(R.string.version), BuildConfig.VERSION_NAME},
                {App.context.getString(R.string.build_date), format.format(BuildConfig.BUILD_DATE)},
                {App.context.getString(R.string.git_repo), "https://github.com"},
                {App.context.getString(R.string.git_revision), BuildConfig.GIT_COMMIT_SHA},
                {App.context.getString(R.string.devs), "Thomas Capodano & Sasha Berthout"},
                {App.context.getString(R.string.license), "GNU General Public License v3.0"}
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
