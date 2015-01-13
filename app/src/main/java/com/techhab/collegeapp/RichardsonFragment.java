package com.techhab.collegeapp;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RichardsonFragment extends Fragment {
    public static final String ARG_OBJECT = "object";
    private static int selectedPosition = 0;
    private static final int MF_INDEX = 0;
    private static final int SATURDAY_INDEX = 1;
    private static final int SUNDAY_INDEX = 2;
    // Buttons Cafeteria
    View v;
    FoodStore Richardson;
    TextView tvTimeInfo;
    private ImageView statusBarArrow;
    private LinearLayout statusBarExtendedInfoFrame;
    RecyclerView rvMenu;
    private LinearLayout statusBar;
    private boolean isBreakfastCollapse = true;
    RecyclerView.LayoutManager mLayoutManager;
    int currentBarColor;
    private boolean isExpanded = false;


    private static final int RICHARDSON = 1;

    public static Fragment createNewInstance() {
        RichardsonFragment fragment = new RichardsonFragment();
        Bundle arg = new Bundle();
        fragment.setArguments(arg);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_richardson, parent, false);
        rvMenu = (RecyclerView) v.findViewById(R.id.fragment_richardson_rvMenu);
        tvTimeInfo = (TextView) v.findViewById(R.id.fragment_richardson_tvInfo);
        statusBarExtendedInfoFrame = (LinearLayout) v.findViewById(R.id.status_bar_extended_info_frame);
        statusBar = (LinearLayout) v.findViewById(R.id.richrdson_status_bar);
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

        setupData();

        mLayoutManager = new LinearLayoutManager(getActivity());
        rvMenu.setLayoutManager(mLayoutManager);
        rvMenu.setAdapter(new MenuAdapter(getActivity(), Richardson.getMealList()));

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



    private void setupData() {
        //todo: fake data for cafe
        Richardson = new FoodStore();
        Richardson.setStoreName("Richardson");
        Meal specials = new Meal();
        specials.setMealTitle("Specials");
        specials.setSubTitle("Sandwiches");
        List<String> breakfastMainLines = new ArrayList<String>();
        specials.setSubTitle2("Soups");
        breakfastMainLines.add("item 1");
        specials.setMainLineItems(breakfastMainLines);
        specials.setInternationalCornerItems(Arrays.asList("1", "2", "3", "4"));
        specials.setOpenTimes(Arrays.asList(new TimeOfDay(10, 0), new TimeOfDay(10, 0),new TimeOfDay(15, 0)));
        specials.setEndTimes(Arrays.asList(new TimeOfDay(24, 0), new TimeOfDay(18, 0), new TimeOfDay(24, 0)));


        List<Meal> meals = new ArrayList<Meal>();
        meals.add(specials);



        Richardson.setMealList(meals);
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
        for (Meal meal : Richardson.getMealList())
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
        int nextDayIndex = dayIndex == 2 ? 0 : dayIndex++;
        return currentTime - getOpenTime(nextDayIndex) - 86400*1000;
    }


    private long getOpenTime(int dayIndex)
    {
        for (Meal meal : Richardson.getMealList()) {
            if (meal.getOpenTimes().get(dayIndex) != null) {
                return meal.getOpenTimes().get(dayIndex).getTimeInMillis();
            }
        }
        return 0;

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

    private class Meal
    {
        String mealTitle;
        String subTitle;
        String subTitle2;
        private List<String> mainLineItems;
        private List<String> internationalCornerItems;
        private List<TimeOfDay> openTimes;
        private List<TimeOfDay> endTimes;

        public String getMealTitle()
        {
            return mealTitle;
        }

        public String getSubTitle()
        {
            return subTitle;
        }
        public String getSubTitle2()
        {
            return subTitle2;
        }
        public void setMealTitle(String mealTitle)
        {
            this.mealTitle = mealTitle;
        }
        public void setSubTitle(String subTitle)
        {
            this.subTitle = subTitle;
        }
        public void setSubTitle2(String subTitle2)
        {
            this.subTitle2 = subTitle2;
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
        private List<Meal> menuList;
        private Context context;
        private int heightToAdd;
        private MenuAdapter(Context context, List<Meal> menuList)
        {
            this.context = context;
            this.menuList = menuList;
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
            final Meal meal = menuList.get(i);
            final MenuAdapter.ViewHolder menuViewHolder = (ViewHolder) viewHolder;
            menuViewHolder.tvTitle.setText(meal.getMealTitle());
            menuViewHolder.tvTitle1.setText(meal.getSubTitle());
            menuViewHolder.tvTitle2.setText(meal.getSubTitle2());

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
            return menuList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView tvTitle;
            public TextView tvTitle1;
            public TextView tvTitle2;

            public LinearLayout llMainLines;
            public LinearLayout llInternationalCorner;
            public ToggleButton tbViewMore;

            public ViewHolder(View itemView) {
                super(itemView);
                tvTitle = (TextView) itemView.findViewById(R.id.menu_item_tvTitle);
                tvTitle1 = (TextView) itemView.findViewById(R.id.fragment_cafeteria_tvMainLine);
                tvTitle2 = (TextView) itemView.findViewById(R.id.fragment_cafeteria_tvInternationalCorner);
                llMainLines = (LinearLayout) itemView.findViewById(R.id.fragment_cafeteria_llMainLine);
                llInternationalCorner = (LinearLayout) itemView.findViewById(R.id.fragment_cafeteria_llInternationalCorner);
                tbViewMore = (ToggleButton) itemView.findViewById(R.id.fragment_cafeteria_tbViewMore);
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

    private int convertToPixel(int dp)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
