package com.polytech.edt.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.polytech.edt.AboutListAdaptater;
import com.polytech.edt.R;


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
        listView.setAdapter(new AboutListAdaptater(getContext()));

        return view;
    }

    @Override
    public int name() {
        return R.string.about;
    }
}
