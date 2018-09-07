package com.polytech.edt;

import android.graphics.RectF;
import android.os.Bundle;
import android.app.Activity;
import android.os.StrictMode;
import android.util.Log;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.alamkanak.weekview.WeekViewLoader;
import com.polytech.edt.model.ADECalendar;
import com.polytech.edt.model.ADEEvent;
import com.polytech.edt.model.Resource;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Class used to develop activities
 */
public class DevActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev);

        // Get a reference for the week view in the layout.
        final WeekView mWeekView = findViewById(R.id.weekView);

        // Avoid thread restrictions
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Fetch calendar
        final ADECalendar c;
        try {
            c = new ADECalendar(Collections.singletonList(Resource.APP3_TC_GROUP_C), 1).load();
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return;
        }

        // Implement listener
        mWeekView.setMonthChangeListener(new MonthLoader.MonthChangeListener() {
            @Override
            public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
                List<WeekViewEvent> events = new ArrayList<>();

                for (ADEEvent event : c.events()) {
                    if (event.month() == (newMonth)) {
                        events.add(event);
                    }
                }

                return events;
            }
        });

        // Change DateTimeInterpreter
        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                String weekday = weekdayNameFormat.format(date.getTime());
                SimpleDateFormat format = new SimpleDateFormat("d/M", Locale.getDefault());
                return weekday.toUpperCase() + format.format(date.getTime());
            }

            @Override
            public String interpretTime(int hour) {
                return hour + ":00 -";
            }
        });

        // Implement click callback
        mWeekView.setOnEventClickListener(new WeekView.EventClickListener() {
            @Override
            public void onEventClick(WeekViewEvent event, RectF eventRect) {

            }
        });
    }
}
