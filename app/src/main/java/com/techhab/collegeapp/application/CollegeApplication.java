package com.techhab.collegeapp.application;

import android.app.Application;
import android.content.SharedPreferences;

import com.techhab.collegeapp.User;

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

    // Gender


    /* Sports Activity */
    private static final String SPORTS_FIRST_TIME = "sports_first_time";
    private boolean openForFirstTime;


    /* College App attributes getters and setters */

    public String getTag() {
        return TAG;
    }

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

    /* Sports Activity getters and setters */
    public boolean getOpenForFirstTime() {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(getKey(), MODE_PRIVATE);
        boolean ret = prefs.getBoolean(SPORTS_FIRST_TIME, false);
        return ret;
    }

    public void setOpenForFirstTime(boolean value) {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(getKey(), MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(SPORTS_FIRST_TIME, value);
        editor.apply();
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
            // guest user.
            // store last logged in time and nothing else
            editor.putLong("lastLoggedInTime", System.currentTimeMillis());
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
            setLoggedIn(false);
            setIsSocial(false);
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
        else {
            if (isSocial()) {
                // not logged in but suppose to
                // set logged in and social attribute to both false
                setIsSocial(false);
                setLoggedIn(false);
            }
        }
        load();
    }
}
