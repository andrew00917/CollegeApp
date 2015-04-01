package com.techhab.collegeapp;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.nineoldandroids.view.ViewHelper;
import com.techhab.kcollegecustomviews.ProgressBar;

public class EventsActivity extends BaseActivity
        implements NavigationDrawerCallbacks, ObservableScrollViewCallbacks {

    private final Handler handler = new Handler();

    private ProgressBar progressBar;

    private Toolbar toolbar;
    private SlidingTabLayout tabs;
    private ViewPager pager;

    private MyPagerAdapter adapter;

    private int currentPosition;

    private LinearLayout header;
    private FrameLayout wrapper;
    private TouchInterceptionFrameLayout mInterceptionLayout;
    private boolean mScrolled;
    private int mSlop;
    private ScrollState mLastScrollState;

    public EventsActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.main_menu_11));

        ViewCompat.setElevation(findViewById(R.id.header), getResources().getDimension(R.dimen.toolbar_elevation));

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        pager = (ViewPager) findViewById(R.id.pager);

        adapter = new MyPagerAdapter(getSupportFragmentManager());

        pager.setAdapter(adapter);

        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);

        // Padding for ViewPager must be set outside the ViewPager itself
        // because with padding, EdgeEffect of ViewPager become strange.
        final int tabHeight = getResources().getDimensionPixelSize(R.dimen.tab_height);
        findViewById(R.id.pager_wrapper).setPadding(0, getActionBarSize() + tabHeight, 0, 0);

        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setCustomTabView(R.layout.tab, android.R.id.text1);
        tabs.setSelectedIndicatorColors(getResources().getColor(R.color.accent_material_light));
        tabs.setDistributeEvenly(true);
        tabs.setViewPager(pager);

        currentPosition = 1;
        pager.setCurrentItem(currentPosition);

        ViewConfiguration vc = ViewConfiguration.get(this);
        mSlop = vc.getScaledTouchSlop();
        header = (LinearLayout) findViewById(R.id.header);
        wrapper = (FrameLayout) findViewById(R.id.pager_wrapper);
        mInterceptionLayout = (TouchInterceptionFrameLayout) findViewById(R.id.container);
        mInterceptionLayout.setScrollInterceptionListener(mInterceptionListener);

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

    public void dismissProgressBar() {
//        if (progressBar.getHeight() != 0) {
//            progressBarHeight = progressBar.getHeight();
//        }
//        HeightAnimation animation = new HeightAnimation(progressBar, progressBarHeight, false);
//        animation.setDuration(300);
//        progressBar.startAnimation(animation);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        if ( ! mScrolled) {
            // This event can be used only when TouchInterceptionFrameLayout
            // doesn't handle the consecutive events.
            adjustToolbar(scrollState);
        }
    }

    private TouchInterceptionFrameLayout.TouchInterceptionListener mInterceptionListener =
            new TouchInterceptionFrameLayout.TouchInterceptionListener() {
        @Override
        public boolean shouldInterceptTouchEvent(MotionEvent ev, boolean moving, float diffX, float diffY) {
//            if (!mScrolled && mSlop < Math.abs(diffX) && Math.abs(diffY) < Math.abs(diffX)) {
//                // Horizontal scroll is maybe handled by ViewPager
//                return false;
//            }
//
//            Scrollable scrollable = getCurrentScrollable();
//            if (scrollable == null) {
//                mScrolled = false;
//                return false;
//            }
//
//            // If interceptionLayout can move, it should intercept.
//            // And once it begins to move, horizontal scroll shouldn't work any longer.
//            int toolbarHeight = toolbar.getHeight();
//            int translationY = (int) ViewHelper.getTranslationY(mInterceptionLayout);
//            boolean scrollingUp = 0 < diffY;
//            boolean scrollingDown = diffY < 0;
//            if (scrollingUp) {
//                if (translationY < 0) {
//                    mScrolled = true;
//                    mLastScrollState = ScrollState.UP;
//                    return true;
//                }
//            } else if (scrollingDown) {
//                if (-toolbarHeight < translationY) {
//                    mScrolled = true;
//                    mLastScrollState = ScrollState.DOWN;
//                    return true;
//                }
//            }
            mScrolled = false;
            return false;
        }

        @Override
        public void onDownMotionEvent(MotionEvent ev) {
        }

        @Override
        public void onMoveMotionEvent(MotionEvent ev, float diffX, float diffY) {
//            float translationY = ScrollUtils.getFloat(ViewHelper.getTranslationY(mInterceptionLayout) + diffY,
//                    -toolbar.getHeight(), 0);
//            ViewHelper.setTranslationY(mInterceptionLayout, translationY);
//            if (translationY < 0) {
//                DrawerLayout.LayoutParams lp = (DrawerLayout.LayoutParams) mInterceptionLayout.getLayoutParams();
//                lp.height = (int) (getScreenHeight() - translationY);
//                mInterceptionLayout.requestLayout();
//            }
        }

        @Override
        public void onUpOrCancelMotionEvent(MotionEvent ev) {
            mScrolled = false;
            adjustToolbar(mLastScrollState);
        }
    };

    private Scrollable getCurrentScrollable() {
        Fragment fragment = getCurrentFragment();
        if (fragment == null) {
            return null;
        }
        View view = fragment.getView();
        if (view == null) {
            return null;
        }
        return (Scrollable) view.findViewById(R.id.my_recycler_view);
    }

    private void adjustToolbar(ScrollState scrollState) {
//        int toolbarHeight = toolbar.getHeight();
//        final Scrollable scrollable = getCurrentScrollable();
//        if (scrollable == null) {
//            return;
//        }
//        int scrollY = scrollable.getCurrentScrollY();
//        if (scrollState == ScrollState.DOWN) {
//            showToolbar();
//        } else if (scrollState == ScrollState.UP) {
//            if (toolbarHeight <= scrollY) {
//                hideToolbar();
//            } else {
//                showToolbar();
//            }
//        } else if (!toolbarIsShown() && !toolbarIsHidden()) {
//            // Toolbar is moving but doesn't know which to move:
//            // you can change this to hideToolbar()
//            showToolbar();
//        }
    }

    private Fragment getCurrentFragment() {
        return adapter.getItemAt(pager.getCurrentItem());
    }

    private boolean toolbarIsShown() {
        return ViewHelper.getTranslationY(mInterceptionLayout) == 0;
    }

    private boolean toolbarIsHidden() {
        return ViewHelper.getTranslationY(mInterceptionLayout) == -toolbar.getHeight();
    }

    private void showToolbar() {
        animateToolbar(0);
    }

    private void hideToolbar() {
        animateToolbar(-toolbar.getHeight());
    }

    private void animateToolbar(final float toY) {
//        float layoutTranslationY = ViewHelper.getTranslationY(mInterceptionLayout);
//        if (layoutTranslationY != toY) {
//            ValueAnimator animator = ValueAnimator.ofFloat(ViewHelper.getTranslationY(mInterceptionLayout), toY).setDuration(200);
//            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                @Override
//                public void onAnimationUpdate(ValueAnimator animation) {
//                    float translationY = (float) animation.getAnimatedValue();
//                    ViewHelper.setTranslationY(mInterceptionLayout, translationY);
//                    if (translationY < 0) {
//                        DrawerLayout.LayoutParams lp = (DrawerLayout.LayoutParams) mInterceptionLayout.getLayoutParams();
//                        lp.height = (int) (getScreenHeight() - translationY);
//                        mInterceptionLayout.requestLayout();
//                    }
//                }
//            });
//            animator.start();
//        }
    }

    /**
     * MyPagerAdapter
     */
    public class MyPagerAdapter extends CacheFragmentStatePagerAdapter {

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
        protected Fragment createItem(int position) {
            Fragment fragment;
            Bundle args = new Bundle();
            switch (position) {
                case 0:
                    fragment = new EventsFragment();
                    args.putInt(EventsFragment.ARG_POSITION, position);
                    fragment.setArguments(args);
                    break;
                case 1:
                    fragment = new EventsFragment();
                    args.putInt(EventsFragment.ARG_POSITION, position);
                    fragment.setArguments(args);
                    break;
                case 2:
                    fragment = new EventsFragment();
                    args.putInt(EventsFragment.ARG_POSITION, position);
                    fragment.setArguments(args);
                    break;
                case 3:
                    fragment = new EventsFragment();
                    args.putInt(EventsFragment.ARG_POSITION, position);
                    fragment.setArguments(args);
                    break;
                case 4:
                    fragment = new EventsFragment();
                    args.putInt(EventsFragment.ARG_POSITION, position);
                    fragment.setArguments(args);
                    break;
                case 5:
                    fragment = new EventsFragment();
                    args.putInt(EventsFragment.ARG_POSITION, position);
                    fragment.setArguments(args);
                    break;
                case 6:
                    fragment = new EventsFragment();
                    args.putInt(EventsFragment.ARG_POSITION, position);
                    fragment.setArguments(args);
                    break;
                case 7:
                    fragment = new EventsFragment();
                    args.putInt(EventsFragment.ARG_POSITION, position);
                    fragment.setArguments(args);
                    break;
                default:
                    fragment = new EventsFragment();
                    args.putInt(EventsFragment.ARG_POSITION, position);
                    fragment.setArguments(args);
                    break;
            }
            return fragment;
        }
    }

}
