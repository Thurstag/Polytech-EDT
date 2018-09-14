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
import com.polytech.edt.AppConfig;
import com.polytech.edt.R;
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

    @Override
    @SuppressLint("StaticFieldLeak")
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

            private boolean done = false;

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

                // Load config
                AppConfig.loadConfig();

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

                    // TODO: Store resources

                    // Load calendar
                    if (false) {
                        // TODO: Load calendar and store it
                    }

                    done = true;
                } catch (Exception e) {
                    LOGGER.fatal(e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (!done) {
                    return;
                }

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
