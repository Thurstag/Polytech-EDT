package com.polytech.edt;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.polytech.edt.calendar.ADEDateTimeInterpreter;
import com.polytech.edt.calendar.ADEEventClickListener;
import com.polytech.edt.calendar.ADEMonthChangeListener;
import com.polytech.edt.model.ADECalendar;
import com.polytech.edt.model.ADEResource;
import com.polytech.edt.model.ADEWeekView;

import java.util.Collections;

public class Calendar extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ADEWeekView weekView;
    private ADECalendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up navigation Drawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Get a reference for the week view in the layout.
        weekView = findViewById(R.id.weekView);

        // Avoid thread restrictions
        // TODO: Remove this in final version
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Fetch calendar
        try {
            calendar = new ADECalendar(Collections.singletonList(new ADEResource(2128))).load();
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            // TODO: Exit app ?
            return;
        }

        // Implement month listener
        weekView.setMonthChangeListener(new ADEMonthChangeListener(calendar));

        // Change DateTimeInterpreter
        weekView.setDateTimeInterpreter(new ADEDateTimeInterpreter(weekView));

        // Implement click callback
        weekView.setOnEventClickListener(new ADEEventClickListener(this));

        // Go to the first event
        goToFirstEvent();
    }

    /**
     * Method to go to the first event
     */
    private void goToFirstEvent() {
        if (calendar == null) {
            // TODO: Add a warning
            return;
        }
        weekView.goToDate(calendar.events().get(0).getStartTime());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.calendar_scope, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            /* Toolbar menu */
            case R.id.menu_item_day_scope:
                weekView.setNumberOfVisibleDays(1);
                goToFirstEvent();
                break;
            case R.id.menu_item_days_scope:
                weekView.setNumberOfVisibleDays(3);
                goToFirstEvent();
                break;
            case R.id.menu_item_week_scope:
                weekView.setNumberOfVisibleDays(7);
                goToFirstEvent();
                break;
        }

        // TODO: Save in preferences scope

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
