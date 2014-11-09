package com.techhab.collegeapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;


public class CafeteriaActivity extends FragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, ActionBar.TabListener {
    ViewPager viewPager;
    ActionBar actionBar;

    // Tag used when logging messages
    private static final String TAG = CafeteriaActivity.class.getSimpleName();

    private CollegeApplication application;

    // Fragment attributes
    private static final int CAFETERIA = 0;
    private static final int RICHARDSON = 1;
    private static final int DRAWER = 2;
    private static final int FRAGMENT_COUNT = DRAWER +1;
    private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];

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
        setContentView(R.layout.activity_cafeteria);
        viewPager=(ViewPager)findViewById(R.id.pager);
        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                              @Override
                                              public void onPageScrolled(int i, float v, int i2) {
                                                  Log.d("VIVZ", "onPageScrolled at"+"position"+i+"from"+v+"with number of pixels"+i2);
                                              }

                                              @Override
                                              public void onPageSelected(int i) {
                                                  actionBar.setSelectedNavigationItem(i);
                                                  //Log.d("VIVZ", "onPageSelected at"+"position"+i);
                                              }

                                              @Override
                                              public void onPageScrollStateChanged(int i) {
                                                if (i==ViewPager.SCROLL_STATE_IDLE)
                                                {
                                                    Log.d("VIVZ", "onPageScrollStateChanged IDLE");
                                                }
                                                if(i==ViewPager.SCROLL_STATE_DRAGGING)
                                                {
                                                    Log.d("VIVZ", "onPageScrollStateChanged Dragging");
                                                }
                                                  if(i==ViewPager.SCROLL_STATE_SETTLING)
                                                  {
                                                      Log.d("VIVZ", "onPageScrollStateChanged Settling");
                                                  }
                                                }
                                              });

//action bar

        actionBar= getActionBar();
        if (actionBar != null) {
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            ActionBar.Tab tab1 = actionBar.newTab();
            tab1.setText("Breakfast");
            tab1.setTabListener(this);

            ActionBar.Tab tab2 = actionBar.newTab();
            tab2.setText("Lunch");
            tab2.setTabListener(this);

            ActionBar.Tab tab3 = actionBar.newTab();
            tab3.setText("Dinner");
            tab3.setTabListener(this);

            actionBar.addTab(tab1);
            actionBar.addTab(tab2);
            actionBar.addTab(tab3);
        }

        application = (CollegeApplication) getApplication();
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        // TODO

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
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
            case R.id.action_refresh:
                //refresh();
                return true;
            case R.id.action_overflow:
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

    @Override
    public void onTabSelected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {
            //Log.d("VIVZ", "onTabSelected at"+"position"+tab.getPosition()+"name"+tab.getText());
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {
           // Log.d("VIVZ", "onTabUnselected at"+"position"+tab.getPosition()+"name"+tab.getText());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {
           // Log.d("VIVZ", "onTabReselected at"+"position"+tab.getPosition()+"name"+tab.getText());
    }
}

//making adapter for navigation tab
class MyAdapter extends FragmentPagerAdapter {
    public MyAdapter(FragmentManager fm) {
        super(fm);

    }

    @Override
    public Fragment getItem(int arg0) {
        Fragment fragment = null;
        if (arg0 == 0) {
            fragment = new RichardsonMenuFragment();
        }
        if (arg0 == 1) {
            fragment = new RichardsonMenuLunch();
        }
        if (arg0 == 2) {
            fragment = new RichardsonMenuDinner();
        }
        return fragment;
    }
    @Override
    public int getCount () {
        return 3;
    }

}
