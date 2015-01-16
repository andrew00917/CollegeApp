package com.techhab.collegeapp;


import android.app.ActionBar;
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
        Subtitle sandwiches = new Subtitle();
        sandwiches.setName("Sandwiches");
        sandwiches.setItems(Arrays.asList("item 1"));
        Subtitle soups = new Subtitle();
        soups.setName("Soups");
        soups.setItems(Arrays.asList("item1", "item2", "item3", "item4", "item5", "item6"));
        specials.getSubtitles().add(sandwiches);
        specials.getSubtitles().add(soups);
        specials.setOpenTimes(Arrays.asList(new TimeOfDay(10, 0), new TimeOfDay(10, 0), new TimeOfDay(15, 0)));
        specials.setEndTimes(Arrays.asList(new TimeOfDay(24, 0), new TimeOfDay(18, 0), new TimeOfDay(24, 0)));

        Meal groups = new Meal();
        groups.setMealTitle("One Meal Swipe = One Choice From Each Group");
        Subtitle groupA = new Subtitle();
        groupA.setName("Group A");
        groupA.setItems(Arrays.asList("item 1","item2", "item3"));
        Subtitle groupB = new Subtitle();
        groupB.setName("Group B");
        groupB.setItems(Arrays.asList("item1", "item2", "item3", "item4"));
        groups.getSubtitles().add(groupA);
        groups.getSubtitles().add(groupB);

        groups.setOpenTimes(Arrays.asList(new TimeOfDay(10, 0), new TimeOfDay(10, 0), new TimeOfDay(15, 0)));
        groups.setEndTimes(Arrays.asList(new TimeOfDay(24, 0), new TimeOfDay(18, 0), new TimeOfDay(24, 0)));


        List<Meal> meals = new ArrayList<Meal>();
        meals.add(specials);
        meals.add(groups);


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
        tvFoodItem.setPadding(20,0,0,0);
        tvFoodItem.setTextColor(getResources().getColor(R.color.primary_text_default_material_light));

        return tvFoodItem;
    }

    private TextView createSubtitleTextView(String subtitle)
    {
        TextView tvSubtitle = new TextView(getActivity());
        tvSubtitle.setText(subtitle);
        tvSubtitle.setTextColor(getResources().getColor(R.color.secondary_text_default_material_light));
        return tvSubtitle;

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
        private List<Subtitle> subtitles = new ArrayList<>();
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

        public List<Subtitle> getSubtitles() {
            return subtitles;
        }

        public void setSubtitles(List<Subtitle> subtitles) {
            this.subtitles = subtitles;
        }
    }

    private class Subtitle
    {
        String name;
        List<String> items;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getItems() {
            return items;
        }

        public void setItems(List<String> items) {
            this.items = items;
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
            View rowView = inflater.inflate(R.layout.richarson_food_wells_menu_item, viewGroup, false);
            return new ViewHolder(rowView, menuList.get(i));
        }


        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int i)
        {
            final Meal meal = menuList.get(i);
            final MenuAdapter.ViewHolder menuViewHolder = (ViewHolder) viewHolder;
            menuViewHolder.tvTitle.setText(meal.getMealTitle());

            loadMenu(menuViewHolder, meal);
            menuViewHolder.tbViewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    menuViewHolder.tbViewMore.setEnabled(false);

                    if (menuViewHolder.tbViewMore.isChecked())
                    {
                        expandCardView((ViewHolder) viewHolder, meal);

                    } else {
                        collapseCardView((ViewHolder) viewHolder, meal);
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

        private void expandCardView(ViewHolder viewHolder, Meal meal) {
            for (int i = 0; i < meal.getSubtitles().size() ; i++)
            {
                HeightAnimation animation = new HeightAnimation(viewHolder.subtitleViewList.get(i),
                        getHeightToAdd(viewHolder.subtitleViewList.get(i), true), true);
                animation.setDuration(300);
                viewHolder.subtitleViewList.get(i).startAnimation(animation);
            }
        }

        private void collapseCardView(ViewHolder viewHolder, Meal meal) {
            for (int i = 0; i < meal.getSubtitles().size() ; i++)
            {
                HeightAnimation animation = new HeightAnimation(viewHolder.subtitleViewList.get(i),
                        getHeightToAdd(viewHolder.subtitleViewList.get(i), false), false);
                animation.setDuration(300);
                viewHolder.subtitleViewList.get(i).startAnimation(animation);
            }

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
            public ToggleButton tbViewMore;
            public List<LinearLayout> subtitleViewList = new ArrayList<>();
            public LinearLayout llContent;

            public ViewHolder(View itemView, Meal meal) {
                super(itemView);
                tvTitle = (TextView) itemView.findViewById(R.id.menu_item_tvTitle);
                tbViewMore = (ToggleButton) itemView.findViewById(R.id.fragment_cafeteria_tbViewMore);
                llContent = (LinearLayout) itemView.findViewById(R.id.food_wells_menu_item_llContent);
                for (Subtitle subtitle : meal.getSubtitles())
                {
                    LinearLayout subtitleLayout = new LinearLayout(getActivity());
                    subtitleLayout.setOrientation(LinearLayout.VERTICAL);
                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    subtitleLayout.setLayoutParams(layoutParams);
                    llContent.addView(subtitleLayout);
                    subtitleViewList.add(subtitleLayout);

                }
            }

        }

        private void loadMenu(MenuAdapter.ViewHolder viewHolder, Meal meal) {

            for (int j = 0; j < meal.getSubtitles().size(); j ++)
            {
                Subtitle subtitle = meal.getSubtitles().get(j);
                viewHolder.subtitleViewList.get(j).addView(createSubtitleTextView(subtitle.getName()));
                for (int i = 0; i < subtitle.getItems().size(); i++) {
                    viewHolder.subtitleViewList.get(j).addView(createFoodItemView(subtitle.getItems().get(i)));
                }

                setListViewHeightBasedOnChildren(viewHolder.subtitleViewList.get(j));

            }

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
            starterHeight = (linearLayout.getChildCount() <3 ? linearLayout.getChildCount() : 4) * heightOfChild;

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
            return heightOfChild * (parent.getChildCount() - 4);


    }

    private int convertToPixel(int dp)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
