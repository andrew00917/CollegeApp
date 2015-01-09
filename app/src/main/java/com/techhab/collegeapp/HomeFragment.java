package com.techhab.collegeapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.Keyframe;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.techhab.collegeapp.application.CollegeApplication;

public class HomeFragment extends Fragment implements View.OnTouchListener {

    // Store the Application (as you can't always get to it when you can't access the Activity - e.g. during rotations)
    private CollegeApplication application;

    // FrameLayout of the progressContainer
    private FrameLayout progressContainer;

    // Views
    private View v;
    private LinearLayout mainContainer;
    private ImageView mainExpand;
    private boolean isExpanded;

    private TextView main00;
    private TextView main01;
    private TextView main02;
    private TextView main10;
    private TextView main11;
    private TextView main12;

    // View id
    private static final int MAIN_EXPAND_ID = R.id.main_expand;
    private static final int MAIN_00_ID = R.id.main_menu_00;
    private static final int MAIN_01_ID = R.id.main_menu_01;
    private static final int MAIN_02_ID = R.id.main_menu_02;
    private static final int MAIN_10_ID = R.id.main_menu_10;
    private static final int MAIN_11_ID = R.id.main_menu_11;
    private static final int MAIN_12_ID = R.id.main_menu_12;

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
        progressContainer.setVisibility(FrameLayout.INVISIBLE);

        // Set banner image according to current time in timezone
//        setBanner(v);

        mainContainer = (LinearLayout) v.findViewById(R.id.main_container);
        mainExpand = (ImageView) v.findViewById(R.id.main_expand);
        isExpanded = false;

        main00 = (TextView) v.findViewById(R.id.main_menu_00);
        main01 = (TextView) v.findViewById(R.id.main_menu_01);
        main02 = (TextView) v.findViewById(R.id.main_menu_02);
        main10 = (TextView) v.findViewById(R.id.main_menu_10);
        main11 = (TextView) v.findViewById(R.id.main_menu_11);
        main12 = (TextView) v.findViewById(R.id.main_menu_12);

        mainExpand.setOnTouchListener(this);

        main00.setOnTouchListener(this);
        main01.setOnTouchListener(this);
        main02.setOnTouchListener(this);
        main10.setOnTouchListener(this);
        main11.setOnTouchListener(this);
        main12.setOnTouchListener(this);

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
                    case MAIN_02_ID:
                        intent = new Intent(getActivity(), FoodActivity.class);
                        break;
                    case MAIN_10_ID:
                        intent = new Intent(getActivity(), AthleticActivity.class);
                        break;
                    case MAIN_11_ID:
                        intent = new Intent(getActivity(), EventsActivity.class);
                        break;
                    case MAIN_12_ID:
                        intent = new Intent(getActivity(), NewsActivity.class);
                        break;
                    case MAIN_EXPAND_ID:
                        if (isExpanded) {
                            com.techhab.collegeapp.Animation animation
                                    = new com.techhab.collegeapp.Animation(mainContainer
                                    , ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                                    , true);
                            animation.setDuration(300);
                            mainContainer.startAnimation(animation);
//                            mainContainer.setLayoutParams(new FrameLayout
//                                    .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                                    ViewGroup.LayoutParams.MATCH_PARENT));
                            mainContainer.setBackgroundColor(getResources().getColor(R.color.main00));
                            setText();
                            expandToolbar();
                            mainExpand.setImageDrawable(getResources().getDrawable(R.drawable.main_expand_less));
                            isExpanded = false;
                        } else {
                            com.techhab.collegeapp.Animation animation
                                    = new com.techhab.collegeapp.Animation(mainContainer
                                    , ViewGroup.LayoutParams.MATCH_PARENT, mainExpand.getHeight() + main00.getHeight()
                                    , false);
                            animation.setDuration(300);
                            mainContainer.startAnimation(animation);
//                            mainContainer.setLayoutParams(new FrameLayout
//                                    .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                                    mainExpand.getHeight() + main00.getHeight()));
                            mainContainer.setBackgroundColor(getResources().getColor(R.color.main00_trans));
                            nullifyText();
                            lessToolbar();
                            mainExpand.setImageDrawable(getResources().getDrawable(R.drawable.main_expand_more));
                            isExpanded = true;
                        }
                        break;
                }
                if (intent != null) {
                    getActivity().startActivity(intent);
                }
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
        view.startAnimation(animation);
    }

    /**
     * Button released method
     *
     * @param view button
     */
    private void buttonReleased(View view) {
        Animation animation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.button_released);
        view.startAnimation(animation);
    }

    private void nullifyText() {
        main00.setText("");
        main01.setText("");
        main02.setText("");
        main10.setText("");
        main11.setText("");
        main12.setText("");
    }

    private void setText() {
        main00.setText(getResources().getString(R.string.main_menu_00));
        main01.setText(getResources().getString(R.string.main_menu_01));
        main02.setText(getResources().getString(R.string.main_menu_02));
        main10.setText(getResources().getString(R.string.main_menu_10));
        main11.setText(getResources().getString(R.string.main_menu_11));
        main12.setText(getResources().getString(R.string.main_menu_12));
    }

    private void lessToolbar() {
        ((HomeActivity) getActivity()).setActionBarLess();
    }

    private void expandToolbar() {
        ((HomeActivity) getActivity()).setActionBarExpand();
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
