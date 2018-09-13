package com.polytech.edt.model.android;

import android.content.Context;
import android.widget.SimpleExpandableListAdapter;

import com.polytech.edt.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupListAdapter extends SimpleExpandableListAdapter {

    private static final String[] keys = new String[]{"KEY"};
    private static final int[] groupIds = new int[]{R.id.expandable_list_header};
    private static final int[] childIds = new int[]{R.id.expandable_list_item};

    public GroupListAdapter(Context context) {
        super(context, buildGroups(), R.layout.expandable_list_header, keys, groupIds, buildChildren(), R.layout.expandable_list_item, keys, childIds);
    }

    private static List<? extends Map<String, ?>> buildGroups() {
        List<Map<String, String>> groups = new ArrayList<>();

        groups.add(new HashMap<String, String>());
        groups.get(0).put(keys[0], "TEST 1");

        groups.add(new HashMap<String, String>());
        groups.get(1).put(keys[0], "TEST 2");

        return groups;
    }

    private static List<? extends List<? extends Map<String, ?>>> buildChildren() {
        List<List<Map<String, String>>> children = new ArrayList<>();

        List<Map<String, String>> child = new ArrayList<>();
        child.add(new HashMap<String, String>());
        child.get(0).put(keys[0], "SUB TEST 1");
        children.add(child);

        child = new ArrayList<>();
        child.add(new HashMap<String, String>());
        child.get(0).put(keys[0], "SUB TEST 2");
        child.add(new HashMap<String, String>());
        child.get(1).put(keys[0], "SUB TEST 2");
        children.add(child);

        return children;
    }
}
