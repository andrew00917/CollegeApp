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
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.*;


public class DetailCafeteriaFragment extends Fragment implements View.OnClickListener {
    public static final String ARG_OBJECT = "object";
    // Buttons Cafeteria
    View v;
    FoodStore cafeteriaStore;
    TextView tvTimeInfo;
    TextView tvTimeInfo1;
    TextView tvTimeDetailInfo;
    RecyclerView rvMenu;
    private boolean isBreakfastCollapse = true;
    RecyclerView.LayoutManager mLayoutManager;


    private static final int RICHARDSON = 1;

    public static Fragment createNewIntace() {
        DetailCafeteriaFragment fragment = new DetailCafeteriaFragment();
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
        //tvTimeInfo1 = (TextView) v.findViewById(R.id.fragment_cafeteria_tvInfo1);
        tvTimeDetailInfo = (TextView) v.findViewById(R.id.fragment_cafeteria_tvTimeDetail);

        tvTimeInfo.setOnClickListener(this);
        //todo: fake data for cafe
        cafeteriaStore = new FoodStore();
        cafeteriaStore.setStoreName("Cafeteria");
        cafeteriaStore.setOpenHour(4);
        cafeteriaStore.setCloseHour(22);
        Meal breakfast = new Meal();
        breakfast.setMealTitle("Breakfast");
        List<String> breakfastMainLines = new ArrayList<String>();
        breakfastMainLines.add("Pancakes");
        breakfastMainLines.add("Scrambled Eggs");
        breakfastMainLines.add("Bacon");
        breakfastMainLines.add("Kfc");
        breakfastMainLines.add("Pate");
        breakfast.setMainLineItems(breakfastMainLines);
        List<String> breakfastInternationalCorner = new ArrayList<String>();
        breakfastInternationalCorner.add("Pho soups");
        breakfastInternationalCorner.add("Dumpling");
        breakfast.setInternationalCornerItems(breakfastInternationalCorner);

        Meal lunch = new Meal();
        lunch.setMealTitle("Lunch");
        List<String> mainLines1 = new ArrayList<String>();
        mainLines1.add("item1");
        mainLines1.add("item2");
        mainLines1.add("item3");
        mainLines1.add("item4");
        mainLines1.add("item5");
        lunch.setMainLineItems(mainLines1);
        List<String> internationalCorner1 = new ArrayList<String>();
        internationalCorner1.add("item1");
        internationalCorner1.add("item2");
        lunch.setInternationalCornerItems(internationalCorner1);
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
        dinner.setInternationalCornerItems(internationalCorner2);

        List<Meal> meals = new ArrayList<Meal>();
        meals.add(breakfast);
        meals.add(lunch);
        meals.add(dinner);
        cafeteriaStore.setMealList(meals);
        mLayoutManager = new LinearLayoutManager(getActivity());
        rvMenu.setLayoutManager(mLayoutManager);
        rvMenu.setAdapter(new MenuAdapter(getActivity(), meals));
        /*if (isOpened(cafeteriaStore))
        {
            v.findViewById(R.id.fragment_careteria_llHeader).setBackgroundColor(getResources().getColor(R.color.green));
            ((TextView) v.findViewById(R.id.fragment_cafeteria_tvInfo1)).setText(R.string.open);
        }
        else
        {
            v.findViewById(R.id.fragment_careteria_llHeader).setBackgroundColor(getResources().getColor(R.color.red));
            ((TextView) v.findViewById(R.id.fragment_cafeteria_tvInfo1)).setText(R.string.closed);
        }*/
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
        long openMillis = getTimeOFDay(cafeteriaStore.getOpenHour(), cafeteriaStore.getOpenMinutes());
        long closeMillis = getTimeOFDay(cafeteriaStore.getCloseHour(), cafeteriaStore.getCloseMinutes());
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


                tvTimeInfo.setText(
                        " "+ "for " + hours + " hours and " + minutes + " minutes"
                             );

            }

            public void onFinish() {
                tvTimeInfo.setText("done");
            }
        }.start();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fragment_cafeteria_tvInfo:
                tvTimeDetailInfo.setVisibility(tvTimeDetailInfo.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);



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
        private List<String> mainLineItems;
        private List<String> internationalCornerItems;

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
//            collapseMenu(menuViewHolder, meal);
            menuViewHolder.tbViewMore.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                    {
                        HeightAnimation animation = new HeightAnimation(((ViewHolder) viewHolder).llMainLines,
                                heightToAdd, true);
                        animation.setDuration(300);
                        ((ViewHolder) viewHolder).llMainLines.startAnimation(animation);
                    }
                    else
                    {
                        HeightAnimation animation = new HeightAnimation(((ViewHolder) viewHolder).llMainLines,
                                heightToAdd, false);
                        animation.setDuration(300);
                        ((ViewHolder) viewHolder).llMainLines.startAnimation(animation);
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
            return mealList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView tvTitle;
            public LinearLayout llMainLines;
            public LinearLayout llInternationalCorner;
            public ToggleButton tbViewMore;

            public ViewHolder(View itemView) {
                super(itemView);
                tvTitle = (TextView) itemView.findViewById(R.id.menu_item_tvTitle);
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
        }

        /**
         *
         */
        public void setListViewHeightBasedOnChildren(LinearLayout linearLayout) {
            if (linearLayout == null) {
                // pre-condition
                return;
            }

            //TODO check to see if the linearlayout has at least 3 children before any of this
            //TODO code.

            View listItem = linearLayout.getChildAt(0);
            listItem.measure(0, 0);
            int heightOfChild = listItem.getMeasuredHeight();

            int starterHeight = (heightOfChild * 3);

            heightToAdd = (heightOfChild) * (linearLayout.getChildCount() - 3);

            /*for (int i = 0; i < 3; i++) {
                listItem.measure(0, 0);
                heightToAdd += listItem.getMeasuredHeight();
            }*/

            ViewGroup.LayoutParams params = linearLayout.getLayoutParams();
            params.height = starterHeight;
            linearLayout.setLayoutParams(params);
            linearLayout.requestLayout();
        }
    }

}
