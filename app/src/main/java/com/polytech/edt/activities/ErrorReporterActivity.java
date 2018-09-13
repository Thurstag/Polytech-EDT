package com.polytech.edt.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.drawable.VectorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.polytech.edt.R;
import com.polytech.edt.exceptions.ExceptionReport;
import com.polytech.edt.util.ColorUtil;
import com.polytech.edt.util.FileIO;
import com.polytech.edt.util.LOGGER;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ErrorReporterActivity extends AppCompatActivity {

    private static int reportMaxCount = 10;

    Toolbar toolbar;
    ExceptionReport report;

    AlertDialog reportContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_reporter);

        // Set up toolbar
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(ColorUtil.coloredSVG((VectorDrawable) Objects.requireNonNull(getDrawable(R.drawable.ic_warning)), getResources().getColor(R.color.white)));
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

        // Save report
        saveReport();
    }

    /**
     * Method to save report
     */
    @SuppressLint("StaticFieldLeak")
    private void saveReport() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss", Locale.FRANCE);
                try {
                    if (!FileIO.dirExists(FileIO.path + "files/reports")) {
                        FileIO.mkdir(FileIO.path + "files/reports");
                    }

                    Collection<File> reports = FileIO.files(FileIO.path + "files/reports/");

                    // Delete first report
                    if (reports.size() >= reportMaxCount) {
                        reports.toArray(new File[]{})[0].delete();
                    }

                    // Save report
                    FileIO.write(FileIO.path + "files/reports/report$" + format.format(new Date()), report.report().toString());
                } catch (Exception e) {
                    LOGGER.error(e);
                }
                return null;
            }
        }.execute();
    }

    private void setUpButtons() {
        // Report content
        findViewById(R.id.button_show_report).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reportContent == null) {
                    reportContent = new AlertDialog.Builder(ErrorReporterActivity.this)
                            .setTitle(getResources().getString(R.string.report_content))
                            .setMessage(report.report().toString())
                            .setNeutralButton(getResources().getString(R.string.close),
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
