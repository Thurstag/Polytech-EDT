package com.polytech.edt.activities;

import android.os.Bundle;
import android.os.StrictMode;
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

import com.polytech.edt.R;
import com.polytech.edt.fragments.AboutFragment;
import com.polytech.edt.fragments.CalendarFragment;
import com.polytech.edt.fragments.GroupsFragment;
import com.polytech.edt.fragments.NamedFragment;
import com.polytech.edt.model.ADECalendar;
import com.polytech.edt.model.ADEResource;
import com.polytech.edt.util.LOGGER;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalendarActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Fragment fragment;
    ADECalendar calendar;
    Toolbar toolbar;
    boolean hideToolBarMenu;

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

        // Avoid thread restrictions
        // TODO: Remove this in final version
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Fetch calendar
        try {
            String[] items = "366,1081,1634,1685,1745,1779,1805,1872,1985,2116,2127,2204,1677,2157,1690,1698,1770,1807,1937,1949,2067,2081,2137,1924,1935,364,1975,2066,2185,2197,51,59,65,71,119,120,121,122,267,1097,1670,1708,1710,1816,1864,1867,1925,2035,2076,2084,2153,2155,2158,2173,2292,2293,2294,2295,2298,2299,2300,2301,2302,2303,2304,2305,1738,1806,1996,1982,2135,2005,1869,2026,2151,2014,1916,2043,1682,1825,1687,1693,1725,1772,1817,1846,1887,2042,2078,2128,2167,2177,2193,1769,1970,2034,1804,2049,2073,2011,2175,2064,2087".split(",");
            List<ADEResource> resources = new ArrayList<>();

            for (String item : items) {
                resources.add(new ADEResource(Integer.parseInt(item)));
            }

            calendar = new ADECalendar(resources).load();
        } catch (Exception e) {
            LOGGER.fatal(e);
            return;
        }

        // Choose calendar fragment
        try {
            Map<String, Serializable> args = new HashMap<>();
            args.put("calendar", calendar);

            fragment = changeFragment(CalendarFragment.class, args);
        } catch (Exception e) {
            LOGGER.fatal(e);
            return;
        }
    }

    /**
     * Method to change fragment
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
        getSupportActionBar().setTitle(getResources().getString(fragment.name()));

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
        getMenuInflater().inflate(R.menu.calendar_scope, menu);

        // Hide or not
        if (hideToolBarMenu && menu != null) {
            for (int i = 0; i < menu.size(); i++) {
                menu.getItem(i).setVisible(false);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Check fragment
        if (!(fragment instanceof CalendarFragment)) {
            LOGGER.warning("Current fragment is not a CalendarFragment");
        }

        // Switch on item clicked
        switch (item.getItemId()) {

            /* Toolbar menu */
            case R.id.menu_item_day_scope:
                ((CalendarFragment) fragment).changeVisibility(1);
                break;
            case R.id.menu_item_days_scope:
                ((CalendarFragment) fragment).changeVisibility(3);
                break;
            case R.id.menu_item_week_scope:
                ((CalendarFragment) fragment).changeVisibility(7);
                break;
        }

        // TODO: Save in preferences scope

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_calendar:
                if (!(fragment instanceof CalendarFragment)) {
                    // Change fragment
                    try {
                        Map<String, Serializable> args = new HashMap<>();
                        args.put("calendar", calendar);

                        fragment = changeFragment(CalendarFragment.class, args);
                        hideToolBarMenu = false;
                    } catch (Exception e) {
                        LOGGER.error(e);
                    }
                }
                break;

            case R.id.nav_group:
                if (!(fragment instanceof GroupsFragment)) {
                    // Change fragment
                    try {
                        fragment = changeFragment(GroupsFragment.class, new HashMap<String, Serializable>());
                        hideToolBarMenu = true;
                    } catch (Exception e) {
                        LOGGER.error(e);
                    }
                }
                break;

            case R.id.nav_about:
                if (!(fragment instanceof AboutFragment)) {
                    // Change fragment
                    try {
                        fragment = changeFragment(AboutFragment.class, new HashMap<String, Serializable>());
                        hideToolBarMenu = true;
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
