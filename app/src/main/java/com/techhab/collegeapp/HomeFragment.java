package com.techhab.collegeapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.Keyframe;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

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

    // Display check
    private boolean isLongPressed = false;
    private boolean isSubmenuShowing = false;

    // SharedPreferences
    private String spKey;
    private SharedPreferences prefs;

    // Animation attributes
    private LayoutTransition mTransitioner;
    private ViewGroup container;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        application = (CollegeApplication) getActivity().getApplication();
        spKey = application.getLoggedInKey();
        application.load();

        container = new LinearLayout(getActivity());
        container.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        resetTransition();
    }

    @SuppressWarnings("unused")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_home, parent, false);

        if ( getActivity().getActionBar() != null && ! getActivity().getActionBar().isShowing()) {
            getActivity().getActionBar().show();
        }

        progressContainer = (FrameLayout)v.findViewById(R.id.progress_container);
        // Hide the progressContainer
        progressContainer.setVisibility(View.INVISIBLE);

        View.OnTouchListener touchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                boolean defaultResult = view.onTouchEvent(event);

                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        switch (view.getId()) {
                            case MAIN_00_ID:
                                main00.setImageDrawable(getResources().getDrawable(R.drawable.money_pressed));
                                break;
                            case MAIN_01_ID:
                                main01.setImageDrawable(getResources().getDrawable(R.drawable.graph_pressed));
                                break;
                            case MAIN_10_ID:
                                main10.setImageDrawable(getResources().getDrawable(R.drawable.docs_pressed));
                                break;
                            case MAIN_11_ID:
                                main11.setImageDrawable(getResources().getDrawable(R.drawable.location_pressed));
                                break;
                            case MAIN_20_ID:
                                main20.setImageDrawable(getResources().getDrawable(R.drawable.payments_pressed));
                                break;
                            case MAIN_21_ID:
                                main21.setImageDrawable(getResources().getDrawable(R.drawable.dots_pressed));
                                break;
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_OUTSIDE:
                        switch (view.getId()) {
                            case MAIN_00_ID:
                                main00.setImageDrawable(getResources().getDrawable(R.drawable.money));
                                break;
                            case MAIN_01_ID:
                                main01.setImageDrawable(getResources().getDrawable(R.drawable.graph));
                                break;
                            case MAIN_10_ID:
                                main10.setImageDrawable(getResources().getDrawable(R.drawable.docs));
                                break;
                            case MAIN_11_ID:
                                main11.setImageDrawable(getResources().getDrawable(R.drawable.location));
                                break;
                            case MAIN_20_ID:
                                main20.setImageDrawable(getResources().getDrawable(R.drawable.payments));
                                break;
                            case MAIN_21_ID:
                                main21.setImageDrawable(getResources().getDrawable(R.drawable.dots));
                                break;
                        }
                        return false;
                    case MotionEvent.ACTION_UP:
                        switch (view.getId()) {
                            case MAIN_00_ID:
                                main00.setImageDrawable(getResources().getDrawable(R.drawable.money));
                                break;
                            case MAIN_01_ID:
                                main01.setImageDrawable(getResources().getDrawable(R.drawable.graph));
                                break;
                            case MAIN_10_ID:
                                main10.setImageDrawable(getResources().getDrawable(R.drawable.docs));
                                break;
                            case MAIN_11_ID:
                                main11.setImageDrawable(getResources().getDrawable(R.drawable.location));
                                break;
                            case MAIN_20_ID:
                                main20.setImageDrawable(getResources().getDrawable(R.drawable.payments));
                                break;
                            case MAIN_21_ID:
                                main21.setImageDrawable(getResources().getDrawable(R.drawable.dots));
                                break;
                        }
                        if ( ! isLongPressed) {
                            toggleMenu(view);
                        }
                        break;
                    default:
                        return defaultResult;
                }
                return true;
            }
        };

        View.OnLongClickListener longClickInfoListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                isLongPressed = true;

                Intent intent = new Intent(getActivity(), InfoActivity.class);
                intent.putExtra("position", "");
                getActivity().startActivity(intent);
                return true;
            }
        };

        main00 = (ImageView) v.findViewById(R.id.main_menu_00);
        main00.setOnTouchListener(touchListener);
        //main00.setOnLongClickListener(longClickListener);

        main01 = (ImageView) v.findViewById(R.id.main_menu_01);
        main01.setOnTouchListener(touchListener);
        //main01.setOnLongClickListener(longClickListener);

        main10 = (ImageView) v.findViewById(R.id.main_menu_10);
        main10.setOnTouchListener(touchListener);
        //main10.setOnLongClickListener(longClickListener);

        main11 = (ImageView) v.findViewById(R.id.main_menu_11);
        main11.setOnTouchListener(touchListener);
        main11.setOnLongClickListener(longClickInfoListener);

        main20 = (ImageView) v.findViewById(R.id.main_menu_20);
        main20.setOnTouchListener(touchListener);
        //main20.setOnLongClickListener(longClickListener);

        main21 = (ImageView) v.findViewById(R.id.main_menu_21);
        main21.setOnTouchListener(touchListener);
        //main21.setOnLongClickListener(longClickListener);

        ImageView submenuToggle = (ImageView) v.findViewById(R.id.main_menu_holder);
        submenuToggle.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 toggle();
             }
        });
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
        isLongPressed = false;
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

    /**
     * Toggle back to main menus
     */
    public void toggle() {
        LinearLayout submenuContainer = (LinearLayout) v.findViewById(R.id.sub_menu_container);
        if (submenuContainer.getVisibility() == LinearLayout.VISIBLE && isSubmenuShowing) {
            submenuContainer.setVisibility(LinearLayout.INVISIBLE);
            isSubmenuShowing = false;
        }
    }

    /**
     * Toggle up and down animation button for sub menu
     */
    public void toggleMenu(View view) {
        long duration;
        mTransitioner.setStagger(LayoutTransition.CHANGE_APPEARING, 30);
        mTransitioner.setStagger(LayoutTransition.CHANGE_DISAPPEARING, 30);
        setupCustomAnimations();
        duration = 500;
        mTransitioner.setDuration(duration);

        LinearLayout submenuContainer = (LinearLayout) v.findViewById(R.id.sub_menu_container);
        if (submenuContainer.getVisibility() == LinearLayout.VISIBLE && isSubmenuShowing) {
            submenuContainer.setVisibility(LinearLayout.INVISIBLE);
            isSubmenuShowing = false;
        }
        else {
            submenuContainer.setVisibility(LinearLayout.VISIBLE);
            isSubmenuShowing = true;
            ImageView mainmenuHolder = (ImageView) v.findViewById(R.id.main_menu_holder);
            ListView submenu = (ListView) v.findViewById(R.id.sub_menu_list);
            ImageView buttonClicked = (ImageView) v.findViewById(view.getId());

            // set up main menu holder
            mainmenuHolder.setImageDrawable(buttonClicked.getDrawable());

            // set up submenu list view
            final String[] values;
            // TODO
            // change array id if id of array is changed in string resource file
            // otherwise do nothing in this method
            switch (view.getId()) {
                case MAIN_00_ID:
                    values = getResources().getStringArray(R.array.sub_menu_00);
                    break;
                case MAIN_01_ID:
                    values = getResources().getStringArray(R.array.sub_menu_01);
                    break;
                case MAIN_10_ID:
                    values = getResources().getStringArray(R.array.sub_menu_10);
                    break;
                case MAIN_11_ID:
                    values = getResources().getStringArray(R.array.campus_info);
                    break;
                case MAIN_20_ID:
                    values = getResources().getStringArray(R.array.sub_menu_20);
                    break;
                case MAIN_21_ID:
                    values = getResources().getStringArray(R.array.sub_menu_21);
                    break;
                default:
                    values = new String[]{};
                    break;
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity()
                    , android.R.layout.simple_list_item_1, android.R.id.text1, values);
            submenu.setAdapter(adapter);

            final Intent subIntent = new Intent(getActivity(), InfoActivity.class);
            submenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, final View view,
                                        int position, long id) {
                    final String item = (String) parent.getItemAtPosition(position);
                    // default key is ""
                    String key = "";
                    for (int i = 0; i < values.length; i++) {
                        if (item.equals(values[i])) {
                            key = values[i].toLowerCase();
                        }
                    }
                    if (key.equals("campus map")) {
                        Intent intent = new Intent(getActivity(), MapsActivity.class);
                        getActivity().startActivity(intent);
                    }
                    else {
                        subIntent.putExtra("position", key);
                        startActivity(subIntent);
                    }
                }
            });

        }
    }


    /**************************************
     * Animation methods
     **************************************/
    private void resetTransition() {
        mTransitioner = new LayoutTransition();
        container.setLayoutTransition(mTransitioner);
    }

    private void setupCustomAnimations() {
        // Changing while Adding
        PropertyValuesHolder pvhLeft = PropertyValuesHolder.ofInt("left", 0, 1);
        PropertyValuesHolder pvhTop = PropertyValuesHolder.ofInt("top", 0, 1);
        PropertyValuesHolder pvhRight = PropertyValuesHolder.ofInt("right", 0, 1);
        PropertyValuesHolder pvhBottom = PropertyValuesHolder.ofInt("bottom", 0, 1);
        PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofFloat("scaleX", 1f, 0f, 1f);
        PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofFloat("scaleY", 1f, 0f, 1f);

        final ObjectAnimator changeIn =
                ObjectAnimator.
                        ofPropertyValuesHolder(this, pvhLeft, pvhTop, pvhRight, pvhBottom, pvhScaleX, pvhScaleY).
                        setDuration(mTransitioner.getDuration(LayoutTransition.CHANGE_APPEARING));

        mTransitioner.setAnimator(LayoutTransition.CHANGE_APPEARING, changeIn);

        changeIn.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator anim) {
                View view = (View) ((ObjectAnimator) anim).getTarget();
                view.setScaleX(1f);
                view.setScaleY(1f);
            }
        });

        // Changing while Removing
        Keyframe kf0 = Keyframe.ofFloat(0f, 0f);
        Keyframe kf1 = Keyframe.ofFloat(.9999f, 360f);
        Keyframe kf2 = Keyframe.ofFloat(1f, 0f);
        PropertyValuesHolder pvhRotation = PropertyValuesHolder.ofKeyframe("rotation", kf0, kf1, kf2);
        final ObjectAnimator changeOut =
                ObjectAnimator.
                        ofPropertyValuesHolder(this, pvhLeft, pvhTop, pvhRight, pvhBottom, pvhRotation).
                        setDuration(mTransitioner.getDuration(LayoutTransition.CHANGE_DISAPPEARING));

        mTransitioner.setAnimator(LayoutTransition.CHANGE_DISAPPEARING, changeOut);

        changeOut.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator anim) {
                View view = (View) ((ObjectAnimator) anim).getTarget();
                view.setRotation(0f);
            }
        });

        // Adding
        ObjectAnimator animIn = ObjectAnimator.
                ofFloat(null, "rotationY", 90f, 0f).
                setDuration(mTransitioner.getDuration(LayoutTransition.APPEARING));

        mTransitioner.setAnimator(LayoutTransition.APPEARING, animIn);

        animIn.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator anim) {
                View view = (View) ((ObjectAnimator) anim).getTarget();
                view.setRotationY(0f);
            }
        });

        // Removing
        ObjectAnimator animOut = ObjectAnimator.
                ofFloat(null, "rotationX", 0f, 90f).
                setDuration(mTransitioner.getDuration(LayoutTransition.DISAPPEARING));

        mTransitioner.setAnimator(LayoutTransition.DISAPPEARING, animOut);

        animOut.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator anim) {
                View view = (View) ((ObjectAnimator) anim).getTarget();
                view.setRotationX(0f);
            }
        });
    }
}
