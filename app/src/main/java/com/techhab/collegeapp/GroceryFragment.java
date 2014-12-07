package com.techhab.collegeapp;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;


public class GroceryFragment extends Fragment implements View.OnClickListener {
    public static final String ARG_OBJECT = "object";
    // Buttons Cafeteria
    private RelativeLayout btn_grocery_walgreens;
    private RelativeLayout btn_grocery_farmersMarket;
    private RelativeLayout btn_grocery_meijers;
    private RelativeLayout btn_grocery_hardingsMarket;
    private RelativeLayout btn_grocery_irvingMarket;
    private RelativeLayout btn_grocery_laMexicanaMarket;
    private RelativeLayout btn_grocery_pacificRimFoods;
    View v;


    public static Fragment createNewIntace() {
        GroceryFragment fragment = new GroceryFragment();
        Bundle arg = new Bundle();
        fragment.setArguments(arg);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_grocery, parent, false);
        btn_grocery_walgreens = (RelativeLayout) v.findViewById(R.id.btn_grocery_walgreens);
        btn_grocery_walgreens.setOnClickListener(this);
        btn_grocery_farmersMarket = (RelativeLayout) v.findViewById(R.id.btn_grocery_farmersMarket);
        btn_grocery_farmersMarket.setOnClickListener(this);
        btn_grocery_meijers = (RelativeLayout) v.findViewById(R.id.btn_grocery_meijers);
        btn_grocery_meijers.setOnClickListener(this);
        btn_grocery_hardingsMarket = (RelativeLayout) v.findViewById(R.id.btn_grocery_hardingsMarket);
        btn_grocery_hardingsMarket.setOnClickListener(this);
        btn_grocery_irvingMarket = (RelativeLayout) v.findViewById(R.id.btn_grocery_irvingMarket);
        btn_grocery_irvingMarket.setOnClickListener(this);
        btn_grocery_laMexicanaMarket = (RelativeLayout) v.findViewById(R.id.btn_grocery_laMexicanaMarket);
        btn_grocery_laMexicanaMarket.setOnClickListener(this);
        btn_grocery_pacificRimFoods= (RelativeLayout) v.findViewById(R.id.btn_grocery_pacificRimFoods);
        btn_grocery_pacificRimFoods.setOnClickListener(this);
        // Example of getting Button view:
        // Button b = (Button) v.findViewById(R.id.BUTTON_ID);
        TextView WalgreensStatus = (TextView) v.findViewById(R.id.tvWalgreensStatus);
        TextView FarmersMarketStatus = (TextView) v.findViewById(R.id.tvFarmersMarketStatus);
        TextView MeijersStatus = (TextView) v.findViewById(R.id.tvMeijersStatus);
        TextView HardingsMarketStatus = (TextView) v.findViewById(R.id.tvHardingsMarketStatus);
        TextView IravingMarketStatus = (TextView) v.findViewById(R.id.tvIravingMarketStatus);
        TextView LaMexicanaMarket = (TextView) v.findViewById(R.id.tvlaMexicanaMarket);
        TextView PacificRimFoodStatus = (TextView) v.findViewById(R.id.tvpacificRimFoodStatus);
        if (!isOpened())
        {
            WalgreensStatus.setText("Closed");
            FarmersMarketStatus.setText("Closed");
            MeijersStatus.setText("Closed");
            HardingsMarketStatus.setText("Closed");
            IravingMarketStatus.setText("Closed");
            LaMexicanaMarket.setText("Closed");
            PacificRimFoodStatus.setText("Closed");
            WalgreensStatus.setTextColor(getResources().getColor(R.color.red));
            FarmersMarketStatus.setTextColor(getResources().getColor(R.color.red));
            MeijersStatus.setTextColor(getResources().getColor(R.color.red));
            HardingsMarketStatus.setTextColor(getResources().getColor(R.color.red));
            IravingMarketStatus.setTextColor(getResources().getColor(R.color.red));
            LaMexicanaMarket.setTextColor(getResources().getColor(R.color.red));
            PacificRimFoodStatus.setTextColor(getResources().getColor(R.color.red));
        }

        return v;
    }

    private boolean isOpened()
    {
        Long currentTime = System.currentTimeMillis();
        Calendar openCalendar = Calendar.getInstance();
        openCalendar.set(Calendar.HOUR_OF_DAY,8);
        openCalendar.set(Calendar.MINUTE,0);
        openCalendar.set(Calendar.SECOND, 0);
        long openTime = openCalendar.getTimeInMillis();
        Calendar closeCalendar = Calendar.getInstance();
        closeCalendar.set(Calendar.HOUR_OF_DAY, 22);
        closeCalendar.set(Calendar.MINUTE, 0);
        closeCalendar.set(Calendar.SECOND, 0);
        long closedTIme = closeCalendar.getTimeInMillis();
        if (currentTime >= openTime && currentTime <= closedTIme)
            return true;
        return false;
    }

    @Override
    public void onClick(View v) {


    }



}
