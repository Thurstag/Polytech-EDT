package com.polytech.edt.task;

import android.os.AsyncTask;

import com.polytech.edt.AppCache;
import com.polytech.edt.config.UserConfig;
import com.polytech.edt.model.ADECalendar;
import com.polytech.edt.model.ADEResource;
import com.polytech.edt.util.LOGGER;

import java.util.Arrays;

public class ReloadCalendar extends AsyncTask<ADEResource, Void, Void> {

    private Runnable callback = null;

    public void setCallback(Runnable callback) {
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(ADEResource... adeResources) {
        try {
            UserConfig config = AppCache.get("config");

            AppCache.save("calendar", new ADECalendar(Arrays.asList(adeResources), config.calendarScope().unit(), config.calendarScope().duration()).load());
        } catch (Exception e) {
            LOGGER.error(e);
        } finally {
            if (callback != null) {
                callback.run();
            }
        }
        return null;
    }
}
