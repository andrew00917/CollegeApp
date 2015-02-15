package com.techhab.collegeapp;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.techhab.rss.Calendar;

import java.util.ArrayList;

public class CalendarFragment extends Fragment {
    public static final String ARG_OBJECT = "object";

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

    View v;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Spinner spinner;
    private int selectedPosition = 0;

    public CalendarFragment() {
        // Required Empty Constructor
    }

    public static Fragment createNewInstance() {
        CalendarFragment fragment = new CalendarFragment();
        Bundle arg = new Bundle();
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_calender, parent, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
//        mRecyclerView.setHasFixedSize(true);

        spinner = (Spinner) v.findViewById(R.id.year_spinner);

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(layoutManager);

        String[] dataset = new String[3];
        dataset[0] = "Fall";
        dataset[1] = "Winter";
        dataset[2] = "Spring";

        RecyclerAdapter mAdapter = new RecyclerAdapter(dataset, getActivity());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                @Override public void onItemClick(View view, int position) {
                    //todo: implement more here
                    Intent intent = new Intent(getActivity(), CalendarDetailActivity.class);
                    startActivity(intent);
                }
        }));

        ArrayList<String> years = new ArrayList<>();
        years.add("2014-15");
        years.add("2015-16");
        years.add("2016-17");

        CustomAdapter spinnerAdapter = new CustomAdapter(getActivity(), android.R.layout.simple_spinner_item, years);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setSelection(0, false);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            // after the item is selected in the spinner it should refresh the fragment
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (selectedPosition  != position)
                {
                    selectedPosition = position;
                    reloadFragment();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner.setAdapter(spinnerAdapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void reloadFragment() {
        getFragmentManager().beginTransaction().detach(this)
                .attach(this)
                .commit();
    }

    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
        private String[] mDataset;
        private Context mContext;

        public RecyclerAdapter(String[] dataset, Context context) {
            mDataset = dataset;
            mContext = context;
        }

        // Not use static
        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView mTextView;
            public TableLayout tableView;

            public ViewHolder(View itemView) {
                super(itemView);
                mTextView = (TextView) itemView.findViewById(R.id.term);
                tableView = (TableLayout) itemView.findViewById(R.id.table_layout);
            }
        }

        @Override
        public int getItemCount() {
            return mDataset.length;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mTextView.setText(mDataset[position]);
            final TableLayout table = holder.tableView;

            String[] content = getActivity().getResources().getStringArray(CONTENT[position]);

            String[] date;
            switch (selectedPosition) {
                case 0:
                    date  = getActivity().getResources().getStringArray(DATE_2014[position]);
                    break;
                case 1:
                    date  = getActivity().getResources().getStringArray(DATE_2014[position]);
                    break;
                case 2:
                    date  = getActivity().getResources().getStringArray(DATE_2014[position]);
                    break;
                default:
                    date  = getActivity().getResources().getStringArray(DATE_2014[position]);
                    break;
            }

            TableRow.LayoutParams rowParam = new TableRow.LayoutParams();
            rowParam.setMargins(1, 1, 1, 1);
//            rowParam.weight = 1;

            TextView text;
            for (int i = 0; i < (content.length >= date.length ? content.length : date.length); i++) {
                TableRow row = new TableRow(getActivity());

                for (int j= 0; j < 2; j++) {
                    text = new TextView(getActivity());
                    text.setTextColor(getResources().getColor(R.color.abc_primary_text_material_light));
                    text.setTextSize(14f);
                    text.setSingleLine();
//                    text.setGravity(Gravity.START);
//                    if (i % 2 == 1) {
//                        text.setBackgroundColor(getResources().getColor(R.color.Tan));
//                    }
                    if (j == 0) {
                        text.setPadding(16, 8, 8, 8);
                        text.setGravity(Gravity.START);
                        text.setTypeface(null, Typeface.BOLD);
                        text.setText(content[i]);
                    }
                    else {
                        text.setPadding(8, 8, 0, 8);
                        text.setGravity(Gravity.START);
                        text.setText(date[i]);
                    }
                    row.addView(text, rowParam);
                }
                table.addView(row);

                // view shorter list
                int starterHeight;
                View listItem = table.getChildAt(0);
                listItem.measure(0, 0);
                int heightOfChild = listItem.getMeasuredHeight();
                starterHeight = (table.getChildCount() < 3 ? table.getChildCount() : 3) * heightOfChild;

                ViewGroup.LayoutParams params = table.getLayoutParams();
                params.height = starterHeight;
                table.setLayoutParams(params);
                table.requestLayout();
            }
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.academic_calendar_recycle, parent, false);
            return new ViewHolder(view);
        }
    }

    /**
     *  CustomSpinnerAdapter for spinner
     */
    private class CustomAdapter extends ArrayAdapter<String> {

        private ArrayList<String> years;
        private LayoutInflater inflater;
        private Context context;

        public CustomAdapter(Context context, int rowResourceLayout, ArrayList<String> years) {
            super(context, rowResourceLayout, years);
            this.context = context;
            this.years = years;

            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        // This funtion called for each row ( Called data.size() times )
        public View getCustomView(int position, View convertView, ViewGroup parent) {

            View row = inflater.inflate(R.layout.cafeteria_spinner_rows, parent, false);

            TextView label = (TextView)row.findViewById(R.id.day);

            label.setText(years.get(position));

            return row;
        }
    }
}