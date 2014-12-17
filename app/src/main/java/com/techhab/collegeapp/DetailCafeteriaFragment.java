package com.techhab.collegeapp;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import org.w3c.dom.Text;

import java.util.*;


public class DetailCafeteriaFragment extends Fragment implements View.OnClickListener {
    public static final String ARG_OBJECT = "object";
    // Buttons Cafeteria
    View v;
    LinearLayout llMainLines;
    LinearLayout llMainLines1;
    LinearLayout llInternationalCorners;
    LinearLayout llInternationalCorners1;
    FoodStore cafeteriaStore;
    TextView tvViewMore;
    TextView tvViewMore1;
    TextView tvTimeInfo;
    TextView tvTimeDetailInfo;
    private boolean isBreakfastCollapse = true;
    private boolean isLunchCollapse = true;



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
        llMainLines = (LinearLayout) v.findViewById(R.id.fragment_cafeteria_llMainLine);
        llMainLines1 = (LinearLayout) v.findViewById(R.id.fragment_cafeteria_llMainLine1);
        llInternationalCorners = (LinearLayout) v.findViewById(R.id.fragment_cafeteria_llInternationalCorner);
        llInternationalCorners1 = (LinearLayout) v.findViewById(R.id.fragment_cafeteria_llInternationalCorner1);
        tvViewMore = (TextView) v.findViewById(R.id.fragment_cafeteria_tvViewMore);
        tvViewMore1 = (TextView) v.findViewById(R.id.fragment_cafeteria_tvViewMore1);
        tvTimeInfo = (TextView) v.findViewById(R.id.fragment_cafeteria_tvInfo);
        tvTimeDetailInfo = (TextView) v.findViewById(R.id.fragment_cafeteria_tvTimeDetail);
        tvViewMore.setOnClickListener(this);
        tvViewMore1.setOnClickListener(this);

        tvTimeInfo.setOnClickListener(this);
        //todo: fake data for cafeteriaStore
        cafeteriaStore = new FoodStore();
        cafeteriaStore.setStoreName("Cafeteria");
        cafeteriaStore.setOpenHour(8);
        cafeteriaStore.setCloseHour(22);
        List<String> mainLines = new ArrayList<String>();
        mainLines.add("Pancakes");
        mainLines.add("Scrambled Eggs");
        mainLines.add("Bacon");
        mainLines.add("Kfc");
        mainLines.add("Pate");
        cafeteriaStore.setMainLines(mainLines);
        List<String> mainLines1 = new ArrayList<String>();
        mainLines1.add("item1");
        mainLines1.add("item2");
        mainLines1.add("item3");
        mainLines1.add("item4");
        mainLines1.add("item5");
        cafeteriaStore.setMainLines1(mainLines1);
        List<String> internationalCorner = new ArrayList<String>();
        internationalCorner.add("Pho soups");
        internationalCorner.add("Dumpling");
        cafeteriaStore.setInternationalCorner(internationalCorner);
        List<String> internationalCorner1 = new ArrayList<String>();
        internationalCorner1.add("item1");
        internationalCorner1.add("item2");
        cafeteriaStore.setInternationalCorner1(internationalCorner1);
        collapseBreakfastMenu();


        return v;
    }

    private void collapseBreakfastMenu() {
        llMainLines.removeAllViews();
        llInternationalCorners.removeAllViews();
        int maxShortLength = cafeteriaStore.getMainLines().size() > 3 ? 3 : cafeteriaStore.getMainLines().size();
        for (int i = 0; i < maxShortLength; i ++)
        {
            llMainLines.addView(createFoodItemView(cafeteriaStore.getMainLines().get(i)));

        }
        maxShortLength = cafeteriaStore.getInternationalCorner().size() > 3 ? 3 : cafeteriaStore.getInternationalCorner().size();
        for (int i = 0; i < maxShortLength; i++)
        {
            llInternationalCorners.addView(createFoodItemView(cafeteriaStore.getInternationalCorner().get(i)));
        }
        isBreakfastCollapse = true;
    }
    private void collapseLunchMenu() {
        llMainLines1.removeAllViews();
        llInternationalCorners1.removeAllViews();
        int maxShortLength = cafeteriaStore.getMainLines1().size() > 3 ? 3 : cafeteriaStore.getMainLines1().size();
        for (int i = 0; i < maxShortLength; i ++)
        {
            llMainLines1.addView(createFoodItemView(cafeteriaStore.getMainLines1().get(i)));

        }
        maxShortLength = cafeteriaStore.getInternationalCorner1().size() > 3 ? 3 : cafeteriaStore.getInternationalCorner1().size();
        for (int i = 0; i < maxShortLength; i++)
        {
            llInternationalCorners1.addView(createFoodItemView(cafeteriaStore.getInternationalCorner1().get(i)));
        }
        isLunchCollapse = true;
    }

    private void openBreakfastMenu() {
        llMainLines.removeAllViews();
        for (String foodItem : cafeteriaStore.getMainLines())
        {
            llMainLines.addView(createFoodItemView(foodItem));
        }

        llInternationalCorners.removeAllViews();
        for (String foodItem : cafeteriaStore.getInternationalCorner())
        {
            llInternationalCorners.addView(createFoodItemView(foodItem));
        }
        isBreakfastCollapse = false;
    }
    private void openLunchMenu() {
        llMainLines1.removeAllViews();
        for (String foodItem1 : cafeteriaStore.getMainLines1())
        {
            llMainLines1.addView(createFoodItemView(foodItem1));
        }

        llInternationalCorners1.removeAllViews();
        for (String foodItem1 : cafeteriaStore.getInternationalCorner1())
        {
            llInternationalCorners1.addView(createFoodItemView(foodItem1));
        }
        isLunchCollapse = false;
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


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fragment_cafeteria_tvViewMore:
                if (isBreakfastCollapse)
                {
                    openBreakfastMenu();
                }
                else
                {
                    collapseBreakfastMenu();
                }
                break;
            case R.id.fragment_cafeteria_tvViewMore1:
                if (isLunchCollapse)
                {
                    openLunchMenu();
                }
                else
                {
                    collapseLunchMenu();
                }
                break;
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
        tvFoodItem.setTextSize(18);
        return tvFoodItem;
    }

    private class FoodStore
    {
        int openHour;
        int openMinutes;
        int closeHour;
        int closeMinutes;
        String storeName;
        List<String> mainLines;
        List<String> mainLines1;
        List<String> internationalCorner;
        List<String> internationalCorner1;


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

        public List<String> getMainLines() {
            return mainLines;
        }
        public List<String> getMainLines1() {
            return mainLines1;
        }
        public void setMainLines(List<String> mainLines) {
            this.mainLines = mainLines;
        }
        public void setMainLines1(List<String> mainLines1) {
            this.mainLines1 = mainLines1;
        }
        public List<String> getInternationalCorner() {
            return internationalCorner;
        }
        public List<String> getInternationalCorner1() {
            return internationalCorner1;
        }
        public void setInternationalCorner(List<String> internationalCorner) {
            this.internationalCorner = internationalCorner;
        }
        public void setInternationalCorner1(List<String> internationalCorner1) {
            this.internationalCorner1 = internationalCorner1;
        }
    }

}
