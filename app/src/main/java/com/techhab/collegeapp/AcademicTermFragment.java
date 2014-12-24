package com.techhab.collegeapp;



import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.zip.Inflater;
public class AcademicTermFragment extends Fragment {

    public static final String ARG_OBJECT = "object";
    public static final String ARG_YEAR = "academic_year";
    public static final String ARG_TERM = "academic_term";

    View v;

    private TableLayout table;

    // @formatter:off
    private static final int[] CONTENT = new int[] {
            R.array.Fall,
            R.array.Winter,
            R.array.Spring
    };
    private static final int[] DATE_2014 = new int[]{
            R.array.Fall_2014,
            R.array.Winter_2014,
            R.array.Spring_2014
    };
    // @formatter:on

    public AcademicTermFragment() {
        // Required Empty Constructor
    }

    public Fragment createNewInstance() {
        Fragment fragment = new AcademicTermFragment();
        Bundle arg = new Bundle();
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_academic_term, container, false);

        table = (TableLayout) v.findViewById(R.id.table_layout);

        int term = getArguments().getInt(ARG_TERM);
        String[] content = getActivity().getResources().getStringArray(CONTENT[term]);

        int year = Integer.parseInt(getArguments().getString(ARG_YEAR));
        String[] date;
        switch (year) {
            case 2014:
                date  = getActivity().getResources().getStringArray(DATE_2014[term]);
                break;
            case 2015:
                date  = getActivity().getResources().getStringArray(DATE_2014[term]);
                break;
            case 2016:
                date  = getActivity().getResources().getStringArray(DATE_2014[term]);
                break;
            default:
                date  = getActivity().getResources().getStringArray(DATE_2014[term]);
                break;
        }

        TableRow.LayoutParams rowParam = new TableRow.LayoutParams();
        rowParam.setMargins(1, 1, 1, 1);
        rowParam.weight = 1;

        TextView text;
        for (int i = 0; i < (content.length >= date.length ? content.length : date.length); i++) {
            TableRow row = new TableRow(getActivity());

            for (int j= 0; j < 2; j++) {
                text = new TextView(getActivity());
                text.setTextColor(getResources().getColor(R.color.black));
                text.setTextSize(14f);
                if (i % 2 == 1) {
                    text.setBackgroundColor(getResources().getColor(R.color.Tan));
                }
                if (j == 0) {
                    text.setPadding(8, 4, 0, 4);
                    text.setGravity(Gravity.START);
                    text.setTypeface(null, Typeface.BOLD);
                    text.setText(content[i]);
                }
                else {
                    text.setPadding(0, 4, 0, 4);
                    text.setGravity(Gravity.CENTER);
                    text.setText(date[i]);
                }
                row.addView(text, rowParam);
            }
            table.addView(row);
        }

        return v;
    }
}
