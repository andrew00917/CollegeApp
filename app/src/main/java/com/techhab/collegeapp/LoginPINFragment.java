package com.techhab.collegeapp;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.techhab.collegeapp.application.CollegeApplication;


public class LoginPINFragment extends Fragment {
    private CollegeApplication application;
    View v;
    private TextView message;
    private int pin;
    private int toFragment = -1;

    public int getToFragment() {
        return toFragment;
    }

    public void setToFragment(int i) {
        toFragment = i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        application = (CollegeApplication) getActivity().getApplication();
        pin = application.getPin();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final User user = application.getCurrentUser();
        v = inflater.inflate(R.layout.activity_pin, container, false);
        v.setBackgroundColor(Color.WHITE);
        if (getActivity().getActionBar() != null && getActivity().getActionBar().isShowing()) {
            getActivity().getActionBar().hide();
        }

        message = (TextView) v.findViewById(R.id.pin_message);
        message.setText(user.getFirstName() + " " + user.getLastName());
        message.setTextColor(Color.GRAY);

        final TextView pintext = (TextView) v.findViewById(R.id.pintext);
        Button btn1 = (Button) v.findViewById(R.id.button1);
        Button btn2 = (Button) v.findViewById(R.id.button2);
        Button btn3 = (Button) v.findViewById(R.id.button3);
        Button btn4 = (Button) v.findViewById(R.id.button4);
        Button btn5 = (Button) v.findViewById(R.id.button5);
        Button btn6 = (Button) v.findViewById(R.id.button6);
        Button btn7 = (Button) v.findViewById(R.id.button7);
        Button btn8 = (Button) v.findViewById(R.id.button8);
        Button btn9 = (Button) v.findViewById(R.id.button9);
        Button btn0 = (Button) v.findViewById(R.id.button0);
        Button btnpass = (Button) v.findViewById(R.id.button_password);
        ImageButton btnback = (ImageButton) v.findViewById(R.id.button_backspace);

        btnpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText input = new EditText(getActivity());
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                AlertDialog passDialog = new AlertDialog.Builder(getActivity())
                        .setTitle("Enter Password")
                        .setMessage("Please enter your password")
                        .setView(input)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if ( input.getText().toString().equals(user.getPassword()) ) {
                                    verified();
                                    dialog.dismiss();
                                }

                                else {
                                    Toast.makeText(getActivity(), "Incorrect password", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();;
                            }
                        })
                        .setCancelable(false)
                        .create();
                // Open the soft keyboard
                passDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                passDialog.show();

            }
        });

        btn1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                pintext.append("1");
            }
        });
        btn2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                pintext.append("2");
            }
        });
        btn3.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                pintext.append("3");
            }
        });
        btn4.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                pintext.append("4");
            }
        });
        btn5.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                pintext.append("5");
            }
        });
        btn6.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                pintext.append("6");
            }
        });
        btn7.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                pintext.append("7");
            }
        });
        btn8.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                pintext.append("8");
            }
        });
        btn9.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                pintext.append("9");
            }
        });
        btn0.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                pintext.append("0");
            }
        });
        btnback.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if ( pintext.length() > 0 ) {
                    pintext.getEditableText().delete(pintext.length() - 1, pintext.length());
                }
            }
        });

        pintext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //nothing
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                if ( pintext.length() > 3 ) {
                    String pinString = pintext.getText().toString();
                    if ( pin == Integer.parseInt(pinString) ) {
                        verified();
                    }
                    else {
                        pintext.setText("");
                        message.setText("Incorrect PIN");
                        message.setTextColor(Color.RED);
                    }
                }
            }
        });

        return v;

    }
    private void verified() {
        // a new fragment to switch to
        Fragment fragment = new ProfileFragment();
        changeFragment();
    }
    private void changeFragment() {
        Fragment fragment;
        if (getToFragment() == 0) {
            fragment = new MyCoursesFragment();
        }
        else if ( getToFragment() == 1 ) {
            fragment = new MyCoursesFragment();
        }
        else if ( getToFragment() == 2 ) {
            fragment = new MyCoursesFragment();
        }
        else {
            //error
            return;
        }
        ProfileActivity activity = (ProfileActivity) getActivity();
        FragmentManager fm = activity.getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.profile_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
