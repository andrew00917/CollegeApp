package com.techhab.collegeapp;

import android.app.Application;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.techhab.collegeapp.application.CollegeApplication;

/**
 * Created by akhavantafti on 4/7/2015.
 */
public abstract class BaseObservableRecyclerActivity extends ActionBarActivity implements ObservableScrollViewCallbacks, NavigationDrawerCallbacks {
    public static final String ARG_SCROLL_Y = "ARG_SCROLL_Y";

    protected Application application;
    protected Toolbar toolbar;
    protected FrameLayout toolbarAndGenderSwitch;
    protected LinearLayout header;
    protected SlidingTabLayout mPagerSlidingTabStrip;
    protected ViewPager mViewPager;
    private FragmentPagerAdapter mPagerAdapter;
    private NavigationDrawerFragment mNavigationDrawerFragment;

    private DrawerLayout mDrawerLayout;
    private int currentPosition;
    private int mBaseTranslationY;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        application = (CollegeApplication) getApplication();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarAndGenderSwitch = (FrameLayout) findViewById(R.id.toolbar_and_gender_switch);
        header = (LinearLayout) findViewById(R.id.header);
        mPagerSlidingTabStrip = (SlidingTabLayout) findViewById(R.id.tabs);
        ViewCompat.setElevation(header, getResources().getDimension(R.dimen.toolbar_elevation));
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getToolbarTitle());

        mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.kzooOrange));

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager(), getTabTitles());
        mViewPager.setAdapter(mPagerAdapter);
        mPagerSlidingTabStrip.setViewPager(mViewPager);
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4,
                getResources().getDisplayMetrics());
        mViewPager.setPageMargin(pageMargin);


        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.home_navigation_drawer);

        /*
            check to see if the user is accessing the this activity for the first time
            if so, then ignore.
            otherwise, change the fragment displaying according to the saved state.
         */
        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt("currentFragment");
            // change fragment displaying according to the saved currentPosition
            mViewPager.setCurrentItem(currentPosition);
        }
    }

    protected abstract int getLayoutResourceId();

    protected abstract String getToolbarTitle();

    protected abstract String[] getTabTitles();

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
                return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen()) {
            mDrawerLayout.closeDrawer(Gravity.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        if (dragging) {
            int toolbarHeight = toolbarAndGenderSwitch.getHeight();
            float currentHeaderTranslationY = ViewHelper.getTranslationY(header);
            if (firstScroll) {
                if (-toolbarHeight < currentHeaderTranslationY) {
                    mBaseTranslationY = scrollY;
                }
            }
            int headerTranslationY = Math.min(0, Math.max(-toolbarHeight,
                    -(scrollY - mBaseTranslationY)));
            ViewPropertyAnimator.animate(header).cancel();
            ViewHelper.setTranslationY(header, headerTranslationY);
            ViewHelper.setTranslationY(mViewPager, headerTranslationY);
        }
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        mBaseTranslationY = 0;

        Fragment fragment = getCurrentFragment();
        if (fragment == null) {
            return;
        }
        View view = fragment.getView();
        if (view == null) {
            return;
        }

        int toolbarHeight = toolbarAndGenderSwitch.getHeight();
        final ObservableScrollCardRecylerView scrollView = (ObservableScrollCardRecylerView) view.findViewById(
                R.id.my_recycler_view);
        if (scrollView == null) {
            return;
        }
        int scrollY = scrollView.getCurrentScrollY();
        if (scrollState == ScrollState.DOWN) {
            showToolbar(scrollView);
        } else if (scrollState == ScrollState.UP) {
            if (toolbarHeight <= scrollY) {
                hideToolbar(scrollView);
            } else {
                showToolbar(scrollView);
            }
        } else {
            // Even if onScrollChanged occurs without scrollY changing, toolbar should be adjusted
            if (toolbarIsShown() || toolbarIsHidden()) {
                // Toolbar is completely moved, so just keep its state
                // and propagate it to other pages
              //  propagateToolbarState(scrollView, toolbarIsShown());
            } else {
                // Toolbar is moving but doesn't know which to move:
                // you can change this to hideToolbar()
                showToolbar(scrollView);
            }
        }
    }


    private Fragment getCurrentFragment() {
        return mPagerAdapter.getItemAt(mViewPager.getCurrentItem());
    }

    private void propagateToolbarState(ObservableScrollCardRecylerView scrollView, boolean isShown) {
        int toolbarHeight = toolbarAndGenderSwitch.getHeight();

        // Set scrollY for the fragments that are not created yet
        mPagerAdapter.setScrollY(isShown ? 0 : toolbarHeight);

        // Set scrollY for the active fragments
        for (int i = 0; i < mPagerAdapter.getCount(); i++) {
            // Skip current item
            if (i == mViewPager.getCurrentItem()) {
                continue;
            }

            // Skip destroyed or not created item
            Fragment f = mPagerAdapter.getItemAt(i);
            if (f == null) {
                continue;
            }

            if (scrollView != null) {
                if (isShown) {
                    // Scroll up
                    if (0 < scrollView.getCurrentScrollY()) {
                        ViewPropertyAnimator.animate(mViewPager).translationY(0).setDuration(0).start();
                    }
                } else {
                    // Scroll down (to hide padding)
                    ViewPropertyAnimator.animate(mViewPager).translationY(-toolbarHeight).setDuration(0).start();
                }
            }
        }
    }

    private boolean toolbarIsShown() {
        return ViewHelper.getTranslationY(header) == 0;
    }

    private boolean toolbarIsHidden() {
        return ViewHelper.getTranslationY(header) == -toolbarAndGenderSwitch.getHeight();
    }

    private void showToolbar(ObservableScrollCardRecylerView scrollView) {
        float headerTranslationY = ViewHelper.getTranslationY(header);
        if (headerTranslationY != 0) {
            ViewPropertyAnimator.animate(header).cancel();
            ViewPropertyAnimator.animate(header).translationY(0).setDuration(200).start();
            ViewPropertyAnimator.animate(mViewPager).translationY(0).setDuration(200).start();
        }
       // propagateToolbarState(scrollView, true);
    }

    private void hideToolbar(ObservableScrollCardRecylerView scrollView) {
        float headerTranslationY = ViewHelper.getTranslationY(header);
        final int toolbarHeight = toolbarAndGenderSwitch.getHeight();
        if (headerTranslationY != -toolbarHeight) {
            ViewPropertyAnimator.animate(header).cancel();
            ViewPropertyAnimator.animate(header).translationY(-toolbarHeight).setDuration(200).start();
            ViewPropertyAnimator.animate(mViewPager).translationY(-toolbarHeight).setDuration(200).start();
        }
      //  propagateToolbarState(scrollView, false);
    }

    public class FragmentPagerAdapter extends FragmentStatePagerAdapter {
        private String[] titles;

        private SparseArray<Fragment> mPages;
        private int mScrollY;

        public FragmentPagerAdapter(FragmentManager fm, String[] titles) {
            super(fm);
            mPages = new SparseArray<>();
            this.titles = titles;
        }

        public void setScrollY(int scrollY) {
            mScrollY = scrollY;
        }

        public void updateTitlesArray(String[] newTitleArray) {
            this.titles = newTitleArray;
            this.notifyDataSetChanged();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        public Fragment getItemAt(int position) {
            return mPages.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (0 <= mPages.indexOfKey(position)) {
                mPages.remove(position);
            }
            super.destroyItem(container, position, object);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = CampusBuildingsFragment.createNewInstance(position);

            //put new argument for scroll view
            Bundle args = fragment.getArguments();
            if (0 <= mScrollY) {
                args.putInt(ARG_SCROLL_Y, mScrollY);
            }
            fragment.setArguments(args);
            mPages.put(position, fragment);
            return fragment;
        }
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
}
