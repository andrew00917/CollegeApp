package com.techhab.collegeapp;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class LoggedOutHomeFragment extends Fragment {

    View progressContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_logged_out_home, parent, false);

        progressContainer = v.findViewById(R.id.progressContainer);
        progressContainer.setVisibility(View.INVISIBLE);

        // set buttons here
        // remember me check button and login button etc.

        return v;
    }
}
