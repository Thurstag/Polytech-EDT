package com.polytech.edt.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import com.polytech.edt.AppCache;
import com.polytech.edt.R;
import com.polytech.edt.android.EnumPicker;
import com.polytech.edt.config.UserConfig;
import com.polytech.edt.model.calendar.CalendarUnit;
import com.polytech.edt.util.LOGGER;

import java.util.Arrays;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends NamedFragment {

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        // IT'S AN EXAMPLE
        UserConfig config = AppCache.get("config");

        // Create picker
        EnumPicker<CalendarUnit> picker = new EnumPicker<>(Objects.requireNonNull(getContext()), Arrays.asList(CalendarUnit.values()), config.calendarScope().unit());
        picker.setOnValueChangeListener(new EnumPicker.OnValueChangedListener<CalendarUnit>() {

            @Override
            public void onValueChange(NumberPicker picker, CalendarUnit _old, CalendarUnit _new) {
                LOGGER.debug(_old.label() + " -> " + _new);
            }
        });
        picker.show();
    }

    @Override
    public int name() {
        return R.string.settings;
    }
}
