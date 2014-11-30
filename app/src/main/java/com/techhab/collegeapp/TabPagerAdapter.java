package com.techhab.collegeapp;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class TabPagerAdapter extends FragmentStatePagerAdapter {
    private static final int NUMBER_PAGES = 3;
    private final Context context;

    public TabPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return DetailCafeteriaFragment.createNewIntace();
            case 1:
               return GroceryFragment.createNewInstace();
            case 2:
                return DiscountFragment.createNewInstace();
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.title_section1).toUpperCase();
            case 1:
                return context.getString(R.string.title_section2).toUpperCase();
            case 2:
                return context.getString(R.string.title_section3).toUpperCase();
            default:
                return "";
        }
    }

     @Override
    public int getCount() {
        return NUMBER_PAGES; //No of Tabs
    }
}
