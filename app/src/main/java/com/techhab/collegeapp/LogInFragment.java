package com.techhab.collegeapp;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.support.v4.widget.DrawerLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.techhab.collegeapp.application.CollegeApplication;


public class LogInFragment extends Fragment {

    private CollegeApplication application;

    private static final int LOGIN_HOME_FRAGMENT = 0;
    private static final int HOME_FRAGMENT = 1;

    View progressContainer;
    View v;

    private TextView rememberMeText;
    private CheckBox rememberMeCheckBox;
    private EditText kEmail, password;
    private Button guestButton, logInButton;

    private int numChar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        application = (CollegeApplication) getActivity().getApplication();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_log_in, parent, false);

        if (getActivity().getActionBar() != null && getActivity().getActionBar().isShowing()) {
            getActivity().getActionBar().hide();
        }
        progressContainer = v.findViewById(R.id.progressContainer);
        progressContainer.setVisibility(View.INVISIBLE);

        // set buttons here
        // remember me check button and guestButton button etc.
        guestButton = (Button) v.findViewById(R.id.guest_button);
        logInButton = (Button) v.findViewById(R.id.log_in_button);
        rememberMeCheckBox = (CheckBox) v.findViewById(R.id.remember_me_checkbox);
        kEmail = (EditText) v.findViewById(R.id.username_input);
        password = (EditText) v.findViewById(R.id.password_input);

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kEmail.setError("Invalid email or password");
            }
        });

        rememberMeText = (TextView) v.findViewById(R.id.remember_me_text);

        numChar = kEmail.getText().toString().length();


        rememberMeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( rememberMeCheckBox.isChecked() ) {
                    rememberMeCheckBox.setChecked(false);
                } else {
                    rememberMeCheckBox.setChecked(true);
                }
            }
        });

        guestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_submit();
            }
        });

        return v;
    }

    private void login_submit() {
        String u = kEmail.getText().toString();
        String p = password.getText().toString();

        try {
            User newUser = new User(u, p);
            if (u.equals("guest")) {
                application.setIsSocial(false);
            }
            application.setCurrentUser(newUser);
            application.load();
            if (application.isLoggedIn()) {
                closeKeyboard(getActivity(), kEmail.getWindowToken());
                closeKeyboard(getActivity(), password.getWindowToken());
                ((HomeActivity) getActivity()).showFragment(HOME_FRAGMENT, false);
                ((HomeActivity) getActivity()).mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                ((HomeActivity) getActivity()).getSupportActionBar().show();
            }
            else {
                // exception
                ((HomeActivity) getActivity()).showFragment(LOGIN_HOME_FRAGMENT, false);
            }
            application.save();
        } catch (Exception e) {
            e.printStackTrace();
            // alert user for an error
        }
    }

    /**
     * Hide soft keyboard when logged in
     *
     * @param c             context
     * @param windowToken   window token of the edit text view
     */
    public static void closeKeyboard(Context c, IBinder windowToken) {
        InputMethodManager mgr = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(windowToken, 0);
    }
}
