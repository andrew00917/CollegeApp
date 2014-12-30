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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.techhab.eventdialogbuilder.EventDialogBuilder;
import com.techhab.kcollegecustomviews.ProgressBar;


public class EventsActivity extends ActionBarActivity
        implements NavigationDrawerCallbacks {

    private final Handler handler = new Handler();

    private ProgressBar progressBar;
    private int progressBarHeight;

    private Toolbar toolbar;
    private PagerSlidingTabStrip tabs;
    private ViewPager pager;

    private MyPagerAdapter adapter;

    private int currentPosition;

    public EventsActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.main_menu_11));

        adapter = new MyPagerAdapter(getSupportFragmentManager());

        pager.setAdapter(adapter);
        currentPosition = 1;
        pager.setCurrentItem(currentPosition);

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
        getMenuInflater().inflate(R.menu.menu_events, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
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
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.button_pressed);
        view.startAnimation(animation);
    }

    /**
     * Button released method
     *
     * @param view button
     */
    public void buttonReleased(View view) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.button_released);
        view.startAnimation(animation);
    }

    public int getProgressBarHeight() {
        return progressBarHeight;
    }

    public void dismissProgressBar() {
        if (progressBar.getHeight() != 0) {
            progressBarHeight = progressBar.getHeight();
        }
        HeightAnimation animation = new HeightAnimation(progressBar, progressBarHeight, false);
        animation.setDuration(300);
        progressBar.startAnimation(animation);
    }


    /**
     * MyPagerAdapter
     */
    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = {"Etc", "Upcoming Events", "Stress Free Zone"
                , "Tuesdays With...","Wind Down Wednesday", "Trivia Night", "Zoo Flicks"
                , "Zoo After Dark"};

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
                    fragment = new EventsEtcFragment();
                    args.putInt(EventsFragment.ARG_OBJECT, position + 1);
                    fragment.setArguments(args);
                    break;
                case 1:
                    fragment = new EventsFragment();
                    args.putInt(EventsFragment.ARG_OBJECT, position + 1);
                    fragment.setArguments(args);
                    break;
                case 2:
                    fragment = new EventsStressFragment();
                    args.putInt(EventsStressFragment.ARG_OBJECT, position + 1);
                    fragment.setArguments(args);
                    break;
                case 3:
                    fragment = new EventsTuesdayFragment();
                    args.putInt(EventsTuesdayFragment.ARG_OBJECT, position + 1);
                    fragment.setArguments(args);
                    break;
                case 4:
                    fragment = new EventsWednesdayFragment();
                    args.putInt(EventsWednesdayFragment.ARG_OBJECT, position + 1);
                    fragment.setArguments(args);
                    break;
                case 5:
                    fragment = new EventsTriviaFragment();
                    args.putInt(EventsTriviaFragment.ARG_OBJECT, position + 1);
                    fragment.setArguments(args);
                    break;
                case 6:
                    fragment = new EventsFlicksFragment();
                    args.putInt(EventsFlicksFragment.ARG_OBJECT, position + 1);
                    fragment.setArguments(args);
                    break;
                case 7:
                    fragment = new EventsZooDarkFragment();
                    args.putInt(EventsZooDarkFragment.ARG_OBJECT, position + 1);
                    fragment.setArguments(args);
                    break;
                default:
                    fragment = new EventsFragment();
                    args.putInt(EventsFragment.ARG_OBJECT, position + 1);
                    fragment.setArguments(args);
                    break;
            }
            return fragment;
        }
    }

}
