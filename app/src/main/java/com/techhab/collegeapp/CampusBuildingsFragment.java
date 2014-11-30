package com.techhab.collegeapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.techhab.collegeapp.R;


public class CampusBuildingsFragment extends Fragment {

    public static final String ARG_OBJECT = "object";

    public CampusBuildingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_campus_buildings,
                container, false);
        TextView hicksCenter = (TextView) view.findViewById(R.id.label_hicks_center);
        hicksCenter.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                getActivity().startActivity(intent);
            }

        });
        return view;
    }


}
