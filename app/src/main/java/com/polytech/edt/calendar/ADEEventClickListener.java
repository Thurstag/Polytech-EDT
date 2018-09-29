package com.polytech.edt.calendar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.RectF;

import com.alamkanak.weekview.WeekViewEvent;
import com.polytech.edt.fragments.CalendarFragment;
import com.polytech.edt.model.calendar.ADEEvent;
import com.polytech.edt.model.calendar.ADEWeekView;
import com.polytech.edt.util.LOGGER;

public class ADEEventClickListener implements ADEWeekView.EventClickListener {

    private CalendarFragment calendar;

    /**
     * Constructor
     *
     * @param calendar Calendar
     */
    public ADEEventClickListener(CalendarFragment calendar) {
        this.calendar = calendar;
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        if (!(event instanceof ADEEvent)) {
            LOGGER.warning("Event isn't an ADEEvent");
            return;
        }
        ADEEvent e = (ADEEvent) event;

        AlertDialog alertDialog = new AlertDialog.Builder(calendar.getContext()).create();
        alertDialog.setTitle(e.getName() + " (" + e.span() + ")");
        alertDialog.setMessage(e.getLocation() + "\n" + e.description());

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
