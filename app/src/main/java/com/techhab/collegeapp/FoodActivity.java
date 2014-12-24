package com.techhab.collegeapp;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;

public class FoodActivity extends ActionBarActivity
            implements NavigationDrawerCallbacks {

        private final Handler handler = new Handler();

        Toolbar toolbar;
        PagerSlidingTabStrip tabs;
        ViewPager pager;

        private MyPagerAdapter adapter;

        private int currentPosition;

        public FoodActivity() {
            super();
        }

        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_food);

            toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds rssItemList to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_food, menu);
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

            private final String[] TITLES = {"Cafeteria", "Richardson", "BookClub"};

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
                        fragment = new DetailCafeteriaFragment();
                        args.putInt(DetailCafeteriaFragment.ARG_OBJECT, position + 1);
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
    }
