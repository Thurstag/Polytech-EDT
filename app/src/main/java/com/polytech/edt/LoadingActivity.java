package com.polytech.edt;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.polytech.edt.model.ADEResource;

import java.util.ArrayList;
import java.util.List;

public class LoadingActivity extends AppCompatActivity {

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        progressBar = findViewById(R.id.loading_progress_bar);
    }

    @Override
    @SuppressLint("StaticFieldLeak")
    protected void onStart() {
        super.onStart();

        // Start logo animation
        ((AnimatedVectorDrawable) ((ImageView) findViewById(R.id.loading_logo)).getDrawable()).start();

        // Download resources
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    List<ADEResource> resources;

                    // Load/Store resources
                    if (false) {
                        // TODO: From a file (JSON)
                        resources = new ArrayList<>();
                    }
                    else {
                        resources = ADEResource.resources();

                        // TODO: Save in file
                    }

                    // TODO: Store resources

                    // Load calendar
                    if (false) {
                        // TODO: Load calendar and store it
                    }
                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage(), e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                // TODO: Create an activity for calendar

                // Stop progress bar
                progressBar.setVisibility(View.GONE);

                // Go to the main activity
                startActivity(new Intent(LoadingActivity.this, DevActivity.class));

                overridePendingTransition(R.anim.activity_slide_in, R.anim.activity_slide_out);
            }
        }.execute();
    }
}
