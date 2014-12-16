package com.techhab.collegeapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.techhab.collegeapp.application.CollegeApplication;


public class InfoActivity extends FragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    public static FragmentManager fm;
    // Tag used when logging messages
    private static final String TAG = InfoActivity.class.getSimpleName();

    private CollegeApplication application;

    // Fragment attributes
    private static final int INFO_HOME = 0;
    private static final int FACILITIES = 1;
    private static final int CALENDER = 2;
    //private static final int SECURITY = 4;
    //private static final int GROCERY = 5;
    //private static final int MAP = 3;
    // TODO
    // PLEASE READ THIS COMMENT BEFORE ADDING FRAGMENTS
    // Make sure to add fragments above this comment
    // Then change the DRAWER int below accordingly
    private static final int DRAWER = 3;
    private static final int FRAGMENT_COUNT = DRAWER +1;
    private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];

    private String position;

    // Boolean recording whether the activity has been resumed so that
    // the logic in onSessionStateChange is only executed if this is the case
    private boolean isResumed = false;

    /**
     * Used to store the last screen title.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        application = (CollegeApplication) getApplication();

        fm = getSupportFragmentManager();
        fragments[INFO_HOME] = fm.findFragmentById(R.id.infoFragment);
        fragments[FACILITIES] = fm.findFragmentById(R.id.facilitiesFragment);
        fragments[CALENDER] = fm.findFragmentById(R.id.calenderFragment);
        //fragments[SECURITY] = fm.findFragmentById(R.id.securityFragment);
        //fragments[GROCERY] = fm.findFragmentById(R.id.groceryFragment);
//        fragments[MAP] = fm.findFragmentById(R.id.mapFragment);
        // TODO
        // More fragments go here
        fragments[DRAWER] = fm.findFragmentById(R.id.home_navigation_drawer);

        FragmentTransaction transaction = fm.beginTransaction();
        for(int i = 0; i < fragments.length; i++) {
            transaction.hide(fragments[i]);
        }
        transaction.commit();

        try {
            Intent intent = getIntent();
            position = intent.getStringExtra("position");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        // TODO
        // compare position with fragment name
        if (position.equals("facilities")) {
            showFragment(FACILITIES, false);
        }
        else if (position.equals("cafeteria")) {
            //Intent intent = new Intent(this, CafeteriaActivity.class);
        }
        else if (position.equals("academic calender")) {
            showFragment(CALENDER, false);
        }
        else {
            // default behavior
            showFragment(INFO_HOME, false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        isResumed = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isResumed = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the logged-in state
        outState.putBoolean(application.getLoggedInKey(), application.isLoggedIn());

        if (application.isLoggedIn() && application.isSocial()) {
            // Save the currentUser
            outState.putString(application.getCurrentUserKey(), application.getCurrentUser().getUserId());
            outState.putString(application.getCurrentUserPasswordKey(), application.getCurrentUser().getPassword());
        }
    }

    public void showFragment(int fragmentIndex, boolean addToBackStack) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        for (int i = 0; i < fragments.length; i++) {
            if (i == fragmentIndex) {
                transaction.show(fragments[i]);
            }
            else {
                transaction.hide(fragments[i]);
            }
        }
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu rssItemList for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search:
                //openSearch();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }
}
