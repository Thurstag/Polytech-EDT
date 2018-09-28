package com.polytech.edt.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.NumberPicker;

import com.polytech.edt.AppCache;
import com.polytech.edt.R;
import com.polytech.edt.android.EnumPickerDialog;
import com.polytech.edt.android.NumberPickerDialog;
import com.polytech.edt.android.OnValueChangedListener;
import com.polytech.edt.android.SettingsListAdapter;
import com.polytech.edt.config.UserConfig;
import com.polytech.edt.model.calendar.CalendarUnit;
import com.polytech.edt.task.ReloadCalendar;
import com.polytech.edt.util.LOGGER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends NamedFragment {

    private ListView listView;
    private ReloadCalendar reloadCalendar = null;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Build list view
        listView = view.findViewById(R.id.settings_list_view);
        listView.setAdapter(new SettingsListAdapter(getContext()));

        // Define listener on click
        listView.setOnItemClickListener(listener());

        return view;
    }

    /**
     * Method to define on item click listener
     *
     * @return Listener
     */
    private AdapterView.OnItemClickListener listener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    // Calendar unit
                    case 0:
                        onCalendarUnitClick();
                        break;

                    case 1:
                        onCalendarDurationClick();
                }
            }
        };
    }

    /**
     * Method triggered on calendar duration click
     */
    private void onCalendarDurationClick() {
        final UserConfig config = AppCache.get("config");

        // Create picker
        NumberPickerDialog picker = new NumberPickerDialog(Objects.requireNonNull(getContext()), 1, 12, config.calendarScope().duration());
        picker.setTitle(getResources().getString(R.string.settings_pick_calendar_duration));
        picker.setOnValueChangeListener(new OnValueChangedListener<Integer>() {
            @Override
            public void onValueChange(NumberPicker picker, Integer _old, Integer _new) {
                try {
                    // Update config
                    config.setCalendarDuration(_new);

                    // Update list
                    listView.setAdapter(new SettingsListAdapter(getContext()));

                    // Kill old task
                    if (reloadCalendar != null && AsyncTask.Status.RUNNING.equals(reloadCalendar.getStatus())) {
                        reloadCalendar.cancel(true);
                    }

                    // Reload calendar in background
                    reloadCalendar = new ReloadCalendar(new ArrayList<>(config.groups()));
                } catch (Exception e) {
                    LOGGER.error(e);
                }
            }
        });

        // Show it
        picker.show();
    }

    /**
     * Method triggered on calendar unit click
     */
    private void onCalendarUnitClick() {
        final UserConfig config = AppCache.get("config");

        // Create picker
        EnumPickerDialog<CalendarUnit> picker = new EnumPickerDialog<>(Objects.requireNonNull(getContext()), Arrays.asList(CalendarUnit.values()), config.calendarScope().unit());
        picker.setTitle(getResources().getString(R.string.settings_pick_calendar_unit));
        picker.setOnValueChangeListener(new OnValueChangedListener<CalendarUnit>() {

            @Override
            public void onValueChange(NumberPicker picker, CalendarUnit _old, CalendarUnit _new) {
                try {
                    // Update config
                    config.setCalendarUnit(_new);

                    // Update list
                    listView.setAdapter(new SettingsListAdapter(getContext()));

                    // Kill old task
                    if (reloadCalendar != null && AsyncTask.Status.RUNNING.equals(reloadCalendar.getStatus())) {
                        reloadCalendar.cancel(true);
                    }

                    // Reload calendar in background
                    reloadCalendar = new ReloadCalendar(new ArrayList<>(config.groups()));
                } catch (Exception e) {
                    LOGGER.error(e);
                }
            }
        });

        // Show it
        picker.show();
    }

    @Override
    public int name() {
        return R.string.settings;
    }
}
