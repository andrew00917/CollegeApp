package com.techhab.collegeapp;

import android.content.Context;
import android.os.AsyncTask;
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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;


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
                if (kEmail.getText().toString().length() < 7 || password.getText().toString().length() < 3) {
                    showLoginError();
                } else {
                    loginSubmit();
                }
            }
        });

        rememberMeText = (TextView) v.findViewById(R.id.remember_me_text);

        //numChar = kEmail.getText().toString().length();

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
                kEmail.setText("guest");
                loginSubmit();
            }
        });

        return v;
    }

    private void loginSubmit() {
        String u = kEmail.getText().toString();
        String p = password.getText().toString();

        if (u.equals("guest")) {
            application.setIsSocial(false);
        }

        User newUser = new User(u, p);
        application.setCurrentUser(newUser);
        new MySqlAsync().execute(u, p);
    }

    private void postLogin(String result) {
        User user = application.getCurrentUser();
        if (result.length() > 5) {
            user.setValid(true);

            // set user info here
            String[] info = result.split(",");
            user.setUserId(info[0]);
            user.setPassword(info[1]);
            if (info[2].equals("TRUE")) {
                user.setActive(true);
            } else {
                user.setActive(false);
            }
            user.setFirstName(info[3]);
            user.setLastName(info[4]);
            user.setUserName();
            user.setEmail(info[5]);

            application.setRememberLogin(rememberMeCheckBox.isChecked());
            application.setLoggedIn(true);
            application.setIsSocial(true);
            application.save();

            closeKeyboard(getActivity(), kEmail.getWindowToken());
            closeKeyboard(getActivity(), password.getWindowToken());
            ((HomeActivity) getActivity()).showFragment(HOME_FRAGMENT, false);
            ((HomeActivity) getActivity()).mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            ((HomeActivity) getActivity()).getSupportActionBar().show();
        } else if (result.equals("guest")) {
            user.setUserAsGuest();

            closeKeyboard(getActivity(), kEmail.getWindowToken());
            closeKeyboard(getActivity(), password.getWindowToken());
            ((HomeActivity) getActivity()).showFragment(HOME_FRAGMENT, false);
            ((HomeActivity) getActivity()).mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            ((HomeActivity) getActivity()).getSupportActionBar().show();
        } else {
            showLoginError();
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

    private void showLoginError() {
        kEmail.setError("Invalid email or password");
    }

    /**
     * Class: MySqlAsync
     *
     * Connect to mySQL via php using getMethod.
     * Use HttpGet and HttpClient to connect.
     */
    public class MySqlAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String...params) {
            String u = (String) params[0];
            String p = (String) params[1];

            if (u.equals("guest")) {
                return "guest";
            } else {
                String link = "http://www.techhab.com/sample_query.php?useremail=" + u + "&password=" + p;

                try {
                    URL url = new URL(link);
                    HttpClient client = new DefaultHttpClient();
                    HttpGet request = new HttpGet();
                    request.setURI(new URI(link));

                    HttpResponse response = client.execute(request);
                    BufferedReader in = new BufferedReader
                            (new InputStreamReader(response.getEntity().getContent()));

                    StringBuffer sb = new StringBuffer("");
                    String line = "";
                    while ((line = in.readLine()) != null) {
                        sb.append(line);
                    }
                    in.close();

                    return sb.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return "guest";
        }

        @Override
        protected void onPostExecute(String result){
            postLogin(result);
        }
    }
}
