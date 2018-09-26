package com.polytech.edt.model.android;

import android.content.Context;
import android.widget.SimpleExpandableListAdapter;

import com.polytech.edt.R;
import com.polytech.edt.model.ADEResource;
import com.polytech.edt.model.tree.Node;
import com.polytech.edt.util.LOGGER;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupListAdapter extends SimpleExpandableListAdapter {

    private static final String[] keys = new String[]{"KEY"};
    private static final int[] groupIds = new int[]{R.id.expandable_list_header};
    private static final int[] childIds = new int[]{R.id.expandable_list_item};

    public GroupListAdapter(Context context, List<Node<String, List<ADEResource>>> resources) {
        super(context, buildGroups(resources), R.layout.expandable_list_header, keys, groupIds, buildChildren(resources), R.layout.expandable_list_item, keys, childIds);
    }

    private static List<? extends Map<String, ?>> buildGroups(List<Node<String, List<ADEResource>>> resources) {
        List<Map<String, String>> groups = new ArrayList<>();

        // Add groups
        for (Node<String, List<ADEResource>> node : resources) {
            groups.add(new HashMap<String, String>());
            groups.get(groups.size() - 1).put(keys[0], node.id());
        }

        return groups;
    }

    private static List<? extends List<? extends Map<String, ?>>> buildChildren(List<Node<String, List<ADEResource>>> resources) {
        List<List<Map<String, String>>> children = new ArrayList<>();
        for (Node<String, List<ADEResource>> node : resources) {
            List<Map<String, String>> child = new ArrayList<>();

            for (ADEResource resource : node.content()) {
                child.add(new HashMap<String, String>());
                child.get(child.size() - 1).put(keys[0], resource.name());
            }

            children.add(child);
        }

        return children;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        try {
            return super.getChildrenCount(groupPosition);
        } catch (IndexOutOfBoundsException ignored) {
            return 0;
        } catch (Exception e) {
            LOGGER.error(e);
            return 0;
        }
    }
}
