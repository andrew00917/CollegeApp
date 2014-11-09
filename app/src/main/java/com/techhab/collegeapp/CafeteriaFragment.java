package com.techhab.collegeapp;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;




public class CafeteriaFragment extends Fragment {

    View v;

    public CafeteriaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_cafeteria, parent, false);

        // Example of getting Button view:
        // Button b = (Button) v.findViewById(R.id.BUTTON_ID);

        return v;
    }


}
