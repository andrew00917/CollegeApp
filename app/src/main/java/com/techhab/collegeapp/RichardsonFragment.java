package com.techhab.collegeapp;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class RichardsonFragment extends Fragment implements View.OnClickListener {
    public static final String ARG_OBJECT = "object";
    // Buttons Cafeteria
    View v;
    FoodStore Richardson;
    TextView tvTimeInfo;
    ImageView image;
    TextView tvTimeDetailInfo;
    RecyclerView rvMenu;
    ImageButton ibExpandble;
    LinearLayout llHeader;
    private boolean isBreakfastCollapse = true;
    RecyclerView.LayoutManager mLayoutManager;
    Spinner spinner;
    int currentBarColor;


    private static final int RICHARDSON = 1;

    public static Fragment createNewIntace() {
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
        rvMenu = (RecyclerView) v.findViewById(R.id.fragment_cafeteria_rvMenu);
        tvTimeInfo = (TextView) v.findViewById(R.id.fragment_cafeteria_tvInfo);
        tvTimeDetailInfo = (TextView) v.findViewById(R.id.fragment_cafeteria_tvTimeDetail);
        ibExpandble = (ImageButton) v.findViewById(R.id.ibExpandable);
        llHeader = (LinearLayout) v.findViewById(R.id.richardson_status_bar);
        spinner = (Spinner) v.findViewById(R.id.richardson_spinner);
        List<String> days = new ArrayList<>();
        days.add("Today");
        days.add("Tomorrow");
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.add(Calendar.DATE, 2);
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
        String nextDay = dayFormat.format(gregorianCalendar.getTime());
        days.add(nextDay);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, days);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        tvTimeInfo.setOnClickListener(this);
        //todo: fake data for Richardson
        Richardson = new FoodStore();
        Richardson.setStoreName("Richardson Room");
        Richardson.setOpenHour(4);
        Richardson.setCloseHour(15);
        Meal Specials = new Meal();
        Specials.setTitle("Specials");
        Specials.setSubTitle("Sandwich");
        List<String> specialSandwiches = new ArrayList<String>();
        specialSandwiches.add("meat ball");
        specialSandwiches.add("panini");
        specialSandwiches.add("Tuna");
        specialSandwiches.add("item 4");
        specialSandwiches.add("item 5");
        specialSandwiches.add("item 6");
        specialSandwiches.add("item 7");
        Specials.setMainLines(specialSandwiches);
        List<String> specialSoups = new ArrayList<String>();
        specialSoups.add("Tomato soups");
        specialSoups.add("Cheddar Soup");
        Specials.setInternationalCorner(specialSoups);

        Meal lunch = new Meal();
        lunch.setTitle("Lunch");
        List<String> mainLines1 = new ArrayList<String>();
        mainLines1.add("item1");
        mainLines1.add("item2");
        mainLines1.add("item3");
        mainLines1.add("item4");
        mainLines1.add("item5");
        lunch.setMainLines(mainLines1);
        List<String> internationalCorner1 = new ArrayList<String>();
        internationalCorner1.add("item1");
        internationalCorner1.add("item2");
        lunch.setInternationalCorner(internationalCorner1);

        Meal dinner = new Meal();
        dinner.setTitle("Dinner");
        List<String> mainLines2 = new ArrayList<String>();
        mainLines2.add("item1");
        mainLines2.add("item2");
        mainLines2.add("item3");
        mainLines2.add("item4");
        mainLines2.add("item5");
        dinner.setMainLines(mainLines2);
        List<String> internationalCorner2 = new ArrayList<String>();
        internationalCorner2.add("item1");
        internationalCorner2.add("item2");
        dinner.setInternationalCorner(internationalCorner2);

        List<Meal> meals = new ArrayList<Meal>();
        meals.add(Specials);
        meals.add(lunch);
        meals.add(dinner);
        Richardson.setMealList(meals);
        mLayoutManager = new LinearLayoutManager(getActivity());
        rvMenu.setLayoutManager(mLayoutManager);
        rvMenu.setAdapter(new MenuAdapter(getActivity(), meals));
        if (isOpened(Richardson))
        {
            currentBarColor = R.color.green;
           llHeader.setBackgroundColor(getResources().getColor(R.color.green));
            ((ImageView) v.findViewById(R.id.fragment_cafeteria_image)).setImageResource(R.drawable.open_sign);
        }
        else
       {
           currentBarColor = R.color.red;
           llHeader.setBackgroundColor(getResources().getColor(R.color.red));
           ((ImageView) v.findViewById(R.id.fragment_cafeteria_image)).setImageResource(R.drawable.open_sign);
       }
        getRemainTime();
        return v;
    }


    private boolean isOpened(FoodStore foodStore)
    {
        Long currentTime = System.currentTimeMillis();
        long openTime = getTimeOFDay(foodStore.getOpenHour(), foodStore.getOpenMinutes());
        Calendar closeCalendar = Calendar.getInstance();
        closeCalendar.set(Calendar.HOUR_OF_DAY, foodStore.getCloseHour());
        closeCalendar.set(Calendar.MINUTE, foodStore.getCloseMinutes());
        closeCalendar.set(Calendar.SECOND, 0);
        long closedTIme = getTimeOFDay(foodStore.getCloseHour(), foodStore.getCloseMinutes());
        if (currentTime >= openTime && currentTime <= closedTIme)
            return true;
        return false;
    }

    private long getTimeOFDay(int hour, int minute)
    {
        Calendar openCalendar = Calendar.getInstance();
        openCalendar.set(Calendar.HOUR_OF_DAY, hour);
        openCalendar.set(Calendar.MINUTE, minute);
        openCalendar.set(Calendar.SECOND, 0);
        return openCalendar.getTimeInMillis();
    }



    private void getRemainTime()
    {
        long openMillis = getTimeOFDay(Richardson.getOpenHour(), Richardson.getOpenMinutes());
        long closeMillis = getTimeOFDay(Richardson.getCloseHour(), Richardson.getCloseMinutes());
        Time TimeNow = new Time();
        TimeNow.setToNow(); // set the date to Current Time
        TimeNow.normalize(true);
        long nowMillis = TimeNow.toMillis(true);
        long millisset = 0;
        if (nowMillis >= openMillis && nowMillis <= closeMillis)
        {
            millisset = closeMillis - nowMillis;

        }
        else
        {
            millisset = openMillis - nowMillis > 0 ? openMillis - nowMillis : openMillis - nowMillis + 86400*1000;

        }

        new CountDownTimer(millisset, 1000) {
            public void onTick(long millisUntilFinished) {

                int days = (int) ((millisUntilFinished / 1000) / 86400);
                int hours = (int) (((millisUntilFinished / 1000) - (days
                        * 86400)) / 3600);
                int minutes = (int) (((millisUntilFinished / 1000) - ((days
                        * 86400) + (hours * 3600))) / 60);
                int seconds = (int) ((millisUntilFinished / 1000) % 60);

                if (isOpened(Richardson))
                {
                    if (hours == 0 && minutes <= 30 && currentBarColor != R.color.Yellow)
                    {
                        currentBarColor = R.color.Yellow;
                        llHeader.setBackgroundColor(getResources().getColor(R.color.Yellow));
                    }
                    else if ((hours > 0 || minutes > 30) && currentBarColor != R.color.green)
                    {
                        currentBarColor = R.color.green;
                        llHeader.setBackgroundColor(getResources().getColor(R.color.green));
                    }
                    tvTimeInfo.setText(
                         "Closing in " + hours + " hours and " + minutes + " minutes"
                );}
                else
                {
                    if (currentBarColor != R.color.red)
                    {
                        currentBarColor = R.color.red;
                        llHeader.setBackgroundColor(getResources().getColor(R.color.red));
                    }
                    tvTimeInfo.setText(
                            "Opening in"+ " " + hours + "hours and " + minutes + " minutes");
                }

            }

            public void onFinish() {
                getRemainTime();

            }
        }.start();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fragment_cafeteria_tvInfo:
                if (tvTimeDetailInfo.getVisibility() == View.VISIBLE)
                {
                    tvTimeDetailInfo.setVisibility(View.GONE);
                    ibExpandble.setImageResource(R.drawable.ic_action_expand);
                }
                else
                {
                    tvTimeDetailInfo.setVisibility(View.VISIBLE);
                    ibExpandble.setImageResource(R.drawable.ic_action_collapse);
                }

        }
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
        String subTitle;
        private List<String> mainLines;
        private List<String> internationalCorner;

        public String getTitle()
        {
            return mealTitle;
        }

        public String getSubTitle()
        {
            return subTitle;
        }
        public void setTitle(String mealTitle)
        {
            this.mealTitle = mealTitle;
        }
        public void setSubTitle(String subTitle)
        {
            this.subTitle = subTitle;
        }

        public List<String> getMainLineItems()
        {
            return mainLines;
        }

        public void setMainLines(List<String> mainLines)
        {
            this.mainLines = mainLines;
        }

        public List<String> getInternationalCornerItems()
        {
            return internationalCorner;
        }

        public void setInternationalCorner(List<String> internationalCorner)
        {
            this.internationalCorner = internationalCorner;
        }
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
            menuViewHolder.tvTitle.setText(meal.getTitle());
            menuViewHolder.tvTitle1.setText(meal.getSubTitle());
            menuViewHolder.tvTitle2.setText(meal.getSubTitle());

            loadMenu(menuViewHolder, meal);
            menuViewHolder.tbViewMore.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                    {

                        HeightAnimation animation = new HeightAnimation(((ViewHolder) viewHolder).llMainLines,
                                getHeightToAdd(((ViewHolder) viewHolder).llMainLines, true), true);
                        animation.setDuration(300);
                        ((ViewHolder) viewHolder).llMainLines.startAnimation(animation);
                        HeightAnimation animation1 = new HeightAnimation(((ViewHolder) viewHolder).llInternationalCorner,
                                getHeightToAdd(((ViewHolder) viewHolder).llInternationalCorner, true), true);
                        animation1.setDuration(300);
                        ((ViewHolder) viewHolder).llInternationalCorner.startAnimation(animation1);

                    }
                    else
                    {
                        HeightAnimation animation = new HeightAnimation(((ViewHolder) viewHolder).llMainLines,
                                getHeightToAdd(((ViewHolder) viewHolder).llMainLines, false), false);
                        animation.setDuration(300);
                        ((ViewHolder) viewHolder).llMainLines.startAnimation(animation);
                        HeightAnimation animation1 = new HeightAnimation(((ViewHolder) viewHolder).llInternationalCorner,
                                getHeightToAdd(((ViewHolder) viewHolder).llInternationalCorner, false), false);
                        animation1.setDuration(300);
                        ((ViewHolder) viewHolder).llInternationalCorner.startAnimation(animation1);
                    }
                }
            });
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

    /*private class MySpinnerAdapter extends SpinnerAdapter {

        public MySpinnerAdapter() {

        }

    }*/

}
