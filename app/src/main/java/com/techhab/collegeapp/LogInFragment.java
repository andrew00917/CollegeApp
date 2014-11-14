package com.techhab.collegeapp;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.support.v4.widget.DrawerLayout;


public class LogInFragment extends Fragment {

    private CollegeApplication application;

    private static final int LOGIN_HOME_FRAGMENT = 0;
    private static final int HOME_FRAGMENT = 1;

    View progressContainer;
    View v;

    private EditText username;
    private EditText password;
    private Button login;

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
        // remember me check button and login button etc.
        login = (Button) v.findViewById(R.id.login_button);

        username = (EditText) v.findViewById(R.id.username_input);
        numChar = username.getText().toString().length();
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (numChar != 0) {
                    if (s.length() == 0) {
                        login.setText(R.string.guest_in);
                    }
                    else {
                        login.setText(R.string.login);
                    }
                }
                else {
                    if (count == 0) {
                        login.setText(R.string.guest_in);
                    }
                    else {
                        login.setText(R.string.login);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                numChar = s.length();
            }
        });

        password = (EditText) v.findViewById(R.id.password_input);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_submit();
            }
        });

        return v;
    }

    private void login_submit() {
        String u = username.getText().toString();
        String p = password.getText().toString();

        try {
            User newUser = new User(u, p);
            if (u.equals("guest")) {
                application.setIsSocial(false);
            }
            application.setCurrentUser(newUser);
            application.load();
            if (application.isLoggedIn()) {
                closeKeyboard(getActivity(), username.getWindowToken());
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
