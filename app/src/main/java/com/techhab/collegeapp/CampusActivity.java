package com.techhab.collegeapp;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;

public class CampusActivity extends BaseObservableRecyclerActivity {

    @Override
    protected String getToolbarTitle() {
        return getResources().getString(R.string.main_menu_01);
    }

    @Override
    protected String[] getTabTitles() {
        return new String[]{"Common", "Dormitories", "Departments"};
    }

    @Override
    protected Class<? extends Fragment> getFragmentClass() {
        return CampusBuildingsFragment.class;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_campus;
    }
}
