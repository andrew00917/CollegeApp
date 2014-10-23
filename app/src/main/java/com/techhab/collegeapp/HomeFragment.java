package com.techhab.collegeapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

public class HomeFragment extends Fragment {

    // Store the Application (as you can't always get to it when you can't access the Activity - e.g. during rotations)
    private CollegeApplication application;

    // FrameLayout of the progressContainer
    private FrameLayout progressContainer;

    // Views
    private View v;
    private ImageView main00;
    private ImageView main01;
    private ImageView main10;
    private ImageView main11;
    private ImageView main20;
    private ImageView main21;

    // View id
    private static final int MAIN_00_ID = R.id.main_menu_00;
    private static final int MAIN_01_ID = R.id.main_menu_01;
    private static final int MAIN_10_ID = R.id.main_menu_10;
    private static final int MAIN_11_ID = R.id.main_menu_11;
    private static final int MAIN_20_ID = R.id.main_menu_20;
    private static final int MAIN_21_ID = R.id.main_menu_21;

    // SharedPreferences
    private String spKey;
    private SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if ( getActivity().getActionBar() != null && ! getActivity().getActionBar().isShowing()) {
            getActivity().getActionBar().show();
        }

        application = (CollegeApplication) getActivity().getApplication();
        spKey = application.getLoggedInKey();
        application.load();
    }

    @SuppressWarnings("unused")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_home, parent, false);


        progressContainer = (FrameLayout)v.findViewById(R.id.progress_container);
        // Hide the progressContainer
        progressContainer.setVisibility(View.INVISIBLE);

        View.OnTouchListener listener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch(view.getId()) {
                    case MAIN_00_ID:
                        Toast.makeText(getActivity(), "Main Menu 00 clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case MAIN_01_ID:
                        Toast.makeText(getActivity(), "Main Menu 01 clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case MAIN_10_ID:
                        Toast.makeText(getActivity(), "Main Menu 10 clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case MAIN_11_ID:
                        Toast.makeText(getActivity(), "Main Menu 11 clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case MAIN_20_ID:
                        Toast.makeText(getActivity(), "Main Menu 20 clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case MAIN_21_ID:
                        Toast.makeText(getActivity(), "Main Menu 21 clicked", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        };

        main00 = (ImageView) v.findViewById(R.id.main_menu_00);
        main00.setOnTouchListener(listener);
        main01 = (ImageView) v.findViewById(R.id.main_menu_01);
        main01.setOnTouchListener(listener);
        main10 = (ImageView) v.findViewById(R.id.main_menu_10);
        main10.setOnTouchListener(listener);
        main11 = (ImageView) v.findViewById(R.id.main_menu_11);
        main11.setOnTouchListener(listener);
        main20 = (ImageView) v.findViewById(R.id.main_menu_20);
        main20.setOnTouchListener(listener);
        main21 = (ImageView) v.findViewById(R.id.main_menu_21);
        main21.setOnTouchListener(listener);

        // Restore the state
        restoreState(savedInstanceState);

        return v;
    }

    // Restores the state during onCreateView
    private void restoreState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // TODO
            //pendingPost = savedInstanceState.getBoolean(PENDING_POST_KEY, false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // TODO
        //outState.putBoolean(PENDING_POST_KEY, pendingPost);
    }


}
