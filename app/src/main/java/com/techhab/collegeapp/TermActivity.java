package com.techhab.collegeapp;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.support.v4.view.ViewPager;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;

public class TermActivity  extends ActionBarActivity
        implements NavigationDrawerCallbacks {

    private static String year;

    private final Handler handler = new Handler();

    Toolbar toolbar;
    PagerSlidingTabStrip tabs;
    ViewPager pager;

    private MyPagerAdapter adapter;

    private int currentPosition;

    public TermActivity() {
        super();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term);

        try {
            Intent intent = getIntent();
            year = intent.getStringExtra("year");
        } catch (Exception e) {
            e.printStackTrace();
            year = "2014"; // default academic year
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(year + "-" + (Integer.parseInt(year) + 1));

        adapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        tabs.setViewPager(pager);

        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);

        /*
            check to see if the user is accessing the this activity for the first time
            if so, then ignore.
            otherwise, change the fragment displaying according to the saved state.
         */
        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt("currentFragment");
            // change fragment displaying according to the saved currentPosition
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds rssItemList to the action bar if it is present.

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }


        return super.onOptionsItemSelected(item);
    }




    @Override
    public void onNavigationDrawerItemSelected(int position) {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * MyPagerAdapter
     */
    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = {"Fall", "Winter", "Spring"};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            Bundle args = new Bundle();
            args.putInt(AcademicTermFragment.ARG_OBJECT, position + 1);
            args.putString(AcademicTermFragment.ARG_YEAR, year);
            args.putInt(AcademicTermFragment.ARG_TERM, position);

            Fragment fragment = new AcademicTermFragment();
            fragment.setArguments(args);

            return fragment;
        }
    }
}


