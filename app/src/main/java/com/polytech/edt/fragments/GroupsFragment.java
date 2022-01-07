/*
 * Copyright 2018-2022 Polytech-EDT
 * Licensed under Apache License 2.0 or any later version
 * Refer to the LICENSE.md file included.
 */
package com.polytech.edt.fragments;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Predicate;
import com.polytech.edt.AppCache;
import com.polytech.edt.R;
import com.polytech.edt.android.GroupListAdapter;
import com.polytech.edt.config.UserConfig;
import com.polytech.edt.model.ADEResource;
import com.polytech.edt.model.tree.ADEResourceTree;
import com.polytech.edt.model.tree.Node;
import com.polytech.edt.task.ReloadCalendar;
import com.polytech.edt.util.LOGGER;
import com.polytech.edt.util.ListUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroupsFragment extends NamedFragment {

    Map<String, ADEResource> resourceMap = new HashMap<>();
    ReloadCalendar reloadCalendar = null;

    public GroupsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groups, container, false);

        // Get resources tree
        final List<Node<String, List<ADEResource>>> resources;
        try {
            resources = new ADEResourceTree((List<ADEResource>) AppCache.get("resources")).leaves();
        } catch (Exception e) {
            LOGGER.fatal(e);
            return view;
        }

        // Build list view
        ExpandableListView listView = view.findViewById(R.id.expandable_list_group);
        GroupListAdapter adapter = new GroupListAdapter(getContext(), resources);
        listView.setAdapter(adapter);

        // To map
        for (Node<String, List<ADEResource>> node : resources) {
            for (ADEResource resource : node.content()) {
                resourceMap.put(resource.name(), resource);
            }
        }

        // Define listener
        adapter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            @SuppressLint("StaticFieldLeak")
            public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked) {
                UserConfig config = AppCache.get("config");
                final Set<ADEResource> res = config.groups();

                // Check in map
                if (!resourceMap.containsKey(buttonView.getText().toString())) {
                    LOGGER.warning("Try to add an unknown resource: " + buttonView.getText());
                    return;
                }

                if (isChecked) { // Add to groups
                    res.add(resourceMap.get(buttonView.getText().toString()));
                }
                else { // Remove from groups
                    ListUtil.remove(res, new Predicate<ADEResource>() {
                        @Override
                        public boolean apply(@Nullable ADEResource input) {
                            return input != null && input.name() != null && input.name().equals(buttonView.getText().toString());
                        }
                    });
                }

                // Save groups
                try {
                    config.setGroups(res);
                } catch (JsonProcessingException e) {
                    LOGGER.fatal(e);
                }

                // Kill old task
                if (reloadCalendar != null && AsyncTask.Status.RUNNING.equals(reloadCalendar.getStatus())) {
                    reloadCalendar.cancel(true);
                }

                // Reload calendar in background
                reloadCalendar = new ReloadCalendar(new ArrayList<>(res));
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public int name() {
        return R.string.groups;
    }
}
