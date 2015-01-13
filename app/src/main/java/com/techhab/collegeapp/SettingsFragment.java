package com.techhab.collegeapp;

import android.app.Fragment;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jhchoe on 11/01/15.
 */
public class SettingsFragment extends PreferenceFragment {

    /**
     *  Reference:
     *  http://developer.android.com/guide/topics/ui/settings.html#DefiningPrefs
     */

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }
}
