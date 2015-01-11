package com.techhab.collegeapp;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;


public class CafeteriaFragment extends Fragment {
    public static final String ARG_OBJECT = "object";
    private static int selectedPosition = 0;
    private static final int MF_INDEX = 0;
    private static final int SATURDAY_INDEX = 1;
    private static final int SUNDAY_INDEX = 2;
    // Buttons Cafeteria
    private View v;
    private FoodStore cafeteriaStore;
    private TextView tvTimeInfo;
    private ImageView statusBarArrow;
    private LinearLayout statusBarExtendedInfoFrame;
    private RecyclerView rvMenu;
    private LinearLayout statusBar;
    private boolean isBreakfastCollapse = true;
    private RecyclerView.LayoutManager mLayoutManager;
    private Spinner cafeteriaSpinner;
    private int currentBarColor;
    private boolean isExpanded = false;
    private long mLastClickTime;

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
        rvMenu = (RecyclerView) v.findViewById(R.id.fragment_cafeteria_rvMenu);
        tvTimeInfo = (TextView) v.findViewById(R.id.fragment_cafeteria_tvInfo);
        statusBarExtendedInfoFrame = (LinearLayout) v.findViewById(R.id.status_bar_extended_info_frame);
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


        cafeteriaSpinner = (Spinner) v.findViewById(R.id.fragment_cafeteria_spinner);
        ArrayList<String> days = new ArrayList<>();
        // populate the days arraylist with 4 entries. The first two will always be
        // "Today" and "Tomorrow", but the last 2 entries must be programmatically determined
        // using the current date.
        days.add("Today");
        days.add("Tomorrow");
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        GregorianCalendar gregorianCalendar1 = new GregorianCalendar();
        gregorianCalendar.add(Calendar.DATE, 2);
        gregorianCalendar1.add(Calendar.DATE, 3);
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
        String thirdDay = dayFormat.format(gregorianCalendar.getTime());
        String forthDay = dayFormat.format(gregorianCalendar1.getTime());
        days.add(thirdDay);
        days.add(forthDay);
        CustomAdapter spinnerAdapter = new CustomAdapter(getActivity(), android.R.layout.simple_spinner_item, days);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cafeteriaSpinner.setSelection(0, false);
        cafeteriaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            // after the item is selected in the spinner it should refresh the fragment
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (selectedPosition  != position)
                {
                    selectedPosition = position;
                    reloadFragment();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cafeteriaSpinner.setAdapter(spinnerAdapter);

        setupData();
        mapDataToView(v);
        mLayoutManager = new LinearLayoutManager(getActivity());
        rvMenu.setLayoutManager(mLayoutManager);
        rvMenu.setAdapter(new MenuAdapter(getActivity(), cafeteriaStore.getMealList()));

       // for changing the color of the status bar based on the time, as well as the image for open/close sign
        long remainTime = getRemainTime();
        if (remainTime >= 0)
        {
            currentBarColor = R.color.green;
            statusBar.setBackgroundColor(getResources().getColor(R.color.green));
            ((ImageView) v.findViewById(R.id.fragment_cafeteria_image)).setImageResource(R.drawable.open_sign);

        } else {
            currentBarColor = R.color.red;
            statusBar.setBackgroundColor(getResources().getColor(R.color.red));
            ((ImageView) v.findViewById(R.id.fragment_cafeteria_image)).setImageResource(R.drawable.open_sign);
        }
        displayStatusBar(remainTime);
        return v;
    }

    private void mapDataToView(View view) {
        List<TimeOfDay> breakfastOpenTimes = cafeteriaStore.getMealList().get(0).getOpenTimes();
        List<TimeOfDay> breakfastCloseTimes = cafeteriaStore.getMealList().get(0).getEndTimes();
        if (breakfastOpenTimes != null && breakfastCloseTimes != null)
        {
            String breakfastTimeMF = breakfastOpenTimes.get(MF_INDEX) == null ? "-" : breakfastOpenTimes.get(MF_INDEX).getTime() + "-" + breakfastCloseTimes.get(MF_INDEX).getTime();
            ((TextView)view.findViewById(R.id.cafeteria_mF_tvBreakfastTime)).setText(breakfastTimeMF);
            String breakfastTimeSat = breakfastOpenTimes.get(SATURDAY_INDEX) == null ? "-" : breakfastOpenTimes.get(SATURDAY_INDEX).getTime() + "-" + breakfastCloseTimes.get(SATURDAY_INDEX).getTime();
            ((TextView)view.findViewById(R.id.cafeteria_saturday_tvBreakfastTime)).setText(breakfastTimeSat);
            String breakfastTimeSun = breakfastOpenTimes.get(SUNDAY_INDEX) == null ? "-" : breakfastOpenTimes.get(SUNDAY_INDEX).getTime() + "-" + breakfastCloseTimes.get(SUNDAY_INDEX).getTime();
            ((TextView)view.findViewById(R.id.cafeteria_sunday_tvBreakfastTime)).setText(breakfastTimeSun);

        }
    }

    private void setupData() {
        //todo: fake data for cafe
        cafeteriaStore = new FoodStore();
        cafeteriaStore.setStoreName("Cafeteria");
        cafeteriaStore.setOpenHour(4);
        cafeteriaStore.setCloseHour(22);
        Meal breakfast = new Meal();
        breakfast.setMealTitle("Breakfast");
        List<String> breakfastMainLines = new ArrayList<String>();
        breakfastMainLines.add("item 1");
        breakfast.setMainLineItems(breakfastMainLines);
        breakfast.setInternationalCornerItems(Arrays.asList("1", "2", "3", "4"));
        breakfast.setOpenTimes(Arrays.asList(new TimeOfDay(7, 0), new TimeOfDay(9, 30), null));
        breakfast.setEndTimes(Arrays.asList(new TimeOfDay(10, 0), new TimeOfDay(11, 0), null));

        Meal lunch = new Meal();
        lunch.setMealTitle("Lunch");

        lunch.setMainLineItems(Arrays.asList("item1","item2","item3","item4","item5","item6","item7"));
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
        dinner.setMainLineItems(mainLines2);
        List<String> internationalCorner2 = new ArrayList<String>();
        internationalCorner2.add("item1");
        internationalCorner2.add("item2");
        internationalCorner2.add("item3");
        internationalCorner2.add("item4");
        internationalCorner2.add("item5");
        internationalCorner2.add("item6");
        dinner.setInternationalCornerItems(internationalCorner2);
        dinner.setOpenTimes(Arrays.asList(new TimeOfDay(17, 0), new TimeOfDay(17, 0), new TimeOfDay(17, 0)));
        dinner.setEndTimes(Arrays.asList(new TimeOfDay(19, 30), new TimeOfDay(19, 0), new TimeOfDay(19, 0)));

        List<Meal> meals = new ArrayList<Meal>();
        meals.add(breakfast);
        meals.add(lunch);
        meals.add(dinner);


        cafeteriaStore.setMealList(meals);
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
            // why you set 300 into HeightAnimation

            HeightAnimation animation = new HeightAnimation(statusBarExtendedInfoFrame, convertToPixel(120), false);
            animation.setDuration(300);
            statusBar.startAnimation(animation);
            isExpanded = !isExpanded;
        }


    }



    private long getRemainTime()
    {
        Calendar currentTime = Calendar.getInstance();
        int dayOfWeek = currentTime.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek)
        {
            case Calendar.SUNDAY:
                return countRemainTime(currentTime.getTimeInMillis(), SUNDAY_INDEX);
            case Calendar.SATURDAY:
                return countRemainTime(currentTime.getTimeInMillis(), SATURDAY_INDEX);
            default:
                return countRemainTime(currentTime.getTimeInMillis(), MF_INDEX);
        }

    }

    private long  countRemainTime(long currentTime, int dayIndex)
    {
        long openTime = 0;
        for (Meal meal : cafeteriaStore.getMealList())
        {
           if (meal.getOpenTimes().get(dayIndex) != null)
           {
               long openTimeInMillis = meal.getOpenTimes().get(dayIndex).getTimeInMillis();
               if (openTime == 0)
               {
                   openTime = openTimeInMillis;
               }
               long closeTimeInMillis = meal.getEndTimes().get(dayIndex).getTimeInMillis();
               if (currentTime >= openTimeInMillis && currentTime <= closeTimeInMillis)
                   return closeTimeInMillis - currentTime;
               if (currentTime < openTimeInMillis)
                   return currentTime - openTimeInMillis;
           }

        }
        return currentTime - openTime - 86400*1000;
    }

    private long getTimeOFDay(int hour, int minute)
    {
        Calendar openCalendar = Calendar.getInstance();
        openCalendar.set(Calendar.HOUR_OF_DAY, hour);
        openCalendar.set(Calendar.MINUTE, minute);
        openCalendar.set(Calendar.SECOND, 0);
        return openCalendar.getTimeInMillis();
    }



    private void displayStatusBar(final long remainTime)
    {

        new CountDownTimer(Math.abs(remainTime), 1000) {
            public void onTick(long millisUntilFinished) {

                int days = (int) ((millisUntilFinished / 1000) / 86400);
                int hours = (int) (((millisUntilFinished / 1000) - (days
                        * 86400)) / 3600);
                int minutes = (int) (((millisUntilFinished / 1000) - ((days
                        * 86400) + (hours * 3600))) / 60);
                int seconds = (int) ((millisUntilFinished / 1000) % 60);


                if (remainTime >= 0)
                {
                    if (hours == 0 && minutes <= 30 && currentBarColor != R.color.Yellow)
                    {
                        currentBarColor = R.color.Yellow;
                        statusBar.setBackgroundColor(getResources().getColor(R.color.Yellow));
                    }
                    else if ((hours > 0 || minutes > 30) && currentBarColor != R.color.green)
                    {
                        currentBarColor = R.color.green;
                        statusBar.setBackgroundColor(getResources().getColor(R.color.green));
                    }
                    tvTimeInfo.setText(
                            "Closing in " + hours + " hours and " + minutes + " minutes"
                    );} else {
                    if (currentBarColor != R.color.red)
                    {
                        currentBarColor = R.color.red;
                        statusBar.setBackgroundColor(getResources().getColor(R.color.red));
                    }
                    tvTimeInfo.setText(
                            "Opening in"+ " " + hours + "hours and " + minutes + " minutes");
                }

            }

            public void onFinish() {
                displayStatusBar(getRemainTime());

            }
        }.start();
    }

    private void PhoneCall(String phoneNumber) {
        Intent phoneIntent = new Intent(Intent.ACTION_CALL);
        phoneIntent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(phoneIntent);
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

        public void setOpenHour(int openHour) {
            this.openHour = openHour;
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

        public void setCloseHour(int closeHour) {
            this.closeHour = closeHour;
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

    private class Meal
    {
        String mealTitle;
        private List<String> mainLineItems;
        private List<String> internationalCornerItems;
        private List<TimeOfDay> openTimes;
        private List<TimeOfDay> endTimes;

        public String getMealTitle()
        {
            return mealTitle;
        }

        public void setMealTitle(String mealTitle)
        {
            this.mealTitle = mealTitle;
        }

        public List<String> getMainLineItems()
        {
            return mainLineItems;
        }

        public void setMainLineItems(List<String> mainLineItems)
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

        public List<TimeOfDay> getOpenTimes() {
            return openTimes;
        }

        public void setOpenTimes(List<TimeOfDay> openTimes) {
            this.openTimes = openTimes;
        }

        public List<TimeOfDay> getEndTimes() {
            return endTimes;
        }

        public void setEndTimes(List<TimeOfDay> endTimes) {
            this.endTimes = endTimes;
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

    private class MenuAdapter extends RecyclerView.Adapter
    {
        private List<Meal> mealList;
        private Context context;
        private int heightToAdd;
        private MenuAdapter(Context context, List<Meal> mealList)
        {
            this.context = context;
            this.mealList = mealList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.food_wells_menu_item, viewGroup, false);

            return new ViewHolder(rowView);
        }


        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int i)
        {
            final Meal meal = mealList.get(i);
            final MenuAdapter.ViewHolder menuViewHolder = (ViewHolder) viewHolder;
            menuViewHolder.tvTitle.setText(meal.getMealTitle());
            menuViewHolder.tvTitle.setText(meal.getMealTitle());

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
            public TextView tvTitle;
            public LinearLayout llMainLines;
            public LinearLayout llInternationalCorner;
            public ToggleButton tbViewMore;
            public CardView cvRoot;

            public ViewHolder(View itemView) {
                super(itemView);
                tvTitle = (TextView) itemView.findViewById(R.id.menu_item_tvTitle);
                llMainLines = (LinearLayout) itemView.findViewById(R.id.fragment_cafeteria_llMainLine);
                llInternationalCorner = (LinearLayout) itemView.findViewById(R.id.fragment_cafeteria_llInternationalCorner);
                tbViewMore = (ToggleButton) itemView.findViewById(R.id.fragment_cafeteria_tbViewMore);
                cvRoot = (CardView) itemView.findViewById(R.id.food_wells_menu_item_cvRoot);
            }

        }

        private void loadMenu(MenuAdapter.ViewHolder viewHolder, Meal meal) {

            for (int i = 0; i < meal.getMainLineItems().size(); i++) {
                viewHolder.llMainLines.addView(createFoodItemView(meal.getMainLineItems().get(i)));
            }

            setListViewHeightBasedOnChildren(viewHolder.llMainLines);

            for (int i = 0; i < meal.getInternationalCornerItems().size(); i++) {
                viewHolder.llInternationalCorner.addView(createFoodItemView(meal.getInternationalCornerItems().get(i)));
            }

            setListViewHeightBasedOnChildren(viewHolder.llInternationalCorner);
        }

        /**
         *
         */
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
