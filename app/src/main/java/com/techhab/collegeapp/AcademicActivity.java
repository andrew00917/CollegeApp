package com.techhab.collegeapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;


public class AcademicActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    // When requested, this adapter returns a DemoObjectFragment,
    // representing an object in the collection.
    private CollectionPagerAdapter mCollectionPagerAdapter;
    private ViewPager mViewPager;

    // Fragment attributes
    private static final int COURSE_SEARCH = 0;
    private static final int CALENDAR = 1;
    private static final int DRAWER = 2;
    private static final int FRAGMENT_COUNT = DRAWER + 1;
    private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];

    public AcademicActivity() {
        super();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academic);

        initViews();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
        mCollectionPagerAdapter = new CollectionPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mCollectionPagerAdapter);
    }

    private void initViews(){

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        setSupportActionBar(toolbar);

        // Disable app name in toolbar
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,toolbar ,  R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
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

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_academic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

    }
}

// Since this is an object collection, use a FragmentStatePagerAdapter,
// and NOT a FragmentPagerAdapter.
class CollectionPagerAdapter extends FragmentStatePagerAdapter {

    private FragmentManager fm;

    public CollectionPagerAdapter(FragmentManager fm) {
        super(fm);
        this.fm = fm;
    }

    @Override
    public Fragment getItem(int i) {
//        Fragment fragment = new ObjectFragment();
//        Bundle args = new Bundle();
//        // Our object is just an integer :-P
//        args.putInt(ObjectFragment.ARG_OBJECT, i + 1);
//        fragment.setArguments(args);
        return fm.findFragmentById(R.id.calendarFragment);
    }

    @Override
    public int getCount() {
        return 100;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "OBJECT " + (position + 1);
    }
}