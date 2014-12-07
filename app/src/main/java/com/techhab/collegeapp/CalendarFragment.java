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

public class CalendarFragment extends Fragment implements View.OnClickListener{
    public static final String ARG_OBJECT = "object";
    // Buttons Calendar
    private RelativeLayout btn_calendar_2015;
    private RelativeLayout btn_calendar_2016;
    private RelativeLayout btn_calendar_2017;
    View v;


    public static Fragment createNewIntace() {
        CalendarFragment fragment = new CalendarFragment();
        Bundle arg = new Bundle();
        fragment.setArguments(arg);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_calender, parent, false);
        btn_calendar_2015 = (RelativeLayout) v.findViewById(R.id.btn_calendar_2015);
        btn_calendar_2015.setOnClickListener(this);
        btn_calendar_2016 = (RelativeLayout) v.findViewById(R.id.btn_calendar_2016);
        btn_calendar_2016.setOnClickListener(this);
        btn_calendar_2017 = (RelativeLayout) v.findViewById(R.id.btn_calendar_2017);
        btn_calendar_2017.setOnClickListener(this);
        // Example of getting Button view:
        // Button b = (Button) v.findViewById(R.id.BUTTON_ID);
    return v;
    }

        @Override
        public void onClick (View v){

            //Intent intent = new Intent(getActivity(), );

            switch (v.getId()) {
                case R.id.btn_calendar_2015:
                    Intent intent = new Intent(getActivity(), TermActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btn_calendar_2016:
                    Intent intent1 = new Intent(getActivity(), TermActivity.class);
                    startActivity(intent1);
                    break;

                case R.id.btn_calendar_2017:
                    Intent intent2 = new Intent(getActivity(), TermActivity.class);
                    startActivity(intent2);
                    break;




            }



    }
}