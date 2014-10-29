package com.techhab.collegeapp;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FacilitiesFragment extends Fragment {

    View v;

    public FacilitiesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_facilities, parent, false);

        // Example of getting Button view:
        // Button b = (Button) v.findViewById(R.id.BUTTON_ID);

        return v;
    }


}
