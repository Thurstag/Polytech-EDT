package com.polytech.edt.activities;

import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.polytech.edt.R;
import com.polytech.edt.exceptions.ExceptionReport;

public class ErrorReporterActivity extends AppCompatActivity {

    Toolbar toolbar;
    ExceptionReport report;

    AlertDialog reportContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_reporter);

        // Set up toolbar
        toolbar = findViewById(R.id.toolbar);
        Drawable icon = getDrawable(R.drawable.ic_warning);
        icon.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        toolbar.setNavigationIcon(icon);
        setSupportActionBar(toolbar);

        // Define listener for close button
        ImageView close = findViewById(R.id.toolbar_app_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ErrorReporterActivity.this.finish();
            }
        });

        // Get report
        report = (ExceptionReport) getIntent().getSerializableExtra("report");

        // Fill interface
        fillTexts();

        // Set up buttons
        setUpButtons();
    }

    private void setUpButtons() {
        // Report content
        findViewById(R.id.button_show_report).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reportContent == null) {
                    reportContent = new AlertDialog.Builder(ErrorReporterActivity.this)
                            .setTitle("Report content") // TODO: string.xml
                            .setMessage(report.report().toString())
                            .setNeutralButton("Close",  // TODO: string.xml
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                }
                else {
                    reportContent.show();
                }
            }
        });

        // TODO: Make send report button
    }

    /**
     * Method to fill texts in UI
     */
    private void fillTexts() {
        TextView textView = findViewById(R.id.error_exception);
        textView.setText(report.exception().getClass().getName());

        textView = findViewById(R.id.error_message);
        textView.setText(report.exception().getMessage());

        if (report.exception().getCause() != null) {
            textView = findViewById(R.id.error_cause);
            textView.setText(report.exception().getCause().getClass().getName());
        }
    }
}
