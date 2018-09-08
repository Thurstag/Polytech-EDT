package com.polytech.edt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekViewEvent;
import com.polytech.edt.model.ADECalendar;
import com.polytech.edt.model.ADEEvent;
import com.polytech.edt.model.ADEResource;
import com.polytech.edt.model.ADEWeekView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Class used to develop activities
 */
public class DevActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final DevActivity self = this;
        setContentView(R.layout.activity_dev);

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        // Get a reference for the week view in the layout.
        final ADEWeekView mWeekView = findViewById(R.id.weekView);

        // Avoid thread restrictions
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Fetch calendar
        final ADECalendar c;
        try {
            c = new ADECalendar(Collections.singletonList(new ADEResource(2128))).load();
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
        mWeekView.setOnEventClickListener(new ADEWeekView.EventClickListener() {
            @Override
            public void onEventClick(WeekViewEvent event, RectF eventRect) {
                ADEEvent e;

                if (event instanceof ADEEvent) {
                    e = (ADEEvent)event;
                }
                else {
                    // TODO: Add warning
                    return;
                }

                AlertDialog alertDialog = new AlertDialog.Builder(self).create();
                alertDialog.setTitle(e.getName() + " (" + e.span() + ")");

                alertDialog.setMessage(e.getLocation() + "\n" + e.description());

                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });

        // Go to the first event
        mWeekView.goToDate(c.events().get(0).getStartTime());
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}
