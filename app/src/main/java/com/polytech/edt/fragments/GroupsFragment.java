package com.polytech.edt.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.polytech.edt.AppCache;
import com.polytech.edt.R;
import com.polytech.edt.model.ADEResource;
import com.polytech.edt.model.android.GroupListAdapter;
import com.polytech.edt.model.tree.ADEResourceTree;
import com.polytech.edt.model.tree.Node;
import com.polytech.edt.util.LOGGER;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroupsFragment extends NamedFragment {

    public GroupsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groups, container, false);

        // Get resources tree
        List<Node<String, List<ADEResource>>> resources;
        try {
            resources = new ADEResourceTree((List<ADEResource>) AppCache.get("resources")).leaves();
        } catch (Exception e) {
            LOGGER.fatal(e);
            return view;
        }

        // Find list
        ExpandableListView listView = view.findViewById(R.id.expandable_list_group);
        listView.setAdapter(new GroupListAdapter(getContext(), resources));

        // Edit text
        TextView groups = view.findViewById(R.id.group_count);
        int size = 0;
        for (Node<String, List<ADEResource>> node : resources) {
            for (ADEResource resource : node.content()) {
                size++;
            }
        }
        groups.setText(String.format("%d group%s", size, size > 1 ? "s" : ""));

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public int name() {
        return R.string.groups;
    }
}
