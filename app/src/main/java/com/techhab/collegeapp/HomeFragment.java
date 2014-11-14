package com.techhab.collegeapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.Keyframe;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class HomeFragment extends Fragment implements View.OnTouchListener {

    // Store the Application (as you can't always get to it when you can't access the Activity - e.g. during rotations)
    private CollegeApplication application;

    // FrameLayout of the progressContainer
    private FrameLayout progressContainer;

    // Views
    private View v;
    private ImageButton main00;
    private ImageButton main01;
    private ImageButton main10;
    private ImageButton main11;
    private ImageButton main20;
    private ImageButton main21;

    // View id
    private static final int MAIN_00_ID = R.id.main_menu_00;
    private static final int MAIN_01_ID = R.id.main_menu_01;
    private static final int MAIN_10_ID = R.id.main_menu_10;
    private static final int MAIN_11_ID = R.id.main_menu_11;
    private static final int MAIN_20_ID = R.id.main_menu_20;
    private static final int MAIN_21_ID = R.id.main_menu_21;

    // SharedPreferences (for settings)
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

        progressContainer = (FrameLayout)v.findViewById(R.id.progress_container);
        // Hide the progressContainer
        progressContainer.setVisibility(View.INVISIBLE);

        // Set banner image according to current time in timezone
//        setBanner(v);

        main00 = (ImageButton) v.findViewById(R.id.main_menu_00);
        main01 = (ImageButton) v.findViewById(R.id.main_menu_01);
        main10 = (ImageButton) v.findViewById(R.id.main_menu_10);
        main11 = (ImageButton) v.findViewById(R.id.main_menu_11);
        main20 = (ImageButton) v.findViewById(R.id.main_menu_20);
        main21 = (ImageButton) v.findViewById(R.id.main_menu_21);

        main00.setOnTouchListener(this);
        main01.setOnTouchListener(this);
        main10.setOnTouchListener(this);
        main11.setOnTouchListener(this);
        main20.setOnTouchListener(this);
        main21.setOnTouchListener(this);

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
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                buttonPressed(view);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                buttonReleased(view);
                break;
            case MotionEvent.ACTION_UP:
                buttonReleased(view);
                Intent intent = null;
                switch (view.getId()) {
                    case MAIN_00_ID:
                        intent = new Intent(getActivity(), AcademicActivity.class);
                        break;
                    case MAIN_01_ID:
                        intent = new Intent(getActivity(), CampusActivity.class);
                        break;
                    case MAIN_10_ID:
                        intent = new Intent(getActivity(), FoodActivity.class);
                        break;
                    case MAIN_11_ID:
                        intent = new Intent(getActivity(), AthleticActivity.class);
                        break;
                    case MAIN_20_ID:
                        intent = new Intent(getActivity(), EventsActivity.class);
                        break;
                    case MAIN_21_ID:
                        intent = new Intent(getActivity(), NewsActivity.class);
                        break;
                }
                getActivity().startActivity(intent);
                break;
        }
        return true;
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

//    public boolean allowBackPressed() {
//        return ! isSubmenuShowing;
//    }

    /**
     * Set Banner according to current time
     *
     * @param view Fragment view
     */
    /*private void setBanner(View view) {
        Time time = new Time(Time.getCurrentTimezone());
        time.setToNow();
        ImageView banner = (ImageView) view.findViewById(R.id.bannerImage);
        int currentTime = Integer.parseInt(time.format("%H"));
        if (currentTime >= 6 && currentTime < 11) {
            banner.setBackgroundResource(R.drawable.k_banner_day);
        }
        else if (currentTime >= 11 && currentTime < 16) {
            banner.setBackgroundResource(R.drawable.k_banner_flags);
        }
        else if (currentTime >= 16 && currentTime < 21) {
            banner.setBackgroundResource(R.drawable.k_banner_fab);
        }
        else {
            banner.setBackgroundResource(R.drawable.k_banner_night);
        }
    }*/

    /**
     * Button pressed method
     *
     * @param view button
     */
    private void buttonPressed(View view) {
        Animation animation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.button_pressed);
        switch (view.getId()) {
            case MAIN_00_ID:
                main00.startAnimation(animation);
                break;
            case MAIN_01_ID:
                main01.startAnimation(animation);
                break;
            case MAIN_10_ID:
                main10.startAnimation(animation);
                break;
            case MAIN_11_ID:
                main11.startAnimation(animation);
                break;
            case MAIN_20_ID:
                main20.startAnimation(animation);
                break;
            case MAIN_21_ID:
                main21.startAnimation(animation);
                break;
        }
    }

    /**
     * Button released method
     *
     * @param view button
     */
    private void buttonReleased(View view) {
        Animation animation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.button_released);
        switch (view.getId()) {
            case MAIN_00_ID:
                main00.startAnimation(animation);
                break;
            case MAIN_01_ID:
                main01.startAnimation(animation);
                break;
            case MAIN_10_ID:
                main10.startAnimation(animation);
                break;
            case MAIN_11_ID:
                main11.startAnimation(animation);
                break;
            case MAIN_20_ID:
                main20.startAnimation(animation);
                break;
            case MAIN_21_ID:
                main21.startAnimation(animation);
                break;
        }
    }

    /**
     * Toggle back to main menus
     */
//    public void toggle(ListView submenu) {
//        if (submenu.getVisibility() == ListView.VISIBLE && isSubmenuShowing) {
//            submenu.setVisibility(ListView.GONE);
//            isSubmenuShowing = false;
//        }
//        else if (submenu.getVisibility() == ListView.VISIBLE) {
//            isSubmenuShowing = true;
//        }
//    }

    /**
     * Toggle up and down animation button for sub menu
     */
//    public void toggleMenu(View view, ListView submenu) {
//        isSubmenuShowing = true;
//
//        // set up submenu list view
//        final String[] values;
//        // TODO
//        // change array id if id of array is changed in string resource file
//        // otherwise do nothing in this method
//        switch (view.getId()) {
//            case MAIN_00_ID:
//                values = getResources().getStringArray(R.array.sub_menu_00);
//                break;
//            case MAIN_01_ID:
//                values = getResources().getStringArray(R.array.campus_info);
//                break;
//            case MAIN_10_ID:
//                values = getResources().getStringArray(R.array.sub_menu_10);
//                break;
//            case MAIN_11_ID:
//                values = getResources().getStringArray(R.array.sub_menu_11);
//                break;
//            case MAIN_20_ID:
//                values = getResources().getStringArray(R.array.sub_menu_20);
//                break;
//            case MAIN_21_ID:
//                values = getResources().getStringArray(R.array.sub_menu_21);
//                break;
//            default:
//                values = new String[]{};
//                break;
//        }
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity()
//                , android.R.layout.simple_list_item_1, android.R.id.text1, values);
//        submenu.setAdapter(adapter);
//
//        final Intent subIntent = new Intent(getActivity(), InfoActivity.class);
//        submenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, final View view,
//                                    int position, long id) {
//                final String item = (String) parent.getItemAtPosition(position);
//
//                // default key is ""
//                String key = "";
//                for (int i = 0; i < values.length; i++) {
//                    if (item.equals(values[i])) {
//                        key = values[i].toLowerCase();
//                    }
//                }
//                if (key.equals("campus map")) {
//                    Intent intent = new Intent(getActivity(), MapsActivity.class);
//                    getActivity().startActivity(intent);
//                }
//                else {
//                    subIntent.putExtra("position", key);
//                    startActivity(subIntent);
//                }
//            }
//        });
//        submenu.setVisibility(ListView.VISIBLE);
//    }


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
