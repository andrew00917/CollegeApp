package com.techhab.collegeapp;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.balysv.materialripple.MaterialRippleLayout;
import com.techhab.rss.Calendar;

import java.util.ArrayList;

public class CalendarFragment extends Fragment {
    public static final String ARG_OBJECT = "object";

    // @formatter:off
    private static final int[] CONTENT = new int[]{
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

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(), view, "CalendarDetailActivity");
                Intent i = new Intent(getActivity(), CalendarDetailActivity.class);
                i.putExtra("position", position);
                ActivityCompat.startActivity(getActivity(), i, options.toBundle());
            }
        }));

        String[] dataset = new String[]{"Fall", "Winter", "Spring"};

        RecyclerAdapter mAdapter = new RecyclerAdapter(dataset, getActivity());
        mRecyclerView.setAdapter(mAdapter);

        //TODO: Add to toolbar here
        ArrayList<String> years = new ArrayList<>();
        years.add("2014-15");
        years.add("2015-16");
        years.add("2016-17");
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


            public ViewHolder(View itemView) {
                super(itemView);
            }
        }

        @Override
        public int getItemCount() {
            return mDataset.length;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            View view = holder.itemView;

            ImageView imgViewStatusTop = (ImageView) view.findViewById(R.id.card_image_view_top);
            TextView tvSeasionName = (TextView) view.findViewById(R.id.tv_calendar_session);
            LinearLayout layoutContainer = (LinearLayout) view.findViewById(R.id.layout_container);

            //set up image view status avatar
            switch (position) {
                case 0:
                    imgViewStatusTop.setImageResource(R.drawable.ic_fall_season);
                    break;
                case 1:
                    imgViewStatusTop.setImageResource(R.drawable.ic_winter_season);
                    break;
                case 2:
                    imgViewStatusTop.setImageResource(R.drawable.ic_spring_season);
                    break;
            }
            //set session title
            tvSeasionName.setText(mDataset[position]);

            String[] content = getActivity().getResources().getStringArray(CONTENT[position]);

            String[] date = getActivity().getResources().getStringArray(DATE_2014[position]);

            //set up view for content
            for (int i = 0; i < 3; i++) {
                LinearLayout layoutContent = new LinearLayout(getActivity());
                layoutContent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                layoutContent.setOrientation(LinearLayout.HORIZONTAL);

                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2f);

                TextView tvContent = new TextView(getActivity());
                tvContent.setText(content[i]);
                tvContent.setTextAppearance(getActivity(), R.style.SingleLineListTextOnly_SubContent);
                tvContent.setLayoutParams(layoutParams);
                tvContent.setPadding(0, 5, 0, 5);

                layoutParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
                TextView tvDate = new TextView(getActivity());
                tvDate.setText(date[i]);
                tvDate.setTextAppearance(getActivity(), R.style.SingleLineListTextOnly_SubContent);
                tvDate.setLayoutParams(layoutParams);
                tvDate.setPadding(0, 5, 0, 5);

                layoutContent.addView(tvContent);
                layoutContent.addView(tvDate);

                layoutContainer.addView(layoutContent);
            }

            //set up hint text view
           /* TextView tvHint = new TextView(getActivity());
            tvHint.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tvHint.setText("Touch to view details");
            tvHint.setTextColor(Color.BLUE);
            tvHint.setGravity(Gravity.END);
            tvHint.setTextAppearance(getActivity(), R.style.SingleLineListTextOnly_SubContent);*/

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, final int position) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.academic_calendar_recycle, parent, false);

            return new ViewHolder(view);
        }
    }

    static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        private OnItemClickListener mListener;

         interface OnItemClickListener {
            public void onItemClick(View view, int position);
        }

        GestureDetector mGestureDetector;

        public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
            mListener = listener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
        }

        @Override public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, view.getChildPosition(childView));
                return true;
            }
            return false;
        }

        @Override public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) { }
    }
}