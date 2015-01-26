package com.techhab.collegeapp;

import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.techhab.collegeapp.application.CollegeApplication;

/**
 * Created by jhchoe on 11/01/15.
 */
public class ProfileActivity extends ActionBarActivity implements NavigationDrawerCallbacks {

    // Tag used when logging messages
    private static final String TAG = ProfileActivity.class.getSimpleName();

    /**
     * Keys for profile intent
     *      user id
     *      user email
     */
    private static final String USER_ID_KEY = "user_id";
    private static final String USER_EMAIL_KEY = "user_email";

    private String userId = "";
    private String userEmail="";

    private CollegeApplication application;

    // Fragment attributes
    private static final int PROFILE = 0;
    private static final int FRAGMENT_COUNT = PROFILE + 1;
    private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];

    /*  Navigation Drawer Stuff */
    public DrawerLayout mDrawerLayout;
//    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Toolbar mToolbar;
//    public ActionBarDrawerToggle mDrawerToggle;

    // Boolean recording whether the activity has been resumed so that
    // the logic in onSessionStateChange is only executed if this is the case
    private boolean isResumed = false;

    /**
     * Used to store the last screen title.
     */
    private CharSequence mTitle;

    // Constructor
    public ProfileActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        userId = intent.getStringExtra(USER_ID_KEY);
        userEmail = intent.getStringExtra(USER_EMAIL_KEY);

        application = (CollegeApplication) getApplication();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        mNavigationDrawerFragment = (NavigationDrawerFragment)
//                getSupportFragmentManager().findFragmentById(R.id.home_navigation_drawer);
//        mNavigationDrawerFragment.setUp(R.id.home_navigation_drawer, mDrawerLayout, mToolbar);

//        mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.kzooOrange));

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putString(ProfileFragment.ARG_ID, userId);
        bundle.putString(ProfileFragment.ARG_EMAIL, userEmail);

        ProfileFragment frag = new ProfileFragment();
        frag.setArguments(bundle);

        transaction.replace(R.id.profile_fragment, frag);
        transaction.commit();

    }

    @Override
    public void onBackPressed() {
//        if (mNavigationDrawerFragment.isDrawerOpen()) {
//            mDrawerLayout.closeDrawer(Gravity.START);
//        } else {
            super.onBackPressed();
//        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
    }

    @Override
    public void onResume() {
        super.onResume();
        isResumed = true;

        onResumeFragments();
    }

    @Override
    public void onPause() {
        super.onPause();
        isResumed = false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

//        outState.putBoolean(application.getLoggedInKey(), application.isLoggedIn());
//
//        if (application.isLoggedIn() && application.isSocial()) {
//            // Save the currentUser
//            outState.putString(application.getCurrentUserKey(), application.getCurrentUser().getUserId());
//            outState.putString(application.getCurrentUserPasswordKey(), application.getCurrentUser().getPassword());
//        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
//        application.setLoggedIn(savedInstanceState.getBoolean(application.getLoggedInKey()));
//
//        if (application.isLoggedIn()) {
//            this.showFragment(HOME, false);
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu rssItemList for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

//        if (mDrawerToggle.onOptionsItemSelected(item)) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    public void changeFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();

    }

    public String getUserId() {
        return userId;
    }

    public String getUserEmail() {
        return userEmail;
    }
}
