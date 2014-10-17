package com.techhab.collegeapp;

import android.app.Application;
import android.content.SharedPreferences;

/**
 * Created by jhchoe on 10/16/14.
 */
public class CollegeApplication extends Application {

    // Value to see if user clicked Guest (e.g. parents)
    static boolean IS_SOCIAL = true;

    // Logged in status of the user
    private boolean isLoggedIn = false;
    private static final String LOGGED_IN_KEY = "logged_in";

    // Current logged in user and key for saving/restoring during the Activity lifecycle
    private User currentUser;
    private static final String CURRENT_USER_KEY = "current_user";

    // CollegeAppRequestError to show when the GameFragment closes
    private CollegeAppRequestError gameFragmentFBRequestError = null;


    /* College App attributes getters and setters */

    public boolean isSocial() {
        return IS_SOCIAL;
    }

    public void setIsSocial(boolean IS_SOCIAL) {
        this.IS_SOCIAL = IS_SOCIAL;
    }

    public long getLastLoggedInTime() {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("user_info", MODE_PRIVATE);
        return prefs.getLong("lastLoggedInTime", 0);
    }


    /* Login attributes getters and setters */

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setIsLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;

        if ( ! isLoggedIn && isSocial()) {
            // If the user is logged out, reset the info and nullify all the logged-in user's values
            setCurrentUser(null);
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
