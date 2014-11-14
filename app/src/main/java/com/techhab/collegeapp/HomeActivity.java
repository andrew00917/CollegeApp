package com.techhab.collegeapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;


public class HomeActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    // Tag used when logging messages
    private static final String TAG = HomeActivity.class.getSimpleName();

    private CollegeApplication application;

    // Uri used in handleError() below
    private static final Uri M_COLLEGE_URL = Uri.parse("http://kzoo.edu");

    // Fragment attributes
    private static final int LOG_IN_HOME = 0;
    private static final int HOME = 1;
    private static final int DRAWER = 2;
    private static final int FRAGMENT_COUNT = DRAWER + 1;
    private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];

    // Boolean recording whether the activity has been resumed so that
    // the logic in onSessionStateChange is only executed if this is the case
    private boolean isResumed = false;

    /* Sub menu animation */
    private ListView sub;

    /* Action Bar */
    private ActionBar actionBar;

    /*  Navigation Drawer Stuff */

    // Drawer layout itself
    public DrawerLayout mDrawerLayout;

    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title.
     */
    private CharSequence mTitle;

    // Constructor
    public HomeActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setContentView(R.layout.activity_home);

        application = (CollegeApplication) getApplication();

        initViews();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.kzooOrange));

        FragmentManager fm = getSupportFragmentManager();
        fragments[LOG_IN_HOME] = fm.findFragmentById(R.id.loggedOutHomeFragment);
        fragments[HOME] = fm.findFragmentById(R.id.homeFragment);
        fragments[DRAWER] = fm.findFragmentById(R.id.home_navigation_drawer);

        FragmentTransaction transaction = fm.beginTransaction();
        for(int i = 0; i < fragments.length; i++) {
            transaction.hide(fragments[i]);
        }
        transaction.commit();

        // Restore the logged-in user's information if it has been saved and the existing data in the application
        // has been destroyed (i.e. the app hasn't been used for a while and memory on the device is low)
        // - only do this if the session is open for the social version only
        if (application.isSocial()) {
            // loggedIn
            if (savedInstanceState != null) {
                boolean loggedInState = savedInstanceState.getBoolean(application.getLoggedInKey(), false);
                application.setLoggedIn(loggedInState);

                if ( application.isLoggedIn() && application.getCurrentUser() == null) {
                    try {
                        // currentUser
                        String currentUserString = savedInstanceState.getString(application.getCurrentUserKey());
                        String currentUserPassword = savedInstanceState.getString(application.getCurrentUserPasswordKey());
                        if (currentUserString.equals("guest")){
                            // Logged in but as guest
                            // set is social attribute
                            application.setIsSocial(false);
                        }
                        else {
                            User currentUser = new User(currentUserString, currentUserPassword);
                            if ( ! currentUser.getUserName().equals("guest")) {
                                application.setCurrentUser(currentUser);
                                application.setIsSocial(true);
                            }
                            else {
                                application.setIsSocial(false);
                            }
                        }
                    } catch (Exception e) {
                        Log.e(application.getTag(), e.toString());
                    }
                }
                else {
                    application.setIsSocial(false);
                }
            }
            else {
                application.setIsSocial(false);
                application.setLoggedIn(false);
            }
        }

        /* Navigation Drawer onCreate stuff */
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.home_navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
//        mNavigationDrawerFragment.setUp(
//                R.id.home_navigation_drawer,
//                (DrawerLayout) findViewById(R.id.drawer_layout));
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // after authenticating the user's id, we get here
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if ( ! (application.isSocial() || application.isLoggedIn())
                && fragments[LOG_IN_HOME] != null) {
            // not logged in and also not guest
            showFragment(LOG_IN_HOME, false);
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            getSupportActionBar().hide();
        }
        else if (application.isLoggedIn() && ! application.isSocial()
                && fragments[HOME] != null) {
            // logged in as guest
            showFragment(HOME, false);
        }
        else if (application.isLoggedIn() && application.isSocial()
                && fragments[HOME] != null) {
            // logged in
            showFragment(HOME, false);
        }
        else {
            // TODO showing logged out home for now
            showFragment(LOG_IN_HOME, false);
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

            //Session session = SpellCheckerService.Session.getActiveSession();
            //if (session != null && session.isOpened() && application.getCurrentUser() != null) {
            //    showFragment(HOME, false);
            //}
            //else {
            //    showFragment(LOG_IN_HOME, false);
            //}
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
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void setListView(ListView sub) {
        this.sub = sub;
    }

    public ListView getListView() {
        return sub;
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

        // Do other changes depending on the fragment that is now showing
        if (application.isSocial()) {
            switch (fragmentIndex) {
                case LOG_IN_HOME:
                    // Hide the progressContainer in LoggedOutHomeFragment
                    if (fragments[LOG_IN_HOME] != null) {
                        ((LogInFragment)fragments[LOG_IN_HOME]).progressContainer.setVisibility(View.INVISIBLE);
                    }

                    if (actionBar != null) {
                        actionBar.hide();
                    }
                    // Set the loggedIn attribute
                    application.setLoggedIn(false);
                    break;
                case HOME:
                    if (actionBar != null && ! actionBar.isShowing()) {
                        actionBar.show();
                    }
                    // Set the loggedIn attribute
                    application.setLoggedIn(true);
                    break;
            }
        }
        else {
            if (fragmentIndex == LOG_IN_HOME) {
                if (actionBar != null) {
                    actionBar.hide();
                }
            }
        }
    }

	/* Facebook Integration Only ... */

    // Call back on HomeActivity when the session state changes to update the view accordingly
    private void updateView() {
        if (isResumed) {
            // TODO
            Toast.makeText(this, "update view", Toast.LENGTH_SHORT).show();
            //Session session = Session.getActiveSession();
            //if (session.isOpened() && ! application.isLoggedIn() && fragments[HOME] != null) {
                // Not logged in, but should be, so fetch the user information and log in (load the HomeFragment)
            //    fetchUserInformationAndLogin();
            //} else if (session.isClosed() && application.isLoggedIn() && fragments[LOG_IN_HOME] != null) {
                // Logged in, but shouldn't be, so load the FBLoggedOutHomeFragment
            //    showFragment(LOG_IN_HOME, false);
            //}

            // Note that error checking for failed logins is done as within an ErrorListener attached to the
            // LoginButton within FBLoggedOutHomeFragment
        }
    }

    // Loads the inventory portion of the HomeFragment.
    private void loadIInformationFragment() {
        Log.d(TAG, "Loading information fragment");
        if (isResumed) {
            // TODO
            Toast.makeText(this, "Loading information fragment", Toast.LENGTH_SHORT).show();
            //((HomeFragment)fragments[HOME]).loadInformation();
        }
        else {
            showError(getString(R.string.error_switching_screens), true);
        }
    }

    void handleError(CollegeAppRequestError error, boolean logout) {
        DialogInterface.OnClickListener listener = null;
        String dialogBody = null;

        if (error == null) {
            dialogBody = getString(R.string.error_dialog_default_text);
        }
        else {
            switch (error.getCategory()) {
                case AUTHENTICATION_RETRY:
                    // tell the user what happened by getting the message id, and
                    // retry the operation later
                    String userAction = (error.shouldNotifyUser()) ? "" :
                            getString(error.getUserActionMessageId());
                    dialogBody = getString(R.string.error_authentication_retry, userAction);
                    listener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, M_COLLEGE_URL);
                            startActivity(intent);
                        }
                    };
                    break;

                case AUTHENTICATION_REOPEN_SESSION:
                    // close the session and reopen it.
                    dialogBody = getString(R.string.error_authentication_reopen);
                    listener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Session session = Session.getActiveSession();
                            //if (session != null && !session.isClosed()) {
                            //    session.closeAndClearTokenInformation();
                            //}
                        }
                    };
                    break;

                case SERVER:
                case THROTTLING:
                    // this is usually temporary, don't clear the fields, and
                    // ask the user to try again
                    dialogBody = getString(R.string.error_server);
                    break;

                case BAD_REQUEST:
                    // this is likely a coding error, ask the user to file a bug
                    dialogBody = getString(R.string.error_bad_request, error.getErrorMessage());
                    break;

                case CLIENT:
                    // this is likely an IO error, so tell the user they have a network issue
                    dialogBody = getString(R.string.network_error);
                    break;

                case OTHER:
                default:
                    // an unknown issue occurred, this could be a code error, or
                    // a server side issue, log the issue, and either ask the
                    // user to retry, or file a bug
                    dialogBody = getString(R.string.error_unknown, error.getErrorMessage());
                    break;
            }
        }

        new AlertDialog.Builder(this)
                .setPositiveButton(R.string.error_dialog_button_text, listener)
                .setTitle(R.string.error_dialog_title)
                .setMessage(dialogBody)
                .show();

        if (logout) {
            logout();
        }
    }

    // Show user error message as a toast
    void showError(String error, boolean logout) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        if (logout) {
            logout();
        }
    }

    private void logout() {
        // TODO
        // Close the session, which will cause a callback to show the logout screen
        //Session.getActiveSession().closeAndClearTokenInformation();

        // Clear any permissions
    }


    /* Navigation Drawer Methods */
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
//        android.app.FragmentManager fragmentManager = getFragmentManager();
        /*fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();*/
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

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(mTitle);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        if (!mNavigationDrawerFragment.isDrawerOpen()) {
//            // Only show items in the action bar relevant to this screen
//            // if the drawer is not showing. Otherwise, let the drawer
//            // decide what to show in the action bar.
//            getMenuInflater().inflate(R.menu.main, menu);
//            restoreActionBar();
//            return true;
//        }
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
        switch (item.getItemId()) {
            case R.id.action_search:
                //openSearch();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends android.app.Fragment {
        /*
         * The fragment argument representing the section number for this
         * fragment.
         * */

        private static final String ARG_SECTION_NUMBER = "section_number";

        /*
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        //@Override
        //public View onCreateView(LayoutInflater inflater, ViewGroup container,
        //                         Bundle savedInstanceState) {
        //    View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        //    return rootView;
        //}

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((HomeActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }
}
