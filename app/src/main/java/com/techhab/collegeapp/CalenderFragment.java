package com.techhab.collegeapp;



import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class CalenderFragment extends Fragment {

    private CollegeApplication application;

    private Context context;

    View v;

    private ImageView container;

    private TextView year;
    private TextView term;
    private TextView content;
    private TextView date;

    private ImageView upArrow;
    private ImageView bottomArrow;
    private ImageView leftArrow;
    private ImageView rightArrow;

    protected MotionEvent downStart = null;
    private boolean horizontalVelocity = false;
    private boolean verticalVelocity = false;
    private float deltaX;
    private float deltaY;

    private boolean isSwipe = false;
    private boolean isTop = true;
    private boolean isBottom = false;
    private boolean isLeft = true;
    private boolean isRight = false;

    public CalenderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

                application = (CollegeApplication) getActivity().getApplication();

        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_calender, parent, false);

        year = (TextView) v.findViewById(R.id.calender_academic_year);
        term = (TextView) v.findViewById(R.id.calender_term);
        content = (TextView) v.findViewById(R.id.calender_content);
        date = (TextView) v.findViewById(R.id.calender_date);

        upArrow = (ImageView) v.findViewById(R.id.up_arrow);
        bottomArrow = (ImageView) v.findViewById(R.id.bottom_arrow);
        leftArrow = (ImageView) v.findViewById(R.id.left_arrow);
        rightArrow = (ImageView) v.findViewById(R.id.right_arrow);

        container = (ImageView) v.findViewById(R.id.calender_container);
        container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // keep track of the starting down-event
                        downStart = MotionEvent.obtain(event);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        deltaX = event.getX() - downStart.getX();
                        deltaY = event.getY() - downStart.getY();

                        // if moved more than slop*2, capture the event for ourselves
                        horizontalVelocity = Math.abs(deltaX) > ViewConfiguration.get(context).getScaledTouchSlop() * 2;
                        verticalVelocity = Math.abs(deltaY) > ViewConfiguration.get(context).getScaledTouchSlop() * 2;

                        if ( ! isSwipe) {
                            if (horizontalVelocity && deltaX < 0) {
                                // if deltaX is negative, user have swiped to left. Show content on right
                                showContentRight();
                                return true;
                            } else if (horizontalVelocity && deltaX > 0) {
                                // if deltaX is positive, user have swiped to right. Show content on left
                                showContentLeft();
                                return true;
                            } else if (verticalVelocity && deltaY < 0) {
                                // if deltaY is negative, user have swiped to up. Show content on bottom
                                showContentBottom();
                                return true;
                            } else if (verticalVelocity && deltaY > 0) {
                                // if deltaY is positive, user have swiped to bottom. Show content on up
                                showContentUp();
                                return true;
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        isSwipe = false;
                        break;
                }
                return true;
            }
        });

        return v;
    }

    private void showContentUp() {
        isSwipe = true;
        if ( ! isTop) {
            // keep year same and change everything else

            // current year should be either 14, 15, or 16
            int currentYear = Integer.parseInt("" + year.getText().charAt(2) + year.getText().charAt(3));

            String currentTerm = term.getText().toString();
            if (currentTerm.equals("Spring Term")) {
                term.setText(R.string.calender_term_winter);
                content.setText(R.string.winter_term_content);

                switch (currentYear) {
                    case 14:
                        date.setText(R.string.winter_term_2014_15_dates);
                        break;
                    case 15:
                        date.setText(R.string.winter_term_2015_16_dates);
                        break;
                    case 16:
                        date.setText(R.string.winter_term_2016_17_dates);
                        break;
                }

                bottomArrow.setVisibility(ImageView.VISIBLE);
                isBottom = false;
            }
            else if (currentTerm.equals("Winter Term")) {
                term.setText(R.string.calender_term_fall);
                content.setText(R.string.fall_term_content);

                switch (currentYear) {
                    case 14:
                        date.setText(R.string.fall_term_2014_15_dates);
                        break;
                    case 15:
                        date.setText(R.string.fall_term_2015_16_dates);
                        break;
                    case 16:
                        date.setText(R.string.fall_term_2016_17_dates);
                        break;
                }

                upArrow.setVisibility(ImageView.INVISIBLE);
                isTop = true;
            }
        }
    }

    private void showContentBottom() {
        isSwipe = true;
        if ( ! isBottom) {
            // keep year same and change everything else

            // current year should be either 14, 15, or 16
            int currentYear = Integer.parseInt("" + year.getText().charAt(2) + year.getText().charAt(3));

            String currentTerm = term.getText().toString();
            if (currentTerm.equals("Fall Term")) {
                term.setText(R.string.calender_term_winter);
                content.setText(R.string.winter_term_content);

                switch (currentYear) {
                    case 14:
                        date.setText(R.string.winter_term_2014_15_dates);
                        break;
                    case 15:
                        date.setText(R.string.winter_term_2015_16_dates);
                        break;
                    case 16:
                        date.setText(R.string.winter_term_2016_17_dates);
                        break;
                }

                upArrow.setVisibility(ImageView.VISIBLE);
                isTop = false;
            }
            else if (currentTerm.equals("Winter Term")) {
                term.setText(R.string.calender_term_spring);
                content.setText(R.string.spring_term_content);

                switch (currentYear) {
                    case 14:
                        date.setText(R.string.spring_term_2014_15_dates);
                        break;
                    case 15:
                        date.setText(R.string.spring_term_2015_16_dates);
                        break;
                    case 16:
                        date.setText(R.string.spring_term_2016_17_dates);
                        break;
                }

                bottomArrow.setVisibility(ImageView.INVISIBLE);
                isBottom = true;
            }
        }
    }

    private void showContentLeft() {
        isSwipe = true;
        if ( ! isLeft) {
            // keep content and term same, change everything else

            // current year should be either 14, 15, or 16
            int currentYear = Integer.parseInt("" + year.getText().charAt(2) + year.getText().charAt(3));
            String currentTerm = term.getText().toString();

            switch (currentYear) {
                case 15:
                    year.setText(R.string.academic_year_2014_15);
                    if (currentTerm.equals("Fall Term")) {
                        date.setText(R.string.fall_term_2014_15_dates);
                    }
                    else if (currentTerm.equals("Winter Term")) {
                        date.setText(R.string.winter_term_2014_15_dates);
                    }
                    else if (currentTerm.equals("Spring Term")) {
                        date.setText(R.string.spring_term_2014_15_dates);
                    }

                    leftArrow.setVisibility(ImageView.INVISIBLE);
                    isLeft = true;
                    break;
                case 16:
                    year.setText(R.string.academic_year_2015_16);
                    if (currentTerm.equals("Fall Term")) {
                        date.setText(R.string.fall_term_2015_16_dates);
                    }
                    else if (currentTerm.equals("Winter Term")) {
                        date.setText(R.string.winter_term_2015_16_dates);
                    }
                    else if (currentTerm.equals("Spring Term")) {
                        date.setText(R.string.spring_term_2015_16_dates);
                    }

                    rightArrow.setVisibility(ImageView.VISIBLE);
                    isRight = false;
                    break;
            }
        }
    }

    private void showContentRight() {
        isSwipe = true;
        if ( ! isRight) {
            // keep content and term same, change everything else

            // current year should be either 14, 15, or 16
            int currentYear = Integer.parseInt("" + year.getText().charAt(2) + year.getText().charAt(3));
            String currentTerm = term.getText().toString();

            switch (currentYear) {
                case 14:
                    year.setText(R.string.academic_year_2015_16);
                    if (currentTerm.equals("Fall Term")) {
                        date.setText(R.string.fall_term_2015_16_dates);
                    }
                    else if (currentTerm.equals("Winter Term")) {
                        date.setText(R.string.winter_term_2015_16_dates);
                    }
                    else if (currentTerm.equals("Spring Term")) {
                        date.setText(R.string.spring_term_2015_16_dates);
                    }

                    leftArrow.setVisibility(ImageView.VISIBLE);
                    isLeft = false;
                    break;
                case 15:
                    year.setText(R.string.academic_year_2016_17);
                    if (currentTerm.equals("Fall Term")) {
                        date.setText(R.string.fall_term_2016_17_dates);
                    }
                    else if (currentTerm.equals("Winter Term")) {
                        date.setText(R.string.winter_term_2016_17_dates);
                    }
                    else if (currentTerm.equals("Spring Term")) {
                        date.setText(R.string.spring_term_2016_17_dates);
                    }

                    rightArrow.setVisibility(ImageView.INVISIBLE);
                    isRight = true;
                    break;
            }
        }
    }

}
