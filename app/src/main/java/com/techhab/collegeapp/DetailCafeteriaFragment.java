package com.techhab.collegeapp;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;


public class DetailCafeteriaFragment extends Fragment implements View.OnClickListener {
    public static final String ARG_OBJECT = "object";
    // Buttons Cafeteria
    private RelativeLayout btn_cafeteria_phone;
    private RelativeLayout btn_cafeteria_richardson;
    private RelativeLayout btn_cafeteria_welles;
    View v;

    private static final int RICHARDSON = 1;

    public static Fragment createNewIntace() {
        DetailCafeteriaFragment fragment = new DetailCafeteriaFragment();
        Bundle arg = new Bundle();
        fragment.setArguments(arg);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_cafeteria, parent, false);
        btn_cafeteria_phone = (RelativeLayout) v.findViewById(R.id.btn_cafeteria_phone);
        btn_cafeteria_phone.setOnClickListener(this);
        btn_cafeteria_richardson = (RelativeLayout) v.findViewById(R.id.btn_cafeteria_richardson);
        btn_cafeteria_richardson.setOnClickListener(this);
        btn_cafeteria_welles = (RelativeLayout) v.findViewById(R.id.btn_cafeteria_welles);
        btn_cafeteria_welles.setOnClickListener(this);
        // Example of getting Button view:
        // Button b = (Button) v.findViewById(R.id.BUTTON_ID);
        TextView richardsonStoreStatus = (TextView) v.findViewById(R.id.tvRichardsonCafeStatus);
        TextView wellesStoreStatus = (TextView) v.findViewById(R.id.tvWellesCafeStatus);
        TextView bookCafeStatus = (TextView) v.findViewById(R.id.tvBookCafeStatus);
        if (!isOpened())
        {
            richardsonStoreStatus.setText("Closed");
            wellesStoreStatus.setText("Closed");
            bookCafeStatus.setText("Closed");
            richardsonStoreStatus.setTextColor(getResources().getColor(R.color.red));
            wellesStoreStatus.setTextColor(getResources().getColor(R.color.red));
            bookCafeStatus.setTextColor(getResources().getColor(R.color.red));
        }

        return v;
    }

    private boolean isOpened()
    {
        Long currentTime = System.currentTimeMillis();
        Calendar openCalendar = Calendar.getInstance();
        openCalendar.set(Calendar.HOUR_OF_DAY,8);
        openCalendar.set(Calendar.MINUTE,0);
        openCalendar.set(Calendar.SECOND, 0);
        long openTime = openCalendar.getTimeInMillis();
        Calendar closeCalendar = Calendar.getInstance();
        closeCalendar.set(Calendar.HOUR_OF_DAY, 22);
        closeCalendar.set(Calendar.MINUTE, 0);
        closeCalendar.set(Calendar.SECOND, 0);
        long closedTIme = closeCalendar.getTimeInMillis();
        if (currentTime >= openTime && currentTime <= closedTIme)
            return true;
        return false;
    }

    @Override
    public void onClick(View v) {

        //Intent intent = new Intent(getActivity(), );
        String phoneNumber = null;
        switch (v.getId()) {
            // Phone Call
            case R.id.btn_cafeteria_phone:
                phoneNumber = getText(R.string.label_cafeteria_phone).toString();
                PhoneCall(phoneNumber);
                break;
            case R.id.btn_cafeteria_welles:
                Intent intent = new Intent(getActivity(), CafeteriaActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_cafeteria_richardson:
                Intent intent1 = new Intent(getActivity(), CafeteriaActivity.class);
                startActivity(intent1);
                break;


        }
    }
    private void PhoneCall(String phoneNumber) {
        Intent phoneIntent = new Intent(Intent.ACTION_CALL);
        phoneIntent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(phoneIntent);
    }


}
