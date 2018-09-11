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
import com.polytech.edt.R;
import com.polytech.edt.io.FileIO;
import com.polytech.edt.model.ADEResource;
import com.polytech.edt.util.LOGGER;

import java.util.ArrayList;
import java.util.List;

public class LoadingActivity extends AppCompatActivity {

    ProgressBar progressBar;

    ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        // Get progress bar and hide it
        progressBar = findViewById(R.id.loading_progress_bar);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    @SuppressLint("StaticFieldLeak")
    protected void onStart() {
        super.onStart();

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

                try {
                    Thread.sleep(1750);
                } catch (InterruptedException e) {
                    LOGGER.warning(e.getMessage());
                }

                try {
                    List<ADEResource> resources;

                    // Read file
                    String resourcesFile = FileIO.read(getApplicationContext(), "resources.json");

                    // Load/Store resources
                    if (resourcesFile != null) {
                        // Decode file
                        resources = mapper.readValue(resourcesFile.getBytes(), new TypeReference<ArrayList<ADEResource>>() {
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
                } catch (Exception e) {
                    LOGGER.error(e);
                    // TODO: Use fatal ?
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                // Stop progress bar
                progressBar.setVisibility(View.GONE);

                // Go to the main activity
                startActivity(new Intent(LoadingActivity.this, CalendarActivity.class));

                // Define a transition
                overridePendingTransition(R.anim.activity_slide_in, R.anim.activity_slide_out);
            }
        }.execute();
    }
}
