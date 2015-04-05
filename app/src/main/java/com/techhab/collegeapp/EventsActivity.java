package com.techhab.collegeapp;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.techhab.kcollegecustomviews.ProgressBar;


public class EventsActivity extends ActionBarActivity
        implements NavigationDrawerCallbacks, ObservableScrollViewCallbacks {

    private final Handler handler = new Handler();

    private ProgressBar progressBar;
    private int progressBarHeight;

    private Toolbar toolbar;
    private SlidingTabLayout tabs;
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
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.main_menu_11));
        getSupportActionBar().setShowHideAnimationEnabled(true);

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

//    public int getProgressBarHeight() {
//        return progressBarHeight;
//    }

    public void dismissProgressBar() {
//        if (progressBar.getHeight() != 0) {
//            progressBarHeight = progressBar.getHeight();
//        }
//        HeightAnimation animation = new HeightAnimation(progressBar, progressBarHeight, false);
//        animation.setDuration(300);
//        progressBar.startAnimation(animation);
        progressBar.setVisibility(View.GONE);
    }

//    @Override
//    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
//
//    }
//
//    @Override
//    public void onDownMotionEvent() {
//
//    }
//
//    @Override
//    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
//        if ( ! mScrolled) {
//            // This event can be used only when TouchInterceptionFrameLayout
//            // doesn't handle the consecutive events.
//            adjustToolbar(scrollState);
//        }
//    }
//
//    private TouchInterceptionFrameLayout.TouchInterceptionListener mInterceptionListener =
//            new TouchInterceptionFrameLayout.TouchInterceptionListener() {
//        @Override
//        public boolean shouldInterceptTouchEvent(MotionEvent ev, boolean moving, float diffX, float diffY) {
////            if (!mScrolled && mSlop < Math.abs(diffX) && Math.abs(diffY) < Math.abs(diffX)) {
////                // Horizontal scroll is maybe handled by ViewPager
////                return false;
////            }
////
////            Scrollable scrollable = getCurrentScrollable();
////            if (scrollable == null) {
////                mScrolled = false;
////                return false;
////            }
////
////            // If interceptionLayout can move, it should intercept.
////            // And once it begins to move, horizontal scroll shouldn't work any longer.
////            int toolbarHeight = toolbar.getHeight();
////            int translationY = (int) ViewHelper.getTranslationY(mInterceptionLayout);
////            boolean scrollingUp = 0 < diffY;
////            boolean scrollingDown = diffY < 0;
////            if (scrollingUp) {
////                if (translationY < 0) {
////                    mScrolled = true;
////                    mLastScrollState = ScrollState.UP;
////                    return true;
////                }
////            } else if (scrollingDown) {
////                if (-toolbarHeight < translationY) {
////                    mScrolled = true;
////                    mLastScrollState = ScrollState.DOWN;
////                    return true;
////                }
////            }
//            mScrolled = false;
//            return false;
//        }
//
//        @Override
//        public void onDownMotionEvent(MotionEvent ev) {
//        }
//
//        @Override
//        public void onMoveMotionEvent(MotionEvent ev, float diffX, float diffY) {
////            float translationY = ScrollUtils.getFloat(ViewHelper.getTranslationY(mInterceptionLayout) + diffY,
////                    -toolbar.getHeight(), 0);
////            ViewHelper.setTranslationY(mInterceptionLayout, translationY);
////            if (translationY < 0) {
////                DrawerLayout.LayoutParams lp = (DrawerLayout.LayoutParams) mInterceptionLayout.getLayoutParams();
////                lp.height = (int) (getScreenHeight() - translationY);
////                mInterceptionLayout.requestLayout();
////            }
//        }
//
//        @Override
//        public void onUpOrCancelMotionEvent(MotionEvent ev) {
//            mScrolled = false;
//            adjustToolbar(mLastScrollState);
//        }
//    };
//
//    private Scrollable getCurrentScrollable() {
//        Fragment fragment = getCurrentFragment();
//        if (fragment == null) {
//            return null;
//        }
//        View view = fragment.getView();
//        if (view == null) {
//            return null;
//        }
//        return (Scrollable) view.findViewById(R.id.my_recycler_view);
//    }
//
//    private void adjustToolbar(ScrollState scrollState) {
////        int toolbarHeight = toolbar.getHeight();
////        final Scrollable scrollable = getCurrentScrollable();
////        if (scrollable == null) {
////            return;
////        }
////        int scrollY = scrollable.getCurrentScrollY();
////        if (scrollState == ScrollState.DOWN) {
////            showToolbar();
////        } else if (scrollState == ScrollState.UP) {
////            if (toolbarHeight <= scrollY) {
////                hideToolbar();
////            } else {
////                showToolbar();
////            }
////        } else if (!toolbarIsShown() && !toolbarIsHidden()) {
////            // Toolbar is moving but doesn't know which to move:
////            // you can change this to hideToolbar()
////            showToolbar();
////        }
//    }
//
//    private Fragment getCurrentFragment() {
//        return adapter.getItemAt(pager.getCurrentItem());
//    }
//
//    private boolean toolbarIsShown() {
//        return ViewHelper.getTranslationY(mInterceptionLayout) == 0;
//    }
//
//    private boolean toolbarIsHidden() {
//        return ViewHelper.getTranslationY(mInterceptionLayout) == -toolbar.getHeight();
//    }
//
//    private void showToolbar() {
//        animateToolbar(0);
//    }
//
//    private void hideToolbar() {
//        animateToolbar(-toolbar.getHeight());
//    }
//
//    private void animateToolbar(final float toY) {
////        float layoutTranslationY = ViewHelper.getTranslationY(mInterceptionLayout);
////        if (layoutTranslationY != toY) {
////            ValueAnimator animator = ValueAnimator.ofFloat(ViewHelper.getTranslationY(mInterceptionLayout), toY).setDuration(200);
////            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
////                @Override
////                public void onAnimationUpdate(ValueAnimator animation) {
////                    float translationY = (float) animation.getAnimatedValue();
////                    ViewHelper.setTranslationY(mInterceptionLayout, translationY);
////                    if (translationY < 0) {
////                        DrawerLayout.LayoutParams lp = (DrawerLayout.LayoutParams) mInterceptionLayout.getLayoutParams();
////                        lp.height = (int) (getScreenHeight() - translationY);
////                        mInterceptionLayout.requestLayout();
////                    }
////                }
////            });
////            animator.start();
////        }
//    }

    @Override
    public void onScrollChanged(int i, boolean b, boolean b2) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        ActionBar actionBar = getSupportActionBar();
        if (scrollState == ScrollState.UP) {
            if (actionBar.isShowing()) {
                actionBar.hide();
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (!actionBar.isShowing()) {
                actionBar.show();
            }
        }
    }

    /**
     * MyPagerAdapter
     */
    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = {"Etc", "Upcoming Events", "Stress Free Zone"
                , "Tuesdays With...", "Wind Down Wednesday", "Trivia Night", "Zoo Flicks"
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
            return EventsFragment.createNewInstance(position);
        }
    }

}
