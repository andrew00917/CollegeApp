package com.techhab.collegeapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.service.textservice.SpellCheckerService;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class HomeActivity extends FragmentActivity {

    // Tag used when logging messages
    private static final String TAG = HomeActivity.class.getSimpleName();

    private CollegeApplication application = (CollegeApplication) getApplication();

    // Uri used in handleError() below
    private static final Uri M_COLLEGE_URL = Uri.parse("http://kzoo.edu");

    // Fragment attributes
    private static final int LOGGED_OUT_HOME = 0;
    private static final int HOME = 1;
    private static final int FRAGMENT_COUNT = HOME +1;
    private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];

    // Boolean recording whether the activity has been resumed so that
    // the logic in onSessionStateChange is only executed if this is the case
    private boolean isResumed = false;

    // Constructor
    public HomeActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        FragmentManager fm = getSupportFragmentManager();
        fragments[LOGGED_OUT_HOME] = fm.findFragmentById(R.id.loggedOutHomeFragment);
        fragments[HOME] = fm.findFragmentById(R.id.homeFragment);

        FragmentTransaction transaction = fm.beginTransaction();
        for(int i = 0; i < fragments.length; i++) {
            transaction.hide(fragments[i]);
        }
        transaction.commit();

        // Restore the logged-in user's information if it has been saved and the existing data in the application
        // has been destroyed (i.e. the app hasn't been used for a while and memory on the device is low)
        // - only do this if the session is open for the social version only
        if (application.IS_SOCIAL) {
            // loggedIn
            if (savedInstanceState != null) {
                boolean loggedInState = savedInstanceState.getBoolean(application.getLoggedInKey(), false);
                application.setLoggedIn(loggedInState);

                if ( application.isLoggedIn() && application.getCurrentUser() == null) {
                    try {
                        // currentUser
                        String currentUserString = savedInstanceState.getString(application.getCurrentUserKey());
                        String currentUserPassword = savedInstanceState.getString(application.getCurrentUserPasswordKey());
                        if (currentUserString != null) {
                            User currentUser = new User(currentUserString, currentUserPassword);
                            application.setCurrentUser(currentUser);
                        }
                    } catch (Exception e) {
                        Log.e(application.TAG, e.toString());
                    }
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if ( ! application.IS_SOCIAL) {
            showFragment(HOME, false);
        }
        else {
            //Session session = SpellCheckerService.Session.getActiveSession();
            //if (session != null && session.isOpened() && application.getCurrentUser() != null) {
            //    showFragment(HOME, false);
            //}
            //else {
            //    showFragment(LOGGED_OUT_HOME, false);
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

        // Save the currentFBUser
        if (application.getCurrentUser() != null) {
            outState.putString(application.getCurrentUserKey(), application.getCurrentUser().getUserId());
            outState.putString(application.getCurrentUserPasswordKey(), application.getCurrentUser().getPassword());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void showFragment(int fragmentIndex, boolean addToBackStack) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        for (int i = 0; i < fragments.length; i++) {
            if (i == fragmentIndex) {
                transaction.show(fragments[i]);
            } else {
                transaction.hide(fragments[i]);
            }
        }
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();

        // Do other changes depending on the fragment that is now showing
        if (application.IS_SOCIAL) {
            switch (fragmentIndex) {
                case LOGGED_OUT_HOME:
                    // Hide the progressContainer in FBLoggedOutHomeFragment
                    if (fragments[LOGGED_OUT_HOME] != null && ((LoggedOutHomeFragment)fragments[LOGGED_OUT_HOME]) != null) {
                        ((LoggedOutHomeFragment)fragments[LOGGED_OUT_HOME]).progressContainer.setVisibility(View.INVISIBLE);
                    }
                    // Set the loggedIn attribute
                    application.setLoggedIn(false);
                    break;
                case HOME:
                    // Set the loggedIn attribute
                    application.setLoggedIn(true);
                    break;
            }
        }
    }

	/* Facebook Integration Only ... */

    // Call back on HomeActivity when the session state changes to update the view accordingly
    private void updateView() {
        if (isResumed) {
            //Session session = Session.getActiveSession();
            //if (session.isOpened() && ! application.isLoggedIn() && fragments[HOME] != null) {
                // Not logged in, but should be, so fetch the user information and log in (load the HomeFragment)
            //    fetchUserInformationAndLogin();
            //} else if (session.isClosed() && application.isLoggedIn() && fragments[LOGGED_OUT_HOME] != null) {
                // Logged in, but shouldn't be, so load the FBLoggedOutHomeFragment
            //    showFragment(LOGGED_OUT_HOME, false);
            //}

            // Note that error checking for failed logins is done as within an ErrorListener attached to the
            // LoginButton within FBLoggedOutHomeFragment
        }
    }

    // Loads the inventory portion of the HomeFragment.
    private void loadIInformationFragment() {
        Log.d(TAG, "Loading inventory fragment");
        if (isResumed) {
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
        // Close the session, which will cause a callback to show the logout screen
        //Session.getActiveSession().closeAndClearTokenInformation();

        // Clear any permissions
    }
}
