/*
 * Copyright 2018-2022 Polytech-EDT
 * Licensed under Apache License 2.0 or any later version
 * Refer to the LICENSE.md file included.
 */
package com.polytech.edt.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.polytech.edt.AppCache;
import com.polytech.edt.R;
import com.polytech.edt.config.UserConfig;
import com.polytech.edt.fragments.AboutFragment;
import com.polytech.edt.fragments.CalendarFragment;
import com.polytech.edt.fragments.GroupsFragment;
import com.polytech.edt.fragments.NamedFragment;
import com.polytech.edt.fragments.SettingsFragment;
import com.polytech.edt.task.ReloadCalendar;
import com.polytech.edt.util.LOGGER;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CalendarActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Fragment fragment;
    Toolbar toolbar;
    boolean hideToolBarMenus;
    ImageView reload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        // Set up toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up navigation Drawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Set up navigation view
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Define listener on reload icon click
        reload = findViewById(R.id.calendar_reload);
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog dialog = new ProgressDialog(CalendarActivity.this);

                // Define properties
                dialog.setMessage(getResources().getString(R.string.loading));
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);

                // Show
                dialog.show();

                // Reload calendar
                ReloadCalendar task = new ReloadCalendar(new ArrayList<>(((UserConfig) AppCache.get("config")).groups()));
                task.setCallback(new Runnable() {
                    @Override
                    public void run() {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    fragment = changeFragment(CalendarFragment.class, new HashMap<String, Serializable>());
                                } catch (Exception e) {
                                    LOGGER.fatal(e);
                                } finally {
                                    dialog.cancel();
                                }
                            }
                        });
                    }
                });
            }
        });

        // Choose calendar fragment
        try {
            fragment = changeFragment(CalendarFragment.class, new HashMap<String, Serializable>());
        } catch (Exception e) {
            LOGGER.fatal(e);
            return;
        }
    }

    /**
     * Change fragment
     *
     * @param _class     Fragment class
     * @param parameters Parameters
     * @return Fragment
     */
    private Fragment changeFragment(Class<? extends NamedFragment> _class, Map<String, Serializable> parameters) throws InstantiationException, IllegalAccessException {
        NamedFragment fragment = _class.newInstance();
        Bundle bundle = new Bundle();

        // Add arguments
        for (String key : parameters.keySet()) {
            bundle.putSerializable(key, parameters.get(key));
        }
        fragment.setArguments(bundle);

        // Change
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.calendar_fragment_container, fragment);
        ft.commit();

        // Change toolbar's name
        if (getSupportActionBar() == null) {
            LOGGER.warning("There is no action bar");
        }
        else {
            getSupportActionBar().setTitle(getResources().getString(fragment.name()));
        }

        return fragment;
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
        // Show menu
        getMenuInflater().inflate(R.menu.calendar_scope, menu);
        reload.setVisibility(View.VISIBLE);

        // Hide menu
        if (hideToolBarMenus) {
            for (int i = 0; i < menu.size(); i++) {
                menu.getItem(i).setVisible(false);
            }
            reload.setVisibility(View.INVISIBLE);
        }
        return true;
    }

    @Override
    @SuppressLint("StaticFieldLeak")
    public boolean onOptionsItemSelected(MenuItem item) {
        // Check fragment
        if (!(fragment instanceof CalendarFragment)) {
            LOGGER.warning("Current fragment is not a CalendarFragment");
            return super.onOptionsItemSelected(item);
        }

        // Switch on item clicked
        int scope;
        switch (item.getItemId()) {

            /* Toolbar menu */
            case R.id.menu_item_day_scope:
                scope = 1;
                break;
            case R.id.menu_item_days_scope:
                scope = 3;
                break;
            case R.id.menu_item_week_scope:
                scope = 7;
                break;

            default:
                LOGGER.warning("Unknown item: " + item.getItemId());
                return super.onOptionsItemSelected(item);
        }

        // Change scope
        ((CalendarFragment) fragment).changeVisibility(scope);

        // Update config
        final int finalScope = scope;
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    ((UserConfig) AppCache.get("config")).setCalendarViewScope(finalScope);
                } catch (JsonProcessingException e) {
                    LOGGER.error(e);
                }
                return null;
            }
        }.execute();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_calendar:
                if (!(fragment instanceof CalendarFragment)) {
                    try {
                        fragment = changeFragment(CalendarFragment.class, new HashMap<String, Serializable>());
                        hideToolBarMenus = false;
                    } catch (Exception e) {
                        LOGGER.error(e);
                    }
                }
                break;

            case R.id.nav_group:
                if (!(fragment instanceof GroupsFragment)) {
                    try {
                        fragment = changeFragment(GroupsFragment.class, new HashMap<String, Serializable>());
                        hideToolBarMenus = true;
                    } catch (Exception e) {
                        LOGGER.error(e);
                    }
                }
                break;

            case R.id.nav_about:
                if (!(fragment instanceof AboutFragment)) {
                    try {
                        fragment = changeFragment(AboutFragment.class, new HashMap<String, Serializable>());
                        hideToolBarMenus = true;
                    } catch (Exception e) {
                        LOGGER.error(e);
                    }
                }
                break;

            case R.id.nav_settings:
                if (!(fragment instanceof SettingsFragment)) {
                    try {
                        fragment = changeFragment(SettingsFragment.class, new HashMap<String, Serializable>());
                        hideToolBarMenus = true;
                    } catch (Exception e) {
                        LOGGER.error(e);
                    }
                }
                break;
        }

        // Update menu status
        invalidateOptionsMenu();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
