package com.techhab.collegeapp;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;


public class PreviewFeedBackFragment extends Fragment {

    private boolean isGoogleAccountOn = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isGoogleAccountOn = getArguments().getBoolean(PreviewFeedBackActivity.IS_GOOGLE_ACCOUNT_ON);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preview_feedback, container, false);
        TextView tvApplicationVersion = (TextView) view.findViewById(R.id.tv_application_version);
         tvApplicationVersion.setText(FeedBackFragment.getApplicationVersion(getActivity()));
        TextView tvDeviceModel = (TextView) view.findViewById(R.id.tv_device_model);
        tvDeviceModel.setText(FeedBackFragment.getDeviceName());

        TextView tvOSVersion = (TextView) view.findViewById(R.id.tv_os_version);
        tvOSVersion.setText("Android " + Build.VERSION.RELEASE);

        if (isGoogleAccountOn)
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


        TextView tvTime = (TextView) view.findViewById(R.id.tv_time);


        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        tvTime.setText(formatter.format(new Date()));
        return view;
    }




}
