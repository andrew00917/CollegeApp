package com.techhab.collegeapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.techhab.collegeapp.application.CollegeApplication;

/**
 * Created by akhavantafti on 1/21/2015.
 */
public class PreviewFeedBackActivity  extends ActionBarActivity implements NavigationDrawerCallbacks {

    // Tag used when logging messages
    private static final String TAG = FeedBackActivity.class.getSimpleName();
    public static final String IS_GOOGLE_ACCOUNT_ON = "GOOGLE_ACCOUNT_ON";

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
    public PreviewFeedBackActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_feedback);


        application = (CollegeApplication) getApplication();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        boolean isGoogleAccountOn = getIntent().getBooleanExtra(IS_GOOGLE_ACCOUNT_ON, true);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_GOOGLE_ACCOUNT_ON, isGoogleAccountOn);
        PreviewFeedBackFragment frag = new PreviewFeedBackFragment();
        frag.setArguments(bundle);
        transaction.replace(R.id.preview_feed_back_fragment, frag);
        transaction.commit();

    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu rssItemList for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_settings, menu);
        return true;
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

}
