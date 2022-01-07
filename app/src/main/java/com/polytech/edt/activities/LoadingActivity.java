/*
 * Copyright 2018-2022 Polytech-EDT
 * Licensed under Apache License 2.0 or any later version
 * Refer to the LICENSE.md file included.
 */
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
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.polytech.edt.App;
import com.polytech.edt.AppCache;
import com.polytech.edt.R;
import com.polytech.edt.config.AppConfig;
import com.polytech.edt.config.UserConfig;
import com.polytech.edt.model.ADEResource;
import com.polytech.edt.model.calendar.ADECalendar;
import com.polytech.edt.util.FileIO;
import com.polytech.edt.util.LOGGER;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LoadingActivity extends AppCompatActivity {

    ProgressBar progressBar;
    TextView waitingText;
    boolean started = false;
    ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
    }

    @Override
    @SuppressLint("StaticFieldLeak")
    protected void onStart() {
        super.onStart();

        // Avoid multiple starts
        if (started) {
            return;
        }
        started = true;

        // Get progress bar and hide it
        progressBar = findViewById(R.id.loading_progress_bar);
        progressBar.setVisibility(View.INVISIBLE);

        // Get waiting text
        waitingText = findViewById(R.id.loading_waiting_text);

        // Start animation
        AnimatedVectorDrawable animator = ((AnimatedVectorDrawable) ((ImageView) findViewById(R.id.loading_logo)).getDrawable());
        animator.start();

        // Define a callback on animation end
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

                // Wait animation end
                try {
                    Thread.sleep(1750);
                } catch (InterruptedException e) {
                    LOGGER.warning(e.getMessage());
                }

                // Load app config
                AppConfig.load();

                try {
                    List<ADEResource> resources = null;
                    boolean resourcesUpdate = false;

                    // Load server resources
                    List<Integer> resourceIds = ADEResource.resourceIds();

                    // Load existing resources
                    if (FileIO.exists(FileIO.ROOT_PATH + "files/resources.json")) {
                        // Decode file
                        resources = mapper.readValue(FileIO.read(getApplicationContext(), "resources.json").getBytes(), new TypeReference<ArrayList<ADEResource>>() {
                        });

                        // Compare local and server resources
                        Set<Integer> diff = new HashSet<>(resourceIds);
                        for (ADEResource resource : resources) {
                            diff.remove(Integer.valueOf(resource.id()));
                        }
                        if (!diff.isEmpty()) {
                            resourcesUpdate = true;
                        }
                    }

                    // Generate resources.json
                    if (resources == null || resourcesUpdate) {
                        // Show loading groups text
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                waitingText.setVisibility(View.VISIBLE);
                            }
                        });

                        // Fetch resources
                        resources = ADEResource.resources(resourceIds);

                        // Encode to json & save into a file
                        FileIO.write(getApplicationContext(), "resources.json", mapper.writeValueAsString(resources));
                    }

                    // Store resources in cache
                    AppCache.save("resources", resources);

                    // Load or create user config
                    UserConfig config;
                    if (!UserConfig.exists()) {
                        config = UserConfig.create();
                    }
                    else {
                        try {
                            config = UserConfig.load();
                        } catch (Exception e) { // Reset config
                            // Notify user
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoadingActivity.this, App.context.getString(R.string.loading_conf_error), Toast.LENGTH_LONG).show();
                                }
                            });

                            config = UserConfig.create();
                        }
                    }

                    // Reset groups
                    if (resourcesUpdate) {
                        config.setGroups(new HashSet<ADEResource>());
                    }

                    // Save in cache
                    AppCache.save("config", config);

                    // Load/Store calendar
                    AppCache.save("calendar", new ADECalendar(new ArrayList<>(config.groups()), config.calendarScope().unit(), config.calendarScope().duration()).load());
                } catch (Exception e) {
                    LOGGER.fatal(e);
                    this.cancel(true);
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
