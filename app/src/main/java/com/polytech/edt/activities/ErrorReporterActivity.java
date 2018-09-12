package com.polytech.edt.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.polytech.edt.R;

public class ErrorReporterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_reporter);

        // Define listener for close button
        ImageView close = findViewById(R.id.toolbar_app_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ErrorReporterActivity.this.finish();
            }
        });
    }
}
