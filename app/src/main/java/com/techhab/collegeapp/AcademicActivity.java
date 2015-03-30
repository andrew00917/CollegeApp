package com.techhab.collegeapp;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.Arrays;
import java.util.List;


public class AcademicActivity extends ActionBarActivity implements NavigationDrawerCallbacks {

    private final Handler handler = new Handler();

    Toolbar toolbar;
    PagerSlidingTabStrip tabs;
    ViewPager pager;

    private MyPagerAdapter adapter;

    private int currentPosition;
    private Spinner toolbarSpinner;

    public AcademicActivity() {
        super();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academic);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbarSpinner = (Spinner) findViewById(R.id.day_spinner);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.main_menu_00));

        adapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        tabs.setViewPager(pager);

        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //if position is in Search Course tab
                if(position == 0){
                    toolbarSpinner.setVisibility(View.GONE);
                }else{
                    toolbarSpinner.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        List<String> days = Arrays.asList("2014-15", "2015-16", "2016-17") ;
        ArrayAdapter baseSpinnerAdapter = new ArrayAdapter<>(this, R.layout.day_spinner_item, days);
        baseSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        toolbarSpinner.setAdapter(baseSpinnerAdapter);

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
//                NavUtils.navigateUpFromSameTask(this);
                onBackPressed();
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
        outState.putInt("currentFragment", currentPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentPosition = savedInstanceState.getInt("currentFragment");
        // change fragment displaying according to the saved currentPosition
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * Button pressed method
     *
     * @param view button
     */
    public void buttonPressed(View view) {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_pressed);
        view.startAnimation(animation);
    }

    /**
     * Button released method
     *
     * @param view button
     */
    public void buttonReleased(View view) {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_released);
        view.startAnimation(animation);
    }

    /**
     * MyPagerAdapter
     */
     class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = {"Search Courses", "Calendar"};

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
                    fragment = new CourseSearchFragment();
                    args.putInt(CourseSearchFragment.ARG_OBJECT, position + 1);
                    fragment.setArguments(args);
                    break;
                case 1:
                    fragment = new CalendarFragment();
                    args.putInt(CalendarFragment.ARG_OBJECT, position + 1);
                    fragment.setArguments(args);
                    break;
                default:
                    fragment = new CourseSearchFragment();
                    args.putInt(CourseSearchFragment.ARG_OBJECT, position + 1);
                    fragment.setArguments(args);
                    break;
            }
            return fragment;
        }
    }
}
