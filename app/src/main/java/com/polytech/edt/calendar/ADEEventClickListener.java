package com.polytech.edt.calendar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.RectF;

import com.alamkanak.weekview.WeekViewEvent;
import com.polytech.edt.Calendar;
import com.polytech.edt.model.ADEEvent;
import com.polytech.edt.model.ADEWeekView;

public class ADEEventClickListener implements ADEWeekView.EventClickListener {

    private Calendar calendar;

    /**
     * Constructor
     *
     * @param calendar Calendar
     */
    public ADEEventClickListener(Calendar calendar) {
        this.calendar = calendar;
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        ADEEvent e;

        if (event instanceof ADEEvent) {
            e = (ADEEvent) event;
        }
        else {
            // TODO: Add warning
            return;
        }

        AlertDialog alertDialog = new AlertDialog.Builder(calendar).create();
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
}
