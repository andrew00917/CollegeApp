package com.techhab.collegeapp;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;




public class RichardsonMenuDinner extends Fragment {
        public static final String ARG_OBJECT = "object";
        public static Fragment createNewInstace() {
            return new RichardsonMenuDinner();
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }

    }

