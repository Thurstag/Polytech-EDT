package com.polytech.edt.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.polytech.edt.AppCache;
import com.polytech.edt.R;
import com.polytech.edt.config.AppConfig;
import com.polytech.edt.config.UserConfig;
import com.polytech.edt.model.ADECalendar;
import com.polytech.edt.model.ADEResource;
import com.polytech.edt.util.FileIO;
import com.polytech.edt.util.LOGGER;

import java.util.ArrayList;
import java.util.List;

public class LoadingActivity extends AppCompatActivity {

    ProgressBar progressBar;
    boolean started = false;
    ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onStart() {
        super.onStart();

        if (started) {
            return;
        }
        started = true;

        // Get progress bar and hide it
        progressBar = findViewById(R.id.loading_progress_bar);
        progressBar.setVisibility(View.INVISIBLE);

        // Start animation
        AnimatedVectorDrawable animator = ((AnimatedVectorDrawable) ((ImageView) findViewById(R.id.loading_logo)).getDrawable());
        animator.start();

        // Define a callback on animation end
        // TODO: Create a class
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                // Show progress bar
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                });

                try {
                    Thread.sleep(1750);
                } catch (InterruptedException e) {
                    LOGGER.warning(e.getMessage());
                }

                // Load app config
                AppConfig.load();

                try {
                    List<ADEResource> resources;

                    // Load/Store resources
                    if (FileIO.exists(FileIO.ROOT_PATH + "files/resources.json")) {
                        // Decode file
                        resources = mapper.readValue(FileIO.read(getApplicationContext(), "resources.json").getBytes(), new TypeReference<ArrayList<ADEResource>>() {
                        });
                    }
                    else {
                        // Fetch resources
                        resources = ADEResource.resources();

                        // Encode to json & save into a file
                        FileIO.write(getApplicationContext(), "resources.json", mapper.writeValueAsString(resources));
                    }

                    // Store resources in cache
                    AppCache.save("resources", resources);

                    // Load/Store user config
                    if (!UserConfig.exists()) {
                        AppCache.save("config", UserConfig.create());
                    }
                    else {
                        AppCache.save("config", UserConfig.load());
                    }

                    // TODO: Change this and retrieve user's groups (When group selection is implemented)
                    String[] items = "366,1081,1634,1685,1745,1779,1805,1872,1985,2116,2127,2204,1677,2157,1690,1698,1770,1807,1937,1949,2067,2081,2137,1924,1935,364,1975,2066,2185,2197,51,59,65,71,119,120,121,122,267,1097,1670,1708,1710,1816,1864,1867,1925,2035,2076,2084,2153,2155,2158,2173,2292,2293,2294,2295,2298,2299,2300,2301,2302,2303,2304,2305,1738,1806,1996,1982,2135,2005,1869,2026,2151,2014,1916,2043,1682,1825,1687,1693,1725,1772,1817,1846,1887,2042,2078,2128,2167,2177,2193,1769,1970,2034,1804,2049,2073,2011,2175,2064,2087".split(",");
                    List<ADEResource> res = new ArrayList<>();
                    for (String item : items) {
                        res.add(new ADEResource(Integer.parseInt(item)));
                    }

                    // Load/Store calendar
                    AppCache.save("calendar", new ADECalendar(res).load());
                } catch (Exception e) {
                    LOGGER.fatal(e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                // Stop progress bar
                progressBar.setVisibility(View.GONE);

                // Go to the main activity
                startActivity(new Intent(LoadingActivity.this, CalendarActivity.class));
                finish();

                // Define a transition
                overridePendingTransition(R.anim.activity_slide_in, R.anim.activity_slide_out);
            }
        }.execute();
    }
}
