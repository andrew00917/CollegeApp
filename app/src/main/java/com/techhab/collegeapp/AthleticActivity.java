package com.techhab.collegeapp;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;


public class AthleticActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private final Handler handler = new Handler();

    Toolbar toolbar;
    PagerSlidingTabStrip tabs;
    ViewPager pager;
    Switch boyGirlSwitch;

    private MyPagerAdapter adapter;

    private int currentPosition;

    private String[] boysSports = {"Baseball", "Basketball", "Football", "Swim/Dive", "Tennis"};
    private String[] girlsSports = {"Basketball", "Softball", "Swim/Dive", "Tennis"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_athletic);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        adapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        tabs.setViewPager(pager);

        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);

        // Handle Boys/Girls toggle switch
        boyGirlSwitch = (Switch) findViewById(R.id.boys_girls_switch);
        boyGirlSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    adapter.TITLES = boysSports;
//                    finish();
//                    startActivity(getIntent());
                } else {
                    adapter.TITLES = girlsSports;
//                    finish();
//                    startActivity(getIntent());
                }
            }
        });
    }

    private void initViews(){

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        setSupportActionBar(toolbar);

        // Disable app name in toolbar
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,toolbar ,  R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                syncState();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                syncState();
            }
        };

        mDrawerToggle.syncState();

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_athletic, menu);
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
                NavUtils.navigateUpFromSameTask(this);
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

    /**
     * MyPagerAdapter
     */
    public class MyPagerAdapter extends FragmentPagerAdapter {

        private String[] TITLES = {"Home Games", "Baseball", "Basketball", "Football", "Swim/Dive", "Tennis"};

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
                    fragment = new HomeGamesFragment();
                    args.putInt(HomeGamesFragment.ARG_OBJECT, position + 1);
                    fragment.setArguments(args);
                    break;
                case 1:
                    fragment = new CalendarFragment();
                    args.putInt(CalendarFragment.ARG_OBJECT, position + 1);
                    fragment.setArguments(args);
                    break;
                case 2:
                    fragment = new BasketballFragment();
                    args.putInt(BasketballFragment.ARG_OBJECT, position + 1);
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
