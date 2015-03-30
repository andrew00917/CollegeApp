package com.techhab.collegeapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.techhab.collegeapp.application.CollegeApplication;


public class FeedBackActivity extends ActionBarActivity implements NavigationDrawerCallbacks {

    // Tag used when logging messages
    private static final String TAG = FeedBackActivity.class.getSimpleName();

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
    public FeedBackActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);


        application = (CollegeApplication) getApplication();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
