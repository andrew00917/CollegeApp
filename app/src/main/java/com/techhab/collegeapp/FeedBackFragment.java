package com.techhab.collegeapp;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 */
public class FeedBackFragment extends Fragment {
    SwitchCompat swStudentAccount;
    EditText etFeedBack;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view  = inflater.inflate(R.layout.fragment_feedback, container, false);
        Button btSend = (Button) view.findViewById(R.id.feed_back_btSend);
        swStudentAccount = (SwitchCompat) view.findViewById(R.id.witchCompatStudentAccount);
        swStudentAccount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                displayStudentAccount(view);
            }
        });
        etFeedBack = (EditText) view.findViewById(R.id.et_feed_back);
        btSend.setOnClickListener(onClickListener);
        //
        TextView tvApplicationVersion = (TextView) view.findViewById(R.id.tv_application_version);
        tvApplicationVersion.setText(FeedBackFragment.getApplicationVersion(getActivity()));
        TextView tvDeviceModel = (TextView) view.findViewById(R.id.tv_device_model);
        tvDeviceModel.setText(FeedBackFragment.getDeviceName());

        TextView tvOSVersion = (TextView) view.findViewById(R.id.tv_os_version);
        tvOSVersion.setText("Android " + Build.VERSION.RELEASE);

        displayStudentAccount(view);
        TextView tvTime = (TextView) view.findViewById(R.id.tv_time);

        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        tvTime.setText(formatter.format(new Date()));

        View parentView = view.findViewById(R.id.feedBack_parentView);
        setupUI(parentView);
        return view;
    }

    public void setupUI(View view) {

        if(!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideKeyboard();
                    return false;
                }

            });
        }

        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(innerView);
            }
        }
    }

    private void hideKeyboard()
    {
        InputMethodManager inputMethodManager = (InputMethodManager)  getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }

    private void displayStudentAccount(View view) {
        if (swStudentAccount.isChecked())
        {
            view.findViewById(R.id.preview_llGoogleAccount).setVisibility(View.VISIBLE);
            view.findViewById(R.id.preview_googleAccountDivider).setVisibility(View.VISIBLE);
            TextView tvGoogleAccount = (TextView) view.findViewById(R.id.tv_google_account);
            tvGoogleAccount.setText(FeedBackFragment.getPossibleGoogleAccount(getActivity()));
        }
        else
        {
            view.findViewById(R.id.preview_llGoogleAccount).setVisibility(View.GONE);
            view.findViewById(R.id.preview_googleAccountDivider).setVisibility(View.GONE);
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.feed_back_btSend:
                    sendEmail();
                    break;
            }
        }
    };

    private void sendEmail()
    {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"oat1345@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "Feed Back");
        i.putExtra(Intent.EXTRA_TEXT   , getEmailBody());
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }


    private String getEmailBody()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        String body = etFeedBack.getText().toString() + "\n"
                + "Application Version: " + getApplicationVersion(getActivity()) +"\n"
                + "Device Model: " + getDeviceName() + "\n"
                + "OS version: " + Build.VERSION.RELEASE + "\n"
                + (swStudentAccount.isChecked() ? "Google Account : " + getPossibleGoogleAccount(getActivity()) + "\n" : "")
                + "Time: " + formatter.format(new Date());
        return body;
    }

    public static String getApplicationVersion(Context context)
    {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            Log.e(FeedBackFragment.class.getSimpleName(), e.getMessage());
        }
        return "";
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    public static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public static String getPossibleGoogleAccount(Context context)
    {
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(context).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                return account.name;
            }
        }
        return "";
    }


}
