/*
 * Copyright 2018-2020 Polytech-EDT
 * Licensed under Apache License 2.0 or any later version
 * Refer to the LICENSE.md file included.
 */
package com.polytech.edt.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.polytech.edt.AppCache;
import com.polytech.edt.R;
import com.polytech.edt.calendar.ADEDateTimeInterpreter;
import com.polytech.edt.calendar.ADEEventClickListener;
import com.polytech.edt.calendar.ADEMonthChangeListener;
import com.polytech.edt.config.UserConfig;
import com.polytech.edt.model.calendar.ADECalendar;
import com.polytech.edt.model.calendar.ADEWeekView;
import com.polytech.edt.util.LOGGER;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends NamedFragment {

    private ADEWeekView weekView;
    private ADECalendar calendar;

    public CalendarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        // Get calendar + weekView
        calendar = AppCache.get("calendar");
        weekView = view.findViewById(R.id.weekView);

        // Init week view
        initWeekView();
        setScope();

        // Inflate the layout for this fragment
        return view;
    }

    /**
     * Method to set calendar's scope
     */
    private void setScope() {
        changeVisibility(((UserConfig) AppCache.get("config")).calendarScope().viewScope());
        goToFirstEvent();
    }

    /**
     * Method to init week view
     */
    public void initWeekView() {
        // MonthChange listener
        weekView.setMonthChangeListener(new ADEMonthChangeListener(calendar));

        // DateTimeInterpreter listener
        weekView.setDateTimeInterpreter(new ADEDateTimeInterpreter(weekView));

        // Click listener
        weekView.setOnEventClickListener(new ADEEventClickListener(this));
    }

    /**
     * Method to go to the first event
     */
    private void goToFirstEvent() {
        if (calendar == null) {
            LOGGER.warning("Calendar is null");
            return;
        }
        if (calendar.events().isEmpty()) {
            return;
        }
        weekView.goToDate(calendar.events().get(0).getStartTime());
    }

    /**
     * Method to change day visibility
     *
     * @param i Count day
     */
    public void changeVisibility(int i) {
        weekView.setNumberOfVisibleDays(i);
    }

    @Override
    public int name() {
        return R.string.calendar;
    }
}
