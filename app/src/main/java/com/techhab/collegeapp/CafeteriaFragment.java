package com.techhab.collegeapp;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;


public class CafeteriaFragment extends Fragment {

    public static final String ARG_OBJECT = "object";
    private static int selectedPosition = 0;
    private static final int MF_INDEX = 0;
    private static final int SATURDAY_INDEX = 1;
    private static final int SUNDAY_INDEX = 2;
    // Buttons Cafeteria
    private View v;
    private FoodStore cafeteriaStore;
    private TextView timeLeftText;
    private ImageView statusBarArrow;
    private LinearLayout statusBarExtendedInfoFrame;
    private RecyclerView mealsRecyclerView;
    private LinearLayout statusBar;
    private boolean isBreakfastCollapse = true;
    private RecyclerView.LayoutManager mLayoutManager;
    private Spinner daySpinner;
    private int currentBarColor;
    private boolean isExpanded = false;
    private Calendar mCalender;
    private int mCurrentTime;
    private int dayNum;
    private boolean isOpen;
    private Meal mCurrentMeal;
    private List<Meal> meals = new ArrayList<>();

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

        // Round mCurrentTime to nears multiple of five to keep things clean
        mCurrentTime = (int) Math.floor((mCurrentTime + 5/2) / 5) * 5;

        dayNum = mCalender.get(Calendar.DAY_OF_WEEK);

        setUpMeals();

        isOpen = isOpen();


        mealsRecyclerView = (RecyclerView) v.findViewById(R.id.meals_recycler_view);
        timeLeftText = (TextView) v.findViewById(R.id.time_left_text);
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


        daySpinner = (Spinner) v.findViewById(R.id.day_spinner);
        ArrayList<String> days = new ArrayList<>();
        // populate the days arraylist with 4 entries. The first two will always be
        // "Today" and "Tomorrow", but the last 2 entries must be programmatically determined
        // using the current date.
        days.add("Today");
        days.add("Tomorrow");

        Calendar mCalender = Calendar.getInstance();
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);

        mCalender.add(Calendar.DATE, 2);
        String thirdDay = dayFormat.format(mCalender.getTime());

        mCalender.add(Calendar.DATE, 1);
        String fourthDay = dayFormat.format(mCalender.getTime());

        days.add(thirdDay);
        days.add(fourthDay);

        CustomAdapter spinnerAdapter = new CustomAdapter(getActivity(),
                android.R.layout.simple_spinner_item, days);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setSelection(0, false);
        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            // after the item is selected in the spinner it should refresh the fragment
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (selectedPosition != position) {
                    selectedPosition = position;
                    reloadFragment();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        daySpinner.setAdapter(spinnerAdapter);

//        setupData();

        mLayoutManager = new LinearLayoutManager(getActivity());
        mealsRecyclerView.setLayoutManager(mLayoutManager);
        mealsRecyclerView.setAdapter(new MealsRecyclerAdapter(getActivity(), meals));

        setUpStatusBar();


        return v;
    }

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
     * Determines when the caf is open
     *
     * @return whether or not the caf is open
     */
    private boolean isOpen() {

        for (int i = 0; i < meals.size(); i++) {
            if ( meals.get(i).isCurrentMeal() ) {
                mCurrentMeal = meals.get(i);
                return true;
            }
        }
        return false;

        /*
        // Check to see if the day is a weekday
        if ( dayNum <= 5 ) {
            // Check to see if caf is open
            if ( timeIsBetween(mCurrentTime, breakfastOpenTimeWeekday, breakfastCloseTimeWeekday) ) {
                mCurrentMeal = "breakfast";
                return true;
            } else if ( timeIsBetween(mCurrentTime, lunchOpenTimeWeekday, lunchCloseTimeWeekday) ) {
                mCurrentMeal = "lunch";
                return true;
            } else if ( timeIsBetween(mCurrentTime, dinnerOpenTime, dinnerCloseTimeWeekday ) ) {
                mCurrentMeal = "dinner";
                return true;
            }
        } else if ( dayNum == 6) { // Saturday
            if ( timeIsBetween(mCurrentTime, breakfastOpenTimeSaturday, breakfastCloseTimeSaturday) ) {
                mCurrentMeal = "breakfast";
                return true;
            } else if ( timeIsBetween(mCurrentTime, brunchOpenTimeSatOrSun, brunchCloseTimeSatOrSun) ) {
                mCurrentMeal = "brunch";
                return true;
            } else if ( timeIsBetween(mCurrentTime, dinnerOpenTime, dinnerCloseTimeSatOrSun) ) {
                mCurrentMeal = "dinner";
                return true;
            }
        } else { // Sunday
            if ( timeIsBetween(mCurrentTime, brunchOpenTimeSatOrSun, brunchCloseTimeSatOrSun) ) {
                mCurrentMeal = "brunch";
                return true;
            } else if ( timeIsBetween(mCurrentTime, dinnerOpenTime, dinnerCloseTimeSatOrSun) ) {
                mCurrentMeal = "dinner";
                return true;
            }
        }
        return false;*/
    }

    /**
     * Returns the amount of time remaining until the caf closes
     *
     * @return time remaining until close
     */
    private String getTimeRemainingUntilClose() {
        // Check to see if the day is a weekday
        if ( dayNum <= 5 ) {
            // Check to see if caf is open
            if ( timeIsBetween(mCurrentTime, breakfastOpenTimeWeekday, breakfastCloseTimeWeekday) ) {
                return timeUntil(breakfastCloseTimeWeekday);
            } else if ( timeIsBetween(mCurrentTime, lunchOpenTimeWeekday, lunchCloseTimeWeekday) ) {
                return timeUntil(lunchCloseTimeWeekday);
            } else if ( timeIsBetween(mCurrentTime, dinnerOpenTime, dinnerCloseTimeWeekday) ) {
                return timeUntil(dinnerCloseTimeWeekday);
            }
        } else if ( dayNum == 6) { // Saturday
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
        if ( dayNum <= 5 ) { // Weekday
            if ( mCurrentTime < breakfastOpenTimeWeekday ) {
                return timeUntil(breakfastOpenTimeWeekday);
            } else if ( mCurrentTime < lunchOpenTimeWeekday ) {
                return timeUntil(lunchOpenTimeWeekday);
            } else if ( mCurrentTime < dinnerOpenTime ) {
                return timeUntil(dinnerOpenTime);
            }
        } else if ( dayNum == 6) { // Saturday
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
        if ( dayNum == 7 ) {
            dayNumTomorrow = 1;
        } else {
            dayNumTomorrow = dayNum + 1;
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

    private void setUpMeals() {
        // There are no meals, add them depending on the day

        // Construct the meals
        Meal breakfast = new Meal("Breakfast");
        Meal brunch = new Meal("Brunch");
        Meal lunch = new Meal("Lunch");
        Meal dinner = new Meal("Dinner");

        if ( dayNum <= 5 ) {
            breakfast.setTimes(breakfastOpenTimeWeekday, breakfastCloseTimeWeekday);
            meals.add(breakfast);

            lunch.setTimes(lunchOpenTimeWeekday, lunchCloseTimeWeekday);
            meals.add(lunch);

            dinner.setTimes(dinnerOpenTime, dinnerCloseTimeWeekday);
            meals.add(dinner);
        } else if ( dayNum == 6) { // Saturday
            breakfast.setTimes(breakfastOpenTimeSaturday, breakfastCloseTimeSaturday);
            meals.add(breakfast);

            brunch.setTimes(brunchOpenTimeSatOrSun, brunchCloseTimeSatOrSun);
            meals.add(brunch);

            dinner.setTimes(dinnerOpenTime, dinnerCloseTimeSatOrSun);
            meals.add(dinner);
        } else { // Sunday
            brunch.setTimes(brunchOpenTimeSatOrSun, brunchCloseTimeSatOrSun);
            meals.add(brunch);

            dinner.setTimes(dinnerOpenTime, dinnerCloseTimeSatOrSun);
            meals.add(dinner);
        }
    }

    /*private void setupData() {
        //todo: fake data for cafe
        cafeteriaStore = new FoodStore();
        cafeteriaStore.setStoreName("Cafeteria");
        Meal breakfast = new Meal();
        breakfast.setMealTitle("Breakfast");
        List<String> breakfastMainLines = new ArrayList<String>();
        breakfastMainLines.add("Scrambled Eggs");
        breakfastMainLines.add("Bacon");
        breakfastMainLines.add("Hash Browns");
        breakfast.addMainLineItems(breakfastMainLines);
        breakfast.setInternationalCornerItems(Arrays.asList("1", "2", "3", "4"));
        Integer[] openTimes = new Integer[] {730, breakfastOpenTimeSaturday};
        breakfast.setOpenTimes(Arrays.asList(new TimeOfDay(7, 30), new TimeOfDay(9, 30), null));
        breakfast.setEndTimes(Arrays.asList(new TimeOfDay(10, 0), new TimeOfDay(11, 0), null));

        Meal lunch = new Meal();
        lunch.setMealTitle("Lunch");

        lunch.addMainLineItems(Arrays.asList("item1", "item2", "item3", "item4",
                "item5", "item6", "item7"));
        List<String> internationalCorner1 = new ArrayList<String>();
        internationalCorner1.add("item1");
        internationalCorner1.add("item2");
        internationalCorner1.add("item3");
        internationalCorner1.add("item4");
        lunch.setInternationalCornerItems(internationalCorner1);
        lunch.setOpenTimes(Arrays.asList(new TimeOfDay(11, 0), null, null));
        lunch.setEndTimes(Arrays.asList(new TimeOfDay(13, 30), null, null));
        Meal dinner = new Meal();
        dinner.setMealTitle("Dinner");
        List<String> mainLines2 = new ArrayList<String>();
        mainLines2.add("item1");
        mainLines2.add("item2");
        mainLines2.add("item3");
        mainLines2.add("item4");
        mainLines2.add("item5");
        dinner.addMainLineItems(mainLines2);
        List<String> internationalCorner2 = new ArrayList<String>();
        internationalCorner2.add("item1");
        internationalCorner2.add("item2");
        internationalCorner2.add("item3");
        internationalCorner2.add("item4");
        internationalCorner2.add("item5");
        internationalCorner2.add("item6");
        dinner.setInternationalCornerItems(internationalCorner2);
//        dinner.setOpenTimes(Integer[] );
        dinner.setOpenTimes(Arrays.asList(new TimeOfDay(17, 0), new TimeOfDay(17, 0), new TimeOfDay(17, 0)));
        dinner.setEndTimes(Arrays.asList(new TimeOfDay(19, 30), new TimeOfDay(19, 0), new TimeOfDay(19, 0)));

        List<Meal> meals = new ArrayList<Meal>();
        meals.add(breakfast);
        meals.add(lunch);
        meals.add(dinner);


        cafeteriaStore.setMealList(meals);
    }*/

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

    /*private long getOpenTime(int dayIndex)
    {
        for (Meal meal : cafeteriaStore.getMealList()) {
            if (meal.getOpenTime().get(dayIndex) != null) {
                return meal.getOpenTime().get(dayIndex).getTimeInMillis();
            }
        }
        return 0;

    }*/
    private long getTimeOFDay(int hour, int minute)
    {
        Calendar openCalendar = Calendar.getInstance();
        openCalendar.set(Calendar.HOUR_OF_DAY, hour);
        openCalendar.set(Calendar.MINUTE, minute);
        openCalendar.set(Calendar.SECOND, 0);
        return openCalendar.getTimeInMillis();
    }



    private TextView createFoodItemView(String foodName)
    {
        TextView tvFoodItem = new TextView(getActivity());
        tvFoodItem.setText("- " + foodName);
        tvFoodItem.setTextColor(getResources().getColor(R.color.primary_text_default_material_light));

        return tvFoodItem;
    }

    private class FoodStore
    {
        int openHour;
        int openMinutes;
        int closeHour;
        int closeMinutes;
        String storeName;
        List<Meal> mealList;
        public int getOpenHour() {
            return openHour;
        }



        public int getOpenMinutes() {
            return openMinutes;
        }

        public void setOpenMinutes(int openMinutes) {
            this.openMinutes = openMinutes;
        }

        public int getCloseHour() {
            return closeHour;
        }



        public int getCloseMinutes() {
            return closeMinutes;
        }

        public void setCloseMinutes(int closeMinutes) {
            this.closeMinutes = closeMinutes;
        }

        public String getStoreName() {
            return storeName;
        }

        public void setStoreName(String storeName) {
            this.storeName = storeName;
        }

        public List<Meal> getMealList()
        {
            return mealList;
        }

        public void setMealList(List<Meal> mealList)
        {
            this.mealList = mealList;
        }
    }

    private class Meal {
        String mealTitle;
        private List<String> mainLineItems;
        private List<String> internationalCornerItems;
        private int openTime;
        private int closeTime;
        private boolean isCurrent = false;
        private boolean isPast = false;
        private boolean isUpcoming = false;

        public Meal(String mealTitle) {
            this.mealTitle = mealTitle;
        }

        public String getMealTitle()
        {
            return mealTitle;
        }

        public List<String> getMainLineItems()
        {
            return mainLineItems;
        }

        public void addMainLineItems(List<String> mainLineItems)
        {
            this.mainLineItems = mainLineItems;
        }

        public List<String> getInternationalCornerItems()
        {
            return internationalCornerItems;
        }

        public void setInternationalCornerItems(List<String> internationalCornerItems)
        {
            this.internationalCornerItems = internationalCornerItems;
        }


        public void setTimes(int openTime, int closeTime) {
            this.openTime = openTime;
            this.closeTime = closeTime;

            isCurrentMeal();
        }

        public boolean isCurrentMeal() {
            return timeIsBetween(mCurrentTime, openTime, closeTime);
        }
    }

    private class TimeOfDay
    {
        int hour;
        int minutes;

        public TimeOfDay(int hour, int minutes)
        {
            this.hour = hour;
            this.minutes = minutes;
        }
        public int getHour() {
            return hour;
        }

        public void setHour(int hour) {
            this.hour = hour;
        }

        public int getMinutes() {
            return minutes;
        }

        public void setMinutes(int minutes) {
            this.minutes = minutes;
        }

        public String getTime()
        {
            NumberFormat formatter = new DecimalFormat("00");
            return hour + ":" + formatter.format(minutes);
        }

        public long getTimeInMillis()
        {
            return getTimeOFDay(hour, minutes);
        }
    }

    private void reloadFragment()
    {
            getFragmentManager().beginTransaction().detach(this)
                    .attach(this)
                    .commit();
    }

    private class MealsRecyclerAdapter extends RecyclerView.Adapter
    {
        private List<Meal> mealList;
        private Context context;

        private MealsRecyclerAdapter(Context context, List<Meal> mealList)
        {
            this.context = context;
            this.mealList = mealList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.caf_meal_card, viewGroup, false);

            return new ViewHolder(rowView);
        }


        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int i)
        {
            final Meal meal = mealList.get(i);
            final MealsRecyclerAdapter.ViewHolder menuViewHolder = (ViewHolder) viewHolder;
            menuViewHolder.mealTitle.setText(meal.getMealTitle());
            menuViewHolder.mealTitle.setText(meal.getMealTitle());

            loadMenu(menuViewHolder, meal);

            menuViewHolder.tbViewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    menuViewHolder.tbViewMore.setEnabled(false);

                    if (menuViewHolder.tbViewMore.isChecked())
                    {
                        expandCardView((ViewHolder) viewHolder);

                    } else {
                        collapseCardView((ViewHolder) viewHolder);
                    }
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            menuViewHolder.tbViewMore.setEnabled(true);
                        }
                    }, 500);

                }
            });

        }

        private void expandCardView(ViewHolder viewHolder) {
            HeightAnimation animation = new HeightAnimation(viewHolder.llMainLines,
                    getHeightToAdd(viewHolder.llMainLines, true), true);
            animation.setDuration(300);
            viewHolder.llMainLines.startAnimation(animation);
            HeightAnimation animation1 = new HeightAnimation(viewHolder.llInternationalCorner,
                    getHeightToAdd(viewHolder.llInternationalCorner, true), true);
            animation1.setDuration(300);
            viewHolder.llInternationalCorner.startAnimation(animation1);
        }

        private void collapseCardView(ViewHolder viewHolder) {
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
            return mealList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView mealTitle;
            public LinearLayout llMainLines;
            public LinearLayout llInternationalCorner;
            public ToggleButton tbViewMore;
            public CardView cvRoot;

            public ViewHolder(View itemView) {
                super(itemView);
                mealTitle = (TextView) itemView.findViewById(R.id.meal_title);
                llMainLines = (LinearLayout) itemView.findViewById(R.id.fragment_cafeteria_llMainLine);
                llInternationalCorner = (LinearLayout) itemView.findViewById(R.id.fragment_cafeteria_llInternationalCorner);
                tbViewMore = (ToggleButton) itemView.findViewById(R.id.fragment_cafeteria_tbViewMore);
                cvRoot = (CardView) itemView.findViewById(R.id.food_wells_menu_item_cvRoot);
            }

        }

        private void loadMenu(MealsRecyclerAdapter.ViewHolder viewHolder, Meal meal) {

            for (int i = 0; i < meal.getMainLineItems().size(); i++) {
                viewHolder.llMainLines.addView(createFoodItemView(meal.getMainLineItems().get(i)));
            }

            setListViewHeightBasedOnChildren(viewHolder.llMainLines);

            for (int i = 0; i < meal.getInternationalCornerItems().size(); i++) {
                viewHolder.llInternationalCorner.addView(createFoodItemView(meal.getInternationalCornerItems().get(i)));
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
    private class CustomAdapter extends ArrayAdapter<String> {

        private ArrayList<String> days;
        private LayoutInflater inflater;
        private Context context;

        /*************  CustomAdapter Constructor *****************/
        public CustomAdapter(Context context, int rowResourceLayout, ArrayList<String> days ) {
            super(context, rowResourceLayout, days);
            this.context = context;
            this.days = days;

            /***********  Layout inflator to call external xml layout () **********************/
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
