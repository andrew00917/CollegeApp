package com.techhab.collegeapp;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
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


import com.techhab.collegeapp.application.CollegeApplication;


import java.util.Calendar;

public class CafeteriaActivity extends ActionBarActivity
        implements NavigationDrawerCallbacks {

    private static final int DATE_PICKER_ID = 999;
    private final Handler handler = new Handler();

    Toolbar toolbar;
    PagerSlidingTabStrip tabs;
    ViewPager pager;

    private MyPagerAdapter adapter;

    private int currentPosition;

    public CafeteriaActivity() {
        super();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cafeteria);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        tabs.setViewPager(pager);

        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds rssItemList to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calendar, menu);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_calendar) {
            showDialog(DATE_PICKER_ID);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:
                Calendar currentCalendar = Calendar.getInstance();
                return new DatePickerDialog(this, pickerListener, currentCalendar.get(Calendar.YEAR), currentCalendar.get(Calendar.MONTH) ,
                        currentCalendar.get(Calendar.DAY_OF_MONTH));
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            //todo: get value here
        }
    };

    @Override
    public void onNavigationDrawerItemSelected(int position) {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putInt("currentColor", currentColor);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
//        currentColor = savedInstanceState.getInt("currentColor");
//        changeColor(currentColor);
    }

    /**
     * MyPagerAdapter
     */
    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = {"Breakfast", "Lunch", "Dinner"};

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
            Fragment fragment;
            Bundle args = new Bundle();
            switch (position) {
                case 0:
                    fragment = new RichardsonMenuFragment();
                    args.putInt(RichardsonMenuFragment.ARG_OBJECT, position + 1);
                    fragment.setArguments(args);
                    break;
                case 1:
                    fragment = new RichardsonMenuLunch();
                    args.putInt(RichardsonMenuLunch.ARG_OBJECT, position + 1);
                    fragment.setArguments(args);
                    break;
                default:
                    fragment = new RichardsonMenuDinner();
                    args.putInt(RichardsonMenuDinner.ARG_OBJECT, position + 1);
                    fragment.setArguments(args);
                    break;
            }
            return fragment;
        }
    }
}
