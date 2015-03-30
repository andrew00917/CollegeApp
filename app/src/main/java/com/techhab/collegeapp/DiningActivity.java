package com.techhab.collegeapp;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class DiningActivity extends ActionBarActivity
            implements NavigationDrawerCallbacks {

    private final Handler handler = new Handler();

    private Toolbar toolbar;
    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private FrameLayout header;
    private Spinner daySpinner;

    private MyPagerAdapter adapter;

    private Calendar mCalender;
    private int dayNum;

    private int currentPosition;

    public DiningActivity() {
        super();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dining);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        header = (FrameLayout) findViewById(R.id.header);
        daySpinner = (Spinner) findViewById(R.id.day_spinner);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Dining");
        mDrawerLayout.setStatusBarBackgroundColor(getResources()
                .getColor(R.color.kzooOrange));

//        ViewCompat.setElevation(header, getResources().getDimension(R.dimen.toolbar_elevation));

        mCalender = Calendar.getInstance();
        dayNum = mCalender.get(Calendar.DAY_OF_WEEK);
        initializeDaySpinner();

        adapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        tabs.setViewPager(pager);

        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);
    }

    private void initializeDaySpinner() {
        ArrayList<String> days = new ArrayList<>();
        // populate the days arraylist with 4 entries. The first two will always be
        // "Today" and "Tomorrow", but the last 2 entries must be programmatically determined
        // using the current date.
        days.add("Today");
        days.add("Tomorrow");
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
        mCalender.add(Calendar.DATE, 2);
        String thirdDay = dayFormat.format(mCalender.getTime());
        mCalender.add(Calendar.DATE, 1);
        String fourthDay = dayFormat.format(mCalender.getTime());
        days.add(thirdDay);
        days.add(fourthDay);

//        CustomSpinnerAdapter spinnerAdapter = new CustomSpinnerAdapter(this,
//                R.layout.day_spinner_item, days);
//        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

//        daySpinner.setAdapter(spinnerAdapter);
        ArrayAdapter baseSpinnerAdapter = new ArrayAdapter<>(this, R.layout.day_spinner_item, days);
        baseSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        daySpinner.setAdapter(baseSpinnerAdapter);

        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Empty the meals list to start anew.
//                meals.clear();
                // Set up the meals again, but this time pass in the day num depending on the
                // position. Sunday is 1.
                dayNum = dayNum + position;
                if ( dayNum >= 8 ) { // Rollover
                    dayNum = dayNum - 7;
                }
//                setUpMeals(selectedDayNum);

//                CafeteriaFragment.createNewInstance();

                adapter.notifyDataSetChanged();

//                mealsRecyclerView.getAdapter().notifyDataSetChanged();
//                checkIfNoMeals(meals);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        /*allMealsAreOver = true;
        for (Meal meal : meals) {
            if ( ! meal.isPast() ) { // If any of the meals aren't past, set it to "Today"
                allMealsAreOver = false;
            }
        }
        // If all meals are over, set the spinner to tomorrow.
        if ( allMealsAreOver ) {
            daySpinner.setSelection(1);
        } else {
            daySpinner.setSelection(0);
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds rssItemList to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_action_with_search, menu);
        //create search interface
        SearchableCreator.makeSearchable(this, menu);
        return true;
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
                onBackPressed();
//                    NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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
    public class MyPagerAdapter extends FragmentStatePagerAdapter {

        private final String[] TITLES = {"Cafeteria", "Stacks", "Book Club"};

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
                    Log.d("MyPagerAdapter", "I'm refreshing! dayNum = " + dayNum);
                    fragment = CafeteriaFragment.createNewInstance(dayNum);
//                    args.putInt(CafeteriaFragment.ARG_OBJECT, position + 1);

                    fragment.setArguments(args);
                    break;
                case 1:
                    fragment = new RichardsonFragment();
                    args.putInt(RichardsonFragment.ARG_OBJECT, position + 1);
                    fragment.setArguments(args);
                    break;
                default:
                    fragment = new BookClubFragment();
                    args.putInt(BookClubFragment.ARG_OBJECT, position + 1);
                    fragment.setArguments(args);
                    break;
            }
            return fragment;
        }
    }

    // setting up a custom adapter for spinner
    private class CustomSpinnerAdapter extends ArrayAdapter<String> {

        private ArrayList<String> days;
        private LayoutInflater inflater;
        private Context context;

        public CustomSpinnerAdapter(Context context, int rowResourceLayout, ArrayList<String> days) {
            super(context, rowResourceLayout, days);
            this.context = context;
            this.days = days;

            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        // This funtion called for each row ( Called data.size() times )
        public View getCustomView(int position, View convertView, ViewGroup parent) {

            /********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
            View row = inflater.inflate(R.layout.cafeteria_spinner_rows, parent, false);

            TextView label = (TextView)row.findViewById(R.id.day);

            label.setText(days.get(position));

            return row;
        }

    }
}
