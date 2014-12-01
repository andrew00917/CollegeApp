package com.techhab.collegeapp;



import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class RichardsonMenuLunch extends Fragment {
        public static final String ARG_OBJECT = "object";
        public static Fragment createNewInstace() {
            return new RichardsonMenuLunch();
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }

    }
