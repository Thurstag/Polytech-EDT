package com.polytech.edt.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.polytech.edt.R;
import com.polytech.edt.model.android.GroupListAdapter;


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

        // Find list
        ExpandableListView listView = view.findViewById(R.id.expandable_list_group);
        listView.setAdapter(new GroupListAdapter(getContext()));

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public int name() {
        return R.string.groups;
    }
}
