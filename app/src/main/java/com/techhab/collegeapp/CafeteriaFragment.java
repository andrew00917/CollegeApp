package com.techhab.collegeapp;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.*;


public class CafeteriaFragment extends Fragment {

    public static final String ARG_OBJECT = "object";
    private View v;
    private TextView timeLeftText, noMoreMeals;
    private ImageView statusBarArrow;
    private LinearLayout statusBarExtendedInfoFrame;
    private RecyclerView mealsRecyclerView;
    private LinearLayout statusBar;
    private RecyclerView.LayoutManager mLayoutManager;
    private Spinner daySpinner;
    private boolean isExpanded = false;
    private Calendar mCalender;
    private int mCurrentTime;
    private int currentDayNum;
    private boolean isOpen;
    private List<Meal> meals = new ArrayList<>();
    private boolean allMealsAreOver = false;

    private final int breakfastOpenTimeWeekday = 730;
    private final int breakfastCloseTimeWeekday = 1000;
    private final int breakfastOpenTimeSaturday = 930;
    private final int breakfastCloseTimeSaturday = 1100;

    private final int brunchOpenTimeSatOrSun = 1115;
    private final int brunchCloseTimeSatOrSun = 1315;

    private final int lunchOpenTimeWeekday = 1100;
    private final int lunchCloseTimeWeekday = 1330;

    private final int dinnerOpenTime = 1700;
    private final int dinnerCloseTimeWeekday = 1930;
    private final int dinnerCloseTimeSatOrSun = 1900;

    private static final int RICHARDSON = 1;

    public static Fragment createNewInstance() {
        CafeteriaFragment fragment = new CafeteriaFragment();
        Bundle arg = new Bundle();
        fragment.setArguments(arg);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_cafeteria, parent, false);

        mCalender = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HHmm");
        // Global variable that holds the 24 hour time and minutes (11:30pm is 2330)
        mCurrentTime = Integer.parseInt(dateFormat.format(mCalender.getTime()));

        // Round mCurrentTime to nearest multiple of five to keep things clean
        mCurrentTime = (int) Math.floor((mCurrentTime + 5/2) / 5) * 5;

        Log.d("CafeteriaFragment", "mCurrentTime = " + mCurrentTime);

        currentDayNum = mCalender.get(Calendar.DAY_OF_WEEK);


        // Add any current or upcoming meals to the meals list. Might be empty if no meals are
        // current or upcoming
        setUpMeals(currentDayNum);

        isOpen = isOpen();

        mealsRecyclerView = (RecyclerView) v.findViewById(R.id.meals_recycler_view);
        timeLeftText = (TextView) v.findViewById(R.id.time_left_text);
        noMoreMeals = (TextView) v.findViewById(R.id.no_more_meals);
        statusBarExtendedInfoFrame = (LinearLayout) v.findViewById(
                R.id.status_bar_extended_info_frame);
        statusBar = (LinearLayout) v.findViewById(R.id.cafeteria_status_bar);

        statusBar.setClickable(true);

        statusBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusBar.setClickable(false);
                expandStatusBar();

                statusBar.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        statusBar.setClickable(true);
                    }
                }, 300);
            }
        });

        mLayoutManager = new LinearLayoutManager(getActivity());
        mealsRecyclerView.setLayoutManager(mLayoutManager);
        mealsRecyclerView.setAdapter(new MealsRecyclerAdapter(getActivity(), meals));

        setUpStatusBar();


        return v;
    }

    /**
     * Sets up the Open/Closed status bar at the top of the fragment.
     */
    private void setUpStatusBar() {
        if ( isOpen() ) {
            statusBar.setBackgroundColor(getResources().getColor(R.color.green));
            ((ImageView) v.findViewById(R.id.status_bar_open_closed_sign))
                    .setImageResource(R.drawable.open_sign);
            timeLeftText.setText(getTimeRemainingUntilClose());
        } else {
            statusBar.setBackgroundColor(getResources().getColor(R.color.red));
            ((ImageView) v.findViewById(R.id.status_bar_open_closed_sign))
                    .setImageResource(R.drawable.closed_sign);
            timeLeftText.setText(getTimeRemainingUntilOpen());
        }
    }

    /**
     * Determines when the caf is open.
     *
     * @return whether or not the caf is open
     */
    private boolean isOpen() {
        if ( meals.isEmpty() ) {
            return false;
        }
        for (int i = 0; i < meals.size(); i++) {
            if ( meals.get(i).isCurrent() ) {
//                mCurrentMeal = meals.get(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the amount of time remaining until the caf closes.
     *
     * @return time remaining until close
     */
    private String getTimeRemainingUntilClose() {
        // Check to see if the day is a weekday
        if ( currentDayNum <= 5 ) {
            // Check to see if caf is open
            if ( timeIsBetween(mCurrentTime, breakfastOpenTimeWeekday, breakfastCloseTimeWeekday) ) {
                return timeUntil(breakfastCloseTimeWeekday);
            } else if ( timeIsBetween(mCurrentTime, lunchOpenTimeWeekday, lunchCloseTimeWeekday) ) {
                return timeUntil(lunchCloseTimeWeekday);
            } else if ( timeIsBetween(mCurrentTime, dinnerOpenTime, dinnerCloseTimeWeekday) ) {
                return timeUntil(dinnerCloseTimeWeekday);
            }
        } else if ( currentDayNum == 6) { // Saturday
            if ( timeIsBetween(mCurrentTime, breakfastOpenTimeSaturday, breakfastCloseTimeSaturday) ) {
                return timeUntil(breakfastCloseTimeSaturday);
            } else if ( timeIsBetween(mCurrentTime, brunchOpenTimeSatOrSun, brunchCloseTimeSatOrSun) ) {
                return timeUntil(brunchCloseTimeSatOrSun);
            } else if ( timeIsBetween(mCurrentTime, dinnerOpenTime, dinnerCloseTimeSatOrSun) ) {
                return timeUntil(dinnerCloseTimeSatOrSun);
            }
        } else { // Sunday
            if ( timeIsBetween(mCurrentTime, brunchOpenTimeSatOrSun, brunchCloseTimeSatOrSun) ) {
                return timeUntil(brunchCloseTimeSatOrSun);
            } else if ( timeIsBetween(mCurrentTime, dinnerOpenTime, dinnerCloseTimeSatOrSun) ) {
                return timeUntil(dinnerCloseTimeSatOrSun);
            }
        }
        return "Caf is currently open";
    }

    private String getTimeRemainingUntilOpen() {
        if ( currentDayNum <= 5 ) { // Weekday
            if ( mCurrentTime < breakfastOpenTimeWeekday ) {
                return timeUntil(breakfastOpenTimeWeekday);
            } else if ( mCurrentTime < lunchOpenTimeWeekday ) {
                return timeUntil(lunchOpenTimeWeekday);
            } else if ( mCurrentTime < dinnerOpenTime ) {
                return timeUntil(dinnerOpenTime);
            }
        } else if ( currentDayNum == 6) { // Saturday
            if ( mCurrentTime < breakfastOpenTimeSaturday ) {
                return timeUntil(breakfastOpenTimeSaturday);
            } else if ( mCurrentTime < brunchOpenTimeSatOrSun) {
                return timeUntil(brunchOpenTimeSatOrSun);
            } else if ( mCurrentTime < dinnerOpenTime ) {
                return timeUntil(dinnerOpenTime);
            }
        } else { // Sunday
            if ( mCurrentTime < brunchOpenTimeSatOrSun) {
                return timeUntil(brunchOpenTimeSatOrSun);
            } else if ( mCurrentTime < dinnerOpenTime ) {
                return timeUntil(dinnerOpenTime);
            }
        }
        return timeUntilTomorrow();
    }

    private String timeUntilTomorrow() {
        String openTime;
        int dayNumTomorrow;

        // Roll over
        if ( currentDayNum == 7 ) {
            dayNumTomorrow = 1;
        } else {
            dayNumTomorrow = currentDayNum + 1;
        }

        if ( 1 <= dayNumTomorrow && dayNumTomorrow <= 5 ) {
            openTime = "7:30 AM";
        } else if ( dayNumTomorrow == 6 ) {
            openTime = "9:30 AM";
        } else {
            openTime = "11:15 AM";
        }

        return "Opens tomorrow at " + openTime;
    }

    private String timeUntil(int laterDate) {
        // Convert both dates to minutes from midnight, find difference, convert back
        int startMinutes = minutesSinceMidnight(mCurrentTime);
        int endMinutes = minutesSinceMidnight(laterDate);

        int dif = endMinutes - startMinutes;

        int hour = dif / 60;
        int min = dif % 60;

        String hoursPart = hour + " hours";
        String minutesPart = " and " + min + " minutes";

        if (hour == 0 ) {
            hoursPart = "";
            minutesPart = min + " minutes";
        } else if ( hour == 1 ) {
            hoursPart = "an hour";
        }

        if ( min == 0 ) {
            minutesPart = "";
        } else if ( min == 30 ) {
            hoursPart = hour + "";
            minutesPart = " and a half hours";
        } else if ( min == 5 ) {
            minutesPart = "a few minutes";
            if ( hour == 1 ) {
                minutesPart = "";
            }
        }

        if ( min == 30 && hour == 0 ) {
            hoursPart = "";
            minutesPart = "half an hour";
        }

        if ( isOpen ) {
            return "Closes in " + hoursPart + minutesPart;
        } else {
            return "Opens in " + hoursPart + minutesPart;
        }
    }

    public static int minutesSinceMidnight(int milTime) {
        double time = milTime / 100d;

        int hours = (int) Math.floor(time);
        int minutes = milTime % 100;

        return (hours * 60) + minutes;
    }

    /**
     *  Helper function to clean up the isOpen() method. Simply checks to see if the the
     *  time is between the last two parameters.
     *
     * @param time the current time
     * @param openTime the starting time
     * @param closeTime the ending time
     * @return Whether or not time is between openTime and closeTime
     */
    private boolean timeIsBetween(int time, long openTime, int closeTime) {
        return openTime <= time && time < closeTime;
    }

    /**
     * Creates the meal objects and adds the appropriate meals, depending on whether or not they
     * are current, in the past, or upcoming.
     */
    private void setUpMeals(int dayNumber) {
        // Construct the meals
        Meal breakfast = new Meal("Breakfast");
        Meal brunch = new Meal("Brunch");
        Meal lunch = new Meal("Lunch");
        Meal dinner = new Meal("Dinner");

        boolean isToday = (dayNumber == currentDayNum);

        // For each meal, set the times for that day then check to see if it is in the past.
        // If it isn't, add it to the meals list.
        if ( 1 < dayNumber && dayNumber <= 6 ) { // Mon - Fri
            possiblyAddToMeals(isToday, breakfast, breakfastOpenTimeWeekday,
                    breakfastCloseTimeWeekday);
            possiblyAddToMeals(isToday, lunch, lunchOpenTimeWeekday, lunchCloseTimeWeekday);
            possiblyAddToMeals(isToday, dinner, dinnerOpenTime, dinnerCloseTimeWeekday);
        } else if ( dayNumber == 7) { // Saturday
            possiblyAddToMeals(isToday, breakfast, breakfastOpenTimeSaturday, breakfastCloseTimeSaturday);
            possiblyAddToMeals(isToday, brunch, brunchOpenTimeSatOrSun, brunchCloseTimeSatOrSun);
            possiblyAddToMeals(isToday, dinner, dinnerOpenTime, dinnerCloseTimeSatOrSun);
        } else { // Sunday
            possiblyAddToMeals(isToday, brunch, brunchOpenTimeSatOrSun, brunchCloseTimeSatOrSun);
            possiblyAddToMeals(isToday, dinner, dinnerOpenTime, dinnerCloseTimeSatOrSun);
        }
    }

    /**
     * Possibly adds 'meal' to the meals list, depending on whether or not it is current or
     * upcoming.
     *
     * @param isToday whether the meals list we're constructing is for today or not
     * @param meal the meal to possibly add
     * @param openTime the opening time of the meal
     * @param closeTime the closing time of the meal
     */
    private void possiblyAddToMeals(boolean isToday, Meal meal, int openTime, int closeTime) {
        meal.setTimes(openTime, closeTime);
        // If isToday is false, add the meal. If isToday is true, then check if it's in the past.
        if ( ! isToday ) {
            meals.add(meal);
        } else if ( ! meal.isPast() ) {
            meals.add(meal);
        }
    }

    private void initializeDaySpinner() {
        ArrayList<String> days = new ArrayList<>();
        // populate the days arraylist with 4 entries. The first two will always be
        // "Today" and "Tomorrow", but the last 2 entries must be programmatically determined
        // using the current date.
        days.add("Today");
        days.add("Tomorrow");
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
        mCalender.add(Calendar.DATE, 2);
        String thirdDay = dayFormat.format(mCalender.getTime());
        mCalender.add(Calendar.DATE, 1);
        String fourthDay = dayFormat.format(mCalender.getTime());
        days.add(thirdDay);
        days.add(fourthDay);

        CustomSpinnerAdapter spinnerAdapter = new CustomSpinnerAdapter(getActivity(),
                android.R.layout.simple_spinner_item, days);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        daySpinner.setAdapter(spinnerAdapter);

        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Empty the meals list to start anew.
                meals.clear();
                // Set up the meals again, but this time pass in the day num depending on the
                // position. Sunday is one.
                int selectedDayNum = currentDayNum + position;
                if ( selectedDayNum >= 8 ) { // Rollover
                    selectedDayNum = selectedDayNum - 7;
                }
                setUpMeals(selectedDayNum);

                mealsRecyclerView.getAdapter().notifyDataSetChanged();
                checkIfNoMeals(meals);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        allMealsAreOver = true;
        for (Meal meal : meals) {
            if ( ! meal.isPast() ) { // If any of the meals aren't past, set it to "Today"
                allMealsAreOver = false;
            }
        }
        // If all meals are over, set the spinner to tomorrow.
        if ( allMealsAreOver ) {
            daySpinner.setSelection(1);
        } else {
            daySpinner.setSelection(0);
        }
    }

    private void expandStatusBar() {
        if ( ! isExpanded ) { // Expand
            // Rotate the drawer arrow
            statusBarArrow = (ImageView) v.findViewById(R.id.status_bar_arrow);

            AnimationSet animSet = new AnimationSet(true);
            animSet.setInterpolator(new DecelerateInterpolator());
            animSet.setFillAfter(true);
            animSet.setFillEnabled(true);

            final RotateAnimation animRotate = new RotateAnimation(0.0f, -180.0f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f);

            animRotate.setDuration(300);
            animRotate.setFillAfter(true);
            animSet.addAnimation(animRotate);

            statusBarArrow.startAnimation(animSet);

            HeightAnimation animation = new HeightAnimation(statusBarExtendedInfoFrame, convertToPixel(120), true);
            animation.setDuration(300);
            statusBar.startAnimation(animation);
            isExpanded = !isExpanded;
        } else { // Collapse
            // Rotate the drawer arrow
            statusBarArrow = (ImageView) v.findViewById(R.id.status_bar_arrow);

            AnimationSet animSet = new AnimationSet(true);
            animSet.setInterpolator(new DecelerateInterpolator());
            animSet.setFillAfter(true);
            animSet.setFillEnabled(true);

            final RotateAnimation animRotate = new RotateAnimation(-180.0f, 0.0f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f);

            animRotate.setDuration(300);
            animRotate.setFillAfter(true);
            animSet.addAnimation(animRotate);

            statusBarArrow.startAnimation(animSet);


            HeightAnimation animation = new HeightAnimation(statusBarExtendedInfoFrame, convertToPixel(120), false);
            animation.setDuration(300);
            statusBar.startAnimation(animation);
            isExpanded = !isExpanded;
        }


    }

    private TextView createFoodItemView(String foodName) {
        TextView tvFoodItem = new TextView(getActivity());
        tvFoodItem.setText("- " + foodName);
        tvFoodItem.setTextColor(getResources().getColor(R.color.primary_text_default_material_light));

        return tvFoodItem;
    }

    private class Meal {
        String mealTitle;
        private String[] mainLineItems;
        private String[] internationalCornerItems;
        private int openTime;
        private int closeTime;
        private boolean isCurrent = false;
        private boolean isPast = false;

        public Meal(String mealTitle) {
            this.mealTitle = mealTitle;

            //TODO pull actual items from internet
            mainLineItems = new String[] {"Main line one", "Main line two"};
            internationalCornerItems = new String[] {"International one", "International two"};
        }

        public String getMealTitle() {
            return mealTitle;
        }

        public boolean isPast() {
            determineTimeState();
            return isPast;
        }

        public boolean isCurrent() {
            return isCurrent;
        }

        public String[] getMainLineItems() {
            return mainLineItems;
        }

        public String[] getInternationalCornerItems() {
            return internationalCornerItems;
        }

        public void setTimes(int openTime, int closeTime) {
            this.openTime = openTime;
            this.closeTime = closeTime;

            determineTimeState();
        }

        public void determineTimeState() {
            if ( mCurrentTime > closeTime ) {
                isPast = true;
            } else {
                isCurrent = timeIsBetween(mCurrentTime, openTime, closeTime);
            }
        }
    }

    private void reloadFragment() {
        getFragmentManager().beginTransaction().detach(this)
                .attach(this)
                .commit();
    }

    /**
     * If the meal list is empty, hides the recycler view and sets the no more meals
     * text to visible.
     *
     * @param mealList
     * @return
     */
    private void checkIfNoMeals(List<Meal> mealList) {
        if ( mealList.isEmpty() ) {
            mealsRecyclerView.setVisibility(View.GONE);
            noMoreMeals.setVisibility(View.VISIBLE);
        } else {
            mealsRecyclerView.setVisibility(View.VISIBLE);
            noMoreMeals.setVisibility(View.GONE);
        }
    }

    public class MealsRecyclerAdapter extends
            RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<Meal> mealList;
        private Context context;

        private MealsRecyclerAdapter(Context context, List<Meal> mealList) {
            this.context = context;
            this.mealList = mealList;

            checkIfNoMeals(this.mealList);

            Log.d("mealsRecycler", "mealList size = " + mealList.size());
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == 1) {
                //inflate your layout and pass it to view holder
                View view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.caf_meal_card, parent, false);
                return new ViewHolderItem(view);
            } else {
                //inflate your layout and pass it to view holder
                View view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.cafeteria_spinner, parent, false);
                return new ViewHolderHeader(view);
            }
        }

        @Override
        public int getItemViewType(int position) {
            if ( position == 0 ) {
                return 0;
            }
            return 1;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder rawHolder, int position) {


            if ( rawHolder instanceof ViewHolderItem ) {
                Meal meal = meals.get(position - 1);
                ViewHolderItem holder = ((ViewHolderItem) rawHolder);
                holder.mealTitle.setText(meal.getMealTitle());

                if ( meal.isCurrent() ) {
                    holder.currentlyServingText.setVisibility(View.VISIBLE);
                }

                if ( meal.getMealTitle().equals("Breakfast") ) {
                    holder.mealImage.setBackground(getResources().getDrawable(R.drawable.breakfast));
                } else if ( meal.getMealTitle().equals("Lunch") ) {
                    holder.mealImage.setBackground(getResources().getDrawable(R.drawable.lunch));
                } else {
                    holder.mealImage.setBackground(getResources().getDrawable(R.drawable.dinner));
                }

                loadMenu(holder, meal);
            } else if ( rawHolder instanceof ViewHolderHeader ) {

                initializeDaySpinner();
            }
        }

        private void expandCardView(ViewHolderItem viewHolder) {
            HeightAnimation animation = new HeightAnimation(viewHolder.llMainLines,
                    getHeightToAdd(viewHolder.llMainLines, true), true);
            animation.setDuration(300);
            viewHolder.llMainLines.startAnimation(animation);
            HeightAnimation animation1 = new HeightAnimation(viewHolder.llInternationalCorner,
                    getHeightToAdd(viewHolder.llInternationalCorner, true), true);
            animation1.setDuration(300);
            viewHolder.llInternationalCorner.startAnimation(animation1);
        }

        private void collapseCardView(ViewHolderItem viewHolder) {
            HeightAnimation animation = new HeightAnimation(viewHolder.llMainLines,
                    getHeightToAdd(viewHolder.llMainLines, false), false);
            animation.setDuration(300);
            viewHolder.llMainLines.startAnimation(animation);
            HeightAnimation animation1 = new HeightAnimation(viewHolder.llInternationalCorner,
                    getHeightToAdd(viewHolder.llInternationalCorner, false), false);
            animation1.setDuration(300);
            viewHolder.llInternationalCorner.startAnimation(animation1);
        }


        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public int getItemCount()
        {
            return mealList.size() + 1;
        }

        public class ViewHolderItem extends RecyclerView.ViewHolder {
            public TextView mealTitle, currentlyServingText, noMoreMeals;
            public LinearLayout llMainLines, llInternationalCorner;
            public FrameLayout mealImage;
            public ToggleButton tbViewMore;
            public CardView cvRoot;

            public ViewHolderItem(View itemView) {
                super(itemView);
                mealTitle = (TextView) itemView.findViewById(R.id.meal_title);
                mealImage = (FrameLayout) itemView.findViewById(R.id.image);
                currentlyServingText = (TextView) itemView.findViewById(R.id.currently_serving);
                llMainLines = (LinearLayout) itemView.findViewById(
                        R.id.fragment_cafeteria_llMainLine);
                llInternationalCorner = (LinearLayout) itemView.findViewById(
                        R.id.fragment_cafeteria_llInternationalCorner);
                tbViewMore = (ToggleButton) itemView.findViewById(
                        R.id.fragment_cafeteria_tbViewMore);
                cvRoot = (CardView) itemView.findViewById(R.id.food_wells_menu_item_cvRoot);
            }

        }

        public class ViewHolderHeader extends RecyclerView.ViewHolder {

            public ViewHolderHeader(View headerView) {
                super(headerView);
                daySpinner = (Spinner) headerView.findViewById(R.id.day_spinner);
            }
        }

        private void loadMenu(ViewHolderItem viewHolder, Meal meal) {

            for (int i = 0; i < meal.getMainLineItems().length; i++) {
                viewHolder.llMainLines.addView(createFoodItemView(meal.getMainLineItems()[i]));
            }

            setListViewHeightBasedOnChildren(viewHolder.llMainLines);

            for (int i = 0; i < meal.getInternationalCornerItems().length; i++) {
                viewHolder.llInternationalCorner.addView(createFoodItemView(meal.getInternationalCornerItems()[i]));
            }

            setListViewHeightBasedOnChildren(viewHolder.llInternationalCorner);
        }

        public void setListViewHeightBasedOnChildren(LinearLayout linearLayout) {
            if (linearLayout == null) {
                // pre-condition
                return;
            }
            int starterHeight;
            View listItem = linearLayout.getChildAt(0);
            listItem.measure(0, 0);
            int heightOfChild = listItem.getMeasuredHeight();
            starterHeight = (linearLayout.getChildCount() < 3 ? linearLayout.getChildCount() : 3) * heightOfChild;

            ViewGroup.LayoutParams params = linearLayout.getLayoutParams();
            params.height = starterHeight;
            linearLayout.setLayoutParams(params);
            linearLayout.requestLayout();

        }
    }


    private int getHeightToAdd(LinearLayout parent, boolean isExpand)
    {
        View listItem = parent.getChildAt(0);
        listItem.measure(0, 0);
        int heightOfChild = listItem.getMeasuredHeight();
        if (parent.getChildCount() <= 3)
            return 0;
        else
            return heightOfChild * (parent.getChildCount() - 3);


    }

    // setting up a custom adapter for spinner
    private class CustomSpinnerAdapter extends ArrayAdapter<String> {

        private ArrayList<String> days;
        private LayoutInflater inflater;
        private Context context;

        public CustomSpinnerAdapter(Context context, int rowResourceLayout, ArrayList<String> days) {
            super(context, rowResourceLayout, days);
            this.context = context;
            this.days = days;

            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        // This funtion called for each row ( Called data.size() times )
        public View getCustomView(int position, View convertView, ViewGroup parent) {

            /********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
            View row = inflater.inflate(R.layout.cafeteria_spinner_rows, parent, false);

            TextView label = (TextView)row.findViewById(R.id.day);

            label.setText(days.get(position));

            return row;
        }

    }

    private int convertToPixel(int dp)
    {
      return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

}
