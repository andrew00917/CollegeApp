package com.techhab.collegeapp;

import android.graphics.AvoidXfermode;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.techhab.collegeapp.application.CollegeApplication;


public class AthleticActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private final Handler handler = new Handler();

    Toolbar toolbar;
    PagerSlidingTabStrip mPagerSlidingTabStrip;
    ViewPager mViewPager;
    SwitchCompat genderSwitch;
    LinearLayout genderSwitchLayout;

    private pagerAdapter mPagerAdapter;

    private CollegeApplication application;

    private int currentPosition;

    private String[] boysSports = {"Home Games", "Boys", "Baseball", "Basketball", "Football",
            "Swim/Dive", "Tennis"};
    private String[] girlsSports = {"Home Games", "Girls", "Basketball", "Softball", "Tennis",
            "Swim/Dive"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_athletic);

        application = (CollegeApplication) getApplication();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        genderSwitchLayout = (LinearLayout) findViewById(R.id.genderSwitchLayout);
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        // Handle Boys/Girls toggle switch
        genderSwitch = (SwitchCompat) findViewById(R.id.boys_girls_switch);
        // Set a custom track drawable to keep it from "highlighting" upon selection
        Drawable genderSwitchTrack = getResources().getDrawable(R.drawable.abc_switch_track_mtrl_alpha);
        genderSwitchTrack.setColorFilter( 0xff9e501b, PorterDuff.Mode.MULTIPLY );
        genderSwitch.setTrackDrawable(genderSwitchTrack);
        genderSwitch.setThumbResource(R.drawable.abc_switch_thumb_material);
        genderSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    changeGenderView(girlsSports);
                } else {
                    changeGenderView(boysSports);
                }
            }
        });

        mPagerAdapter = new pagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mPagerSlidingTabStrip.setViewPager(mViewPager);
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4,
                getResources().getDisplayMetrics());
        mViewPager.setPageMargin(pageMargin);

        /* Check to see if this is the first time the user has opened
        AthleticActivity. If it is, show the dialog to choose
        Men's or Women's sports */
        if ( application.getSportsFirstTime() ) {
//          Immediately hide the whole PagerSlidingTabStrip, ViewPager, and gender switch
//          right after until the new user has chosen a gender.
            mPagerSlidingTabStrip.setVisibility(View.INVISIBLE);
            mViewPager.setVisibility(View.INVISIBLE);
            genderSwitchLayout.setVisibility(View.INVISIBLE);
            Log.d("Open sports for first time", "true!");
            new MaterialDialog.Builder(this)
                    .title("View Sports For...")
                    .items(new String[]{"Men", "Women"})
                    .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog dialog, View view,
                                                int which, CharSequence text) {
                            if (which == 0) {
                                application.setSportsFirstTime(false);
                                application.setSportsPreference(true);
                                Log.d("Sports Preferences", "I chose men");
                                changeGenderView(boysSports);
                                genderSwitch.setChecked(false);
                                mPagerSlidingTabStrip.setVisibility(View.VISIBLE);
                                mViewPager.setVisibility(View.VISIBLE);
                                genderSwitchLayout.setVisibility(View.VISIBLE);
                                dialog.dismiss();
                            } else if (which == 1) {
                                application.setSportsFirstTime(false);
                                application.setSportsPreference(false);
                                Log.d("Sports Preferences", "I chose women");
                                genderSwitch.setChecked(true);
                                changeGenderView(girlsSports);
                                mPagerSlidingTabStrip.setVisibility(View.VISIBLE);
                                mViewPager.setVisibility(View.VISIBLE);
                                genderSwitchLayout.setVisibility(View.VISIBLE);
                                dialog.dismiss();
                            } else {
                                Log.d("Sports Preferences", "I chose none");
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
            if ( application.getSportsPreference() ) {
                changeGenderView(boysSports);
                genderSwitch.setChecked(false);
            } else {
                changeGenderView(girlsSports);
                genderSwitch.setChecked(true);
            }
        }
    }

    /** Changes the activity from viewing men's sports to women's by modifying the
     * adapter's data and refreshing the necessary views.
     *
     * @param titleArray the men's or women's sports titles array
     */
    private void changeGenderView(String[] titleArray) {
        if ( titleArray.equals(boysSports) ) {
            application.setSportsPreference(true);
        } else {
            application.setSportsPreference(false);
        }
        mPagerAdapter.TITLES = titleArray;
        mPagerAdapter.notifyDataSetChanged();
        mPagerSlidingTabStrip.notifyDataSetChanged();
//        For now, set the current page back to the beginning to keep the user from
//        switching while on page that's too high of an index for the other gender.
        // TODO: add code so that switching genders automatically takes the user to the
        // appropriate page of the opposite gender.
        mViewPager.setCurrentItem(0);

    }

    private void initViews(){

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        setSupportActionBar(toolbar);

//        Disable app name in toolbar
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
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

//        Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
                application.setSportsPreference(true);
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
    public void onNavigationDrawerItemSelected(int position) {

    }

    /**
     * pagerAdapter for the SlidingPageTab thing
     */
    public class pagerAdapter extends FragmentStatePagerAdapter {

        // Temporarily initiate the TITLES to be an array to avoid NullPointerExceptions
        private String[] TITLES = boysSports;

        public pagerAdapter(FragmentManager fm) {
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
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            Bundle args = new Bundle();
            if (application.getSportsPreference()) {
                switch (position) {
                    case 0:
                        fragment = new HomeGamesFragment();
                        args.putInt(HomeGamesFragment.ARG_OBJECT, position + 1);
                        fragment.setArguments(args);
                        break;
                    case 1:
                        fragment = new HomeGamesFragment();
                        args.putInt(HomeGamesFragment.ARG_OBJECT, position + 1);
                        fragment.setArguments(args);
                        break;
                    case 2:
                        fragment = new HomeGamesFragment();
                        args.putInt(HomeGamesFragment.ARG_OBJECT, position + 1);
                        fragment.setArguments(args);
                        break;
                    default:
                        fragment = new HomeGamesFragment();
                        args.putInt(HomeGamesFragment.ARG_OBJECT, position + 1);
                        fragment.setArguments(args);
                        break;
                }
                return fragment;
            } else {
                switch (position) {
                    default:
                        fragment = new BasketballFragment();
                        args.putInt(BasketballFragment.ARG_OBJECT, position + 1);
                        fragment.setArguments(args);
                        break;
                }
                return fragment;
            }
        }
    }
}
