package com.techhab.collegeapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class LoggedOutHomeFragment extends Fragment {

    private CollegeApplication application;

    private static final int HOME_FRAGMENT = 1;

    View progressContainer;
    View v;

    private EditText username;
    private EditText password;
    private Button login;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        application = (CollegeApplication) getActivity().getApplication();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_logged_out_home, parent, false);

        progressContainer = v.findViewById(R.id.progressContainer);
        progressContainer.setVisibility(View.INVISIBLE);

        // set buttons here
        // remember me check button and login button etc.
        username = (EditText) v.findViewById(R.id.username_input);
        password = (EditText) v.findViewById(R.id.password_input);
        login = (Button) v.findViewById(R.id.login_button);
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
            if (newUser.isValid()) {
                application.setCurrentUser(newUser);
                application.setIsSocial(true);
                application.setLoggedIn(true);
                application.save();
                ((HomeActivity) getActivity()).showFragment(HOME_FRAGMENT, false);
            }
            else {
                // exception
            }
        } catch (Exception e) {
            e.printStackTrace();
            // alert user for an error
        }
    }
}
