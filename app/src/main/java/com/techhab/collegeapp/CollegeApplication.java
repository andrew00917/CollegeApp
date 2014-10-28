package com.techhab.collegeapp;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by jhchoe on 10/16/14.
 */
public class CollegeApplication extends Application {

    // Tag used when logging all messages with the same tag
    static final String TAG = "CollegeApp";

    // Value to see if user clicked Guest (e.g. parents)
    static boolean IS_SOCIAL = true;

    // Check value for user to check if they want app to remember their username & password
    private boolean rememberLogin = false;

    // Logged in status of the user
    private boolean isLoggedIn = false;
    private static final String LOGGED_IN_KEY = "logged_in";

    // Current logged in user
    private User currentUser;
    private static final String USER_KEY = "user_info";
    private static final String USER_PASSWORD_KEY = "user_password";

    // CollegeAppRequestError to show when the GameFragment closes
    private CollegeAppRequestError gameFragmentFBRequestError = null;



    /* College App attributes getters and setters */

    public boolean isSocial() {
        return IS_SOCIAL;
    }

    public void setIsSocial(boolean IS_SOCIAL) {
        this.IS_SOCIAL = IS_SOCIAL;
    }

    public boolean getRememberLogin() {
        return rememberLogin;
    }

    public void setRememberLogin(boolean rememberLogin) {
        this.rememberLogin = rememberLogin;
    }

    public long getLastLoggedInTime() {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(getKey(), MODE_PRIVATE);
        return prefs.getLong("lastLoggedInTime", 0);
    }


    /* Login attributes getters and setters */

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;

        if ( ! isLoggedIn && isSocial()) {
            // If the user is logged out, reset the info and nullify all the logged-in user's values
            setCurrentUser(null);
            setIsSocial(false);
            setRememberLogin(false);
        }
    }

    public String getLoggedInKey() {
        return LOGGED_IN_KEY;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public String getCurrentUserKey() {
        return USER_KEY;
    }

    public String getCurrentUserPasswordKey() {
        return USER_PASSWORD_KEY;
    }

    public String getKey() {
        if ( ! isSocial()) {
            return "guest";
        }
        return LOGGED_IN_KEY;
    }

    public void save() {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(getKey(), MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        if (getRememberLogin() && getCurrentUser() != null && isSocial() && isLoggedIn()) {
            // user checked remember me box
            // save their user name info and password
            editor.putString("userID", getCurrentUser().getUserId());
            editor.putString("userPassword", getCurrentUser().getPassword());
            editor.putBoolean("rememberLogin", getRememberLogin());
            editor.putLong("lastLoggedInTime", System.currentTimeMillis());
        }
        else if (getCurrentUser() != null && isSocial() && isLoggedIn()) {
            // user didn't check remember me box
            // but they are logged in
            // save everything except storing their username and password
            editor.putBoolean("rememberLogin", getRememberLogin());
            editor.putLong("lastLoggedInTime", System.currentTimeMillis());
        }
        else if (getCurrentUser() != null && ! isSocial()) {
            // currently using app as guest
            // prompt the user with Not Authorized dialog
            // but if the user gets the prompt, something wrong with our guest authentication
            // double check the code accordingly.

        }
        else {
            // not logged in, not guest
            // no reason for them to be able to get here.
            // redirect them to logoutFragment.

        }
        editor.apply();
    }

    public void load() {
        if (getCurrentUser() != null && getCurrentUser().isValid()) {
            // After login page, current user is set
            // Could be a new user or not.
            // Either way, treat them same.
            setLoggedIn(true);
            setIsSocial(true);

            // check for user checking remember me box
            if (getRememberLogin()) {
                // if so, save their information.
                save();
            }
        }
        else if (getCurrentUser() != null && getCurrentUser().getUserName().equals("guest")) {
            // guest load
            setLoggedIn(true);
            setIsSocial(false);
        }
        else {
            // error
            // check code, should not get here
            Log.e(TAG, "Error: loading. User is not null and also not a guest.");
        }
    }

    public void onCreate() {
        User user;
        if ( ! getKey().equals("guest")) {
            SharedPreferences prefs = getApplicationContext().getSharedPreferences(getKey(), MODE_PRIVATE);
            boolean l = prefs.getBoolean("rememberLogin", false);
            if (l) {
                // check to see if user's information is saved
                // remember me box checked last time logged in
                setRememberLogin(l);
                user = new User(prefs.getString("userID", null), prefs.getString("userPassword", null));
                if (user.isValid()) {
                    setCurrentUser(user);
                    setLoggedIn(true);
                }
                else {
                    // prompt user with error message
                }
            }
            else {
                // user who didn't want their information stored
                // treat them like new user.
                // Wait for user to put their login information in then
                // construct a new user, set it to current user
                // call this method again.
                // DO NOTHING HERE.
            }
        }
    }
}