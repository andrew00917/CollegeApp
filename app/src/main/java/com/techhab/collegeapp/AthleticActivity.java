package com.techhab.collegeapp;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.techhab.collegeapp.application.CollegeApplication;



public class AthleticActivity extends ActionBarActivity
        implements NavigationDrawerCallbacks, ObservableScrollViewCallbacks {

    private final Handler handler = new Handler();

    private Toolbar toolbar;
    private PagerSlidingTabStrip mPagerSlidingTabStrip;
    private ViewPager mViewPager;
    private SwitchCompat genderSwitch;
    private LinearLayout genderSwitchLayout, header;
    private TextView menText, womenText;
    private FrameLayout toolbarAndGenderSwitch;

    private int mBaseTranslationY;

    private pagerAdapter mPagerAdapter;

    private CollegeApplication application;

    private int currentPosition;

    private String[] mensSports = {"Home Games", "Baseball", "Basketball", "Cross Country",
            "Football", "Golf", "Soccer", "Swim/Dive", "Tennis", "Lacrosse"};
    private String[] womensSports = {"Home Games", "Basketball", "Cross Country", "Golf",
            "Soccer", "Softball", "Swim/Dive", "Tennis", "Volleyball", "Lacrosse"};

    private boolean isLoading; // Variable used to track complex genderSwitch bug

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_athletic);

        application = (CollegeApplication) getApplication();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarAndGenderSwitch = (FrameLayout) findViewById(R.id.toolbar_and_gender_switch);
        header = (LinearLayout) findViewById(R.id.header);
        mPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        genderSwitchLayout = (LinearLayout) findViewById(R.id.gender_switch_layout);
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        menText = (TextView) findViewById(R.id.men_text);
        womenText = (TextView) findViewById(R.id.women_text);

        ViewCompat.setElevation(header, getResources().getDimension(R.dimen.toolbar_elevation));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sports");
        mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.kzooOrange));

//        getSupportActionBar().hide();

        // Handle Boys/Girls toggle switch
        genderSwitch = (SwitchCompat) findViewById(R.id.gender_switch);
        // Set a custom track drawable to keep it from "highlighting" upon selection
        Drawable genderSwitchTrack = getResources().getDrawable(
                R.drawable.abc_switch_track_mtrl_alpha);
        genderSwitchTrack.setColorFilter(0xff9e501b, PorterDuff.Mode.MULTIPLY);
        genderSwitch.setTrackDrawable(genderSwitchTrack);
        Drawable genderSwitchThumb = getResources().getDrawable(
                R.drawable.abc_switch_thumb_material);
        genderSwitch.setThumbDrawable(genderSwitchThumb);
        genderSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    changeGenderView(womensSports);
                    application.setSportPreference(mViewPager.getCurrentItem());

                } else {
                    changeGenderView(mensSports);
                    application.setSportPreference(mViewPager.getCurrentItem());
                }
            }
        });

        // Set men/women text onClickListeners
        menText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if ( genderSwitch.isChecked() ) {
                    genderSwitch.setChecked(false);
                }
            }
        });
        womenText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if ( !genderSwitch.isChecked() ) {
                    genderSwitch.setChecked(true);
                }
            }
        });

        mPagerAdapter = new pagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mPagerSlidingTabStrip.setViewPager(mViewPager);
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4,
                getResources().getDisplayMetrics());
        mViewPager.setPageMargin(pageMargin);

        mViewPager.setCurrentItem(application.getSportPreference());
        // Call notifyDataSetChanged() to get rid of a minor visual bug that keeps the
        // colors from updating in the tab strip
        mPagerSlidingTabStrip.notifyDataSetChanged();

        /* Check to see if this is the first time the user has opened
        AthleticActivity. If it is, show the dialog to choose
        Men's or Women's sports */
        if ( application.getSportsFirstTime() ) {
//          Immediately hide the whole PagerSlidingTabStrip, ViewPager, and gender switch
//          right after until the new user has chosen a gender.
            mPagerSlidingTabStrip.setVisibility(View.INVISIBLE);
            mViewPager.setVisibility(View.INVISIBLE);
            genderSwitchLayout.setVisibility(View.INVISIBLE);
            new MaterialDialog.Builder(this)
                    .title("View Sports For...")
                    .items(new String[]{"Men", "Women"})
                    .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog dialog, View view,
                                                int which, CharSequence text) {
                            if (which == 0) {
                                application.setSportsFirstTime(false);
                                application.setSportsGenderPreference(true);
                                changeGenderView(mensSports);
                                genderSwitch.setChecked(false);
                                mPagerSlidingTabStrip.setVisibility(View.VISIBLE);
                                mViewPager.setVisibility(View.VISIBLE);
                                genderSwitchLayout.setVisibility(View.VISIBLE);
                                dialog.dismiss();
                            } else if (which == 1) {
                                application.setSportsFirstTime(false);
                                application.setSportsGenderPreference(false);
                                genderSwitch.setChecked(true);
                                changeGenderView(womensSports);
                                mPagerSlidingTabStrip.setVisibility(View.VISIBLE);
                                mViewPager.setVisibility(View.VISIBLE);
                                genderSwitchLayout.setVisibility(View.VISIBLE);
                                dialog.dismiss();
                            } else {
                                Toast.makeText(getApplicationContext(), "Please choose a gender",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .positiveText("Choose")
                    .positiveColor(getResources().getColor(R.color.kzooOrange))
                    .negativeText("Cancel")
                    .autoDismiss(false)
                    .negativeColor(getResources().getColor(R.color.gray))
                    .callback(new MaterialDialog.Callback() {
                        // Material Dialog library needs this empty method
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            dialog.dismiss();
                            finish();
                        }
                    })
                    .cancelable(false)
                    .show();
        } else {
            if ( application.getSportsGenderPreference() ) {
                genderSwitch.setChecked(false);
            } else {
                isLoading = true;
                genderSwitch.setChecked(true);
            }
        }

        // Collapsing toolbar stuff

        // When the page is selected, other fragments' scrollY should be adjusted
        // according to the toolbar status(shown/hidden)
        mPagerSlidingTabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int i) {
                propagateToolbarState(toolbarIsShown());
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

        propagateToolbarState(toolbarIsShown());



    }

    private void hideMenu() {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.hide_toolbar);
        a.reset();
        toolbar.startAnimation(a);
//        btn.setVisibility(View.INVISIBLE);
    }

    /** Changes the activity's view from one gender to the other by modifying the
     * adapter's data and refreshing the necessary views.
     *
     * @param titleArray the men's or women's sports titles array
     */
    private void changeGenderView(String[] titleArray) {

        // Get the page index before it changes
        int pageIndexPreSwitch = mViewPager.getCurrentItem();

        if ( titleArray.equals(mensSports) ) {
            application.setSportsGenderPreference(true);
        } else {
            application.setSportsGenderPreference(false);
        }
        mPagerAdapter.TITLES = titleArray;
        mPagerAdapter.notifyDataSetChanged();
        mPagerSlidingTabStrip.notifyDataSetChanged();

        if ( isLoading ) {
            mViewPager.setCurrentItem(application.getSportPreference());
            isLoading = false;
        } else {
            mViewPager.setCurrentItem(convertPage(pageIndexPreSwitch));
        }
        mPagerSlidingTabStrip.notifyDataSetChanged();
        mPagerAdapter.notifyDataSetChanged();

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        application.setSportPreference(mViewPager.getCurrentItem());
    }

    @Override
    public void onPause() {
        super.onPause();
        application.setSportPreference(mViewPager.getCurrentItem());
    }

    /** Converts the previously viewed sport to a "matching" sport of the opposite
     * gender.
     *
     * @param position - the current page index the user is coming from
     * @return - the new page index the user is going to see
     */
    private int convertPage(int position) {
        if ( !application.getSportsGenderPreference() ) {
            switch (position) {
                case 1: return 5;
                case 2: return 1;
                case 3: return 2;
                case 4: return 0;
                case 5: return 3;
                case 6: return 4;
                case 7: return 6;
                case 8: return 7;
                case 9: return 9;
            }
        } else {
            Log.d("position:", "Position: " + position);
            switch (position) {
                case 1: return 2;
                case 2: return 3;
                case 3: return 5;
                case 4: return 6;
                case 5: return 1;
                case 6: return 7;
                case 7: return 8;
                case 8: return 0;
                case 9: return 9;
            }
        }
        return 0;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds rssItemList to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_athletic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle toolbar item clicks here. The toolbar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_reset_shared_prefs:
                application.setSportsFirstTime(true);
                application.setSportsGenderPreference(true);
                finish();
                return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {    }


    /**
     * =======================
     *
     * BEGIN OBSERVABLE SCROLL VIEW
     *
     * =======================
     */


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
        final ObservableScrollView scrollView = (ObservableScrollView) view.findViewById(
                R.id.scrollview);
        if (scrollView == null) {
            return;
        }
        int scrollY = scrollView.getCurrentScrollY();
        if (scrollState == ScrollState.DOWN) {
            showToolbar();
        } else if (scrollState == ScrollState.UP) {
            if (toolbarHeight <= scrollY) {
                hideToolbar();
            } else {
                showToolbar();
            }
        } else {
            // Even if onScrollChanged occurs without scrollY changing, toolbar should be adjusted
            if (toolbarIsShown() || toolbarIsHidden()) {
                // Toolbar is completely moved, so just keep its state
                // and propagate it to other pages
                propagateToolbarState(toolbarIsShown());
            } else {
                // Toolbar is moving but doesn't know which to move:
                // you can change this to hideToolbar()
                showToolbar();
            }
        }
    }

    private Fragment getCurrentFragment() {
        return mPagerAdapter.getItemAt(mViewPager.getCurrentItem());
    }

    private void propagateToolbarState(boolean isShown) {
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

            ObservableScrollView scrollView = (ObservableScrollView) f.getView().findViewById(R.id.scrollview);
            if (isShown) {
                // Scroll up
                if (0 < scrollView.getCurrentScrollY()) {
                    scrollView.scrollTo(0, 0);
                }
            } else {
                // Scroll down (to hide padding)
                if (scrollView.getCurrentScrollY() < toolbarHeight) {
                    scrollView.scrollTo(0, toolbarHeight);
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

    private void showToolbar() {
        float headerTranslationY = ViewHelper.getTranslationY(header);
        if (headerTranslationY != 0) {
            ViewPropertyAnimator.animate(header).cancel();
            ViewPropertyAnimator.animate(header).translationY(0).setDuration(200).start();
        }
        propagateToolbarState(true);
    }

    private void hideToolbar() {
        float headerTranslationY = ViewHelper.getTranslationY(header);
        int toolbarHeight = toolbarAndGenderSwitch.getHeight();
        if (headerTranslationY != -toolbarHeight) {
            ViewPropertyAnimator.animate(header).cancel();
            ViewPropertyAnimator.animate(header).translationY(-toolbarHeight).setDuration(200).start();
        }
        propagateToolbarState(false);
    }

    /**
     * =======================
     *
     * END OBSERVABLE SCROLL VIEW
     *
     * =======================
     */

    /**
     * pagerAdapter for the SlidingPageTab thing
     */
    public class pagerAdapter extends FragmentStatePagerAdapter {

        // Temporarily initiate the TITLES to be an array to avoid NullPointerExceptions
        private String[] TITLES = mensSports;

        private SparseArray<Fragment> mPages;
        private int mScrollY;

        public pagerAdapter(FragmentManager fm) {
            super(fm);
            mPages = new SparseArray<Fragment>();
        }

        public void setScrollY(int scrollY) {
            mScrollY = scrollY;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
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
            Fragment fragment;
            if (0 <= mScrollY ) {
                Bundle args = new Bundle();
                if (application.getSportsGenderPreference()) {
                    switch (position) {
                        /*case 0:
                            fragment = new HomeGamesFragment();
                            args.putInt(HomeGamesFragment.ARG_OBJECT, position + 1);
                            fragment.setArguments(args);
                            break;*/
                        case 1:
                            fragment = new BaseballAndSoftballFragment();
                            args.putInt(BaseballAndSoftballFragment.ARG_SCROLL_Y, mScrollY);
                            args.putBoolean(BaseballAndSoftballFragment.GENDER, true);
                            fragment.setArguments(args);
                            break;
                        case 2:
                            fragment = new BasketballFragment();
                            args.putInt(BasketballFragment.ARG_SCROLL_Y, mScrollY);
                            args.putBoolean(BasketballFragment.GENDER, application.getSportsGenderPreference());
                            fragment.setArguments(args);
                            break;
                        case 3:
                            fragment = new CrossCountryFragment();
                            args.putInt(CrossCountryFragment.ARG_SCROLL_Y, mScrollY);
                            args.putBoolean(CrossCountryFragment.GENDER, application.getSportsGenderPreference());
                            fragment.setArguments(args);
                            break;
                        case 4:
                            fragment = new FootballFragment();
                            args.putInt(FootballFragment.ARG_SCROLL_Y, mScrollY);
                            args.putBoolean(FootballFragment.GENDER, true);
                            fragment.setArguments(args);
                            break;
                        case 5:
                            fragment = new GolfFragment();
                            args.putInt(GolfFragment.ARG_SCROLL_Y, mScrollY);
                            args.putBoolean(GolfFragment.GENDER, true);
                            fragment.setArguments(args);
                            break;
                        case 6:
                            fragment = new SoccerFragment();
                            args.putInt(SoccerFragment.ARG_SCROLL_Y, mScrollY);
                            args.putBoolean(SoccerFragment.GENDER, application.getSportsGenderPreference());
                            fragment.setArguments(args);
                            break;


                        case 8:
                            fragment = new TennisFragment();
                            args.putInt(TennisFragment.ARG_SCROLL_Y, mScrollY);
                            args.putBoolean(TennisFragment.GENDER, application.getSportsGenderPreference());
                            fragment.setArguments(args);
                            break;
                        default:
                            fragment = new CrossCountryFragment();
                            args.putInt(CrossCountryFragment.ARG_SCROLL_Y, mScrollY);
                            args.putBoolean(CrossCountryFragment.GENDER, application.getSportsGenderPreference());
                            fragment.setArguments(args);
                            break;
                    }
                    mPages.put(position, fragment);
                    return fragment;
                } else {
                    switch (position) {
                        case 1:
                            fragment = new BasketballFragment();
                            args.putInt(BasketballFragment.ARG_SCROLL_Y, mScrollY);
                            args.putBoolean(BasketballFragment.GENDER, application.getSportsGenderPreference());
                            fragment.setArguments(args);
                            break;
                        case 2:
                            fragment = new CrossCountryFragment();
                            args.putInt(CrossCountryFragment.ARG_SCROLL_Y, mScrollY);
                            args.putBoolean(CrossCountryFragment.GENDER, application.getSportsGenderPreference());
                            fragment.setArguments(args);
                            break;
                        case 3:
                            fragment = new GolfFragment();
                            args.putInt(GolfFragment.ARG_SCROLL_Y, mScrollY);
                            args.putBoolean(GolfFragment.GENDER, true);
                            fragment.setArguments(args);
                            break;
                        case 4:
                            fragment = new SoccerFragment();
                            args.putInt(SoccerFragment.ARG_SCROLL_Y, mScrollY);
                            args.putBoolean(SoccerFragment.GENDER, application.getSportsGenderPreference());
                            fragment.setArguments(args);
                            break;
                        case 5:
                            fragment = new BaseballAndSoftballFragment();
                            args.putInt(BaseballAndSoftballFragment.ARG_SCROLL_Y, mScrollY);
                            args.putBoolean(BaseballAndSoftballFragment.GENDER, false);
                            fragment.setArguments(args);
                            break;


                        case 7:
                            fragment = new TennisFragment();
                            args.putInt(TennisFragment.ARG_SCROLL_Y, mScrollY);
                            args.putBoolean(TennisFragment.GENDER, application.getSportsGenderPreference());
                            fragment.setArguments(args);
                            break;
                        default:
                            fragment = new BasketballFragment();
                            args.putInt(BasketballFragment.ARG_SCROLL_Y, mScrollY);
                            args.putBoolean(BasketballFragment.GENDER, application.getSportsGenderPreference());
                            fragment.setArguments(args);
                            break;
                    }
                    mPages.put(position, fragment);
                    return fragment;
                }
            }


            Fragment f = new BasketballFragment();
            if (0 <= mScrollY) {
                Bundle args = new Bundle();
                args.putInt(BasketballFragment.ARG_SCROLL_Y, mScrollY);
                f.setArguments(args);
            }
            return f;
        }
    }
}
