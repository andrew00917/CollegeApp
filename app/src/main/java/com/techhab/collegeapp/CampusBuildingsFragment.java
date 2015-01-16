package com.techhab.collegeapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.techhab.collegeapp.R;
import com.techhab.collegeapp.application.CollegeApplication;


public class CampusBuildingsFragment extends Fragment {

    public static final String ARG_OBJECT = "object";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    public CampusBuildingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_games, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(layoutManager);

        String[] building = new String[2];
        building[0] = "HICKS CENTER";
        building[1] = "HOBEN HALL";
//        dataset[2] = "Arcus Center";
//        dataset[3] = "Hoben Hall";
//        dataset[4] = "Here is a description of Hoben Hall";
//        dataset[5] = "western_michigan_broncos";

        String [] description = new String[2];

        description[0] = "This is a description of Hicks Center We get a reference to the ExpandableListView, expandableListView.\n" +
                "\n" +
                "Then we call MyDataProvider.getDataHashMap() to get the HashMap containing the lists of countries and cities. We assign this to our HashMap, countriesHashMap.";
        description[1] = "This is a description for Hoben Halllllllllllllllll";

        Drawable[] drawable = new Drawable[2];
        Resources res = getResources();
        drawable[0] = res.getDrawable(R.drawable.domo);
        drawable[1] = res.getDrawable(R.drawable.k_banner_night);

        mAdapter = new RecyclerAdapter(building, description, drawable, getActivity());
        mRecyclerView.setAdapter(mAdapter);

        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * Recycler Adapter
     */
    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
        private String[] mBuilding;
        private String[] mDescription;
        private Drawable[] mDrawable;
        private Context mContext;

        public boolean[] isExpanded;
        public int[] dimen;

        private void toggleVisibility (TextView text) {
            if (text.getVisibility() == View.GONE) {
                text.setVisibility(View.VISIBLE);
            } else {
                text.setVisibility(View.GONE);
            }
        }

        public RecyclerAdapter(String[] building, String[] description, Drawable[] drawable, Context context) {
            mBuilding = building;
            mDrawable = drawable;
            mDescription = description;
            mContext = context;

            isExpanded = new boolean[mBuilding.length];
            dimen = new int[mBuilding.length];

            for(int i = 0; i < isExpanded.length; i++) {
                isExpanded[i] = false;
                dimen[i] = -1;
            }
        }

        // Do not use static
        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView mTextView, mExpandTextView;
            public FrameLayout mBanner;
            public Button button, readButton;

            public ViewHolder(View itemView) {
                super(itemView);

                mTextView = (TextView) itemView.findViewById(R.id.building_title);
                mExpandTextView = (TextView) itemView.findViewById(R.id.building_description);

                mBanner = (FrameLayout) itemView.findViewById(R.id.building_banner);
                mBanner.setOnTouchListener(new View.OnTouchListener(){
                    public boolean onTouch(View v, MotionEvent event){
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                mBanner.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.DARKEN);
                                break;
                            case MotionEvent.ACTION_UP:
                                mBanner.getBackground().setColorFilter(null);
                                toggleVisibility(mExpandTextView);
                                break;
                            case MotionEvent.ACTION_CANCEL:
                                mBanner.getBackground().setColorFilter(null);
                                break;
                            default: break;
                        }
                        return true;
                    }
                });

                readButton = (Button) itemView.findViewById(R.id.read_more_button);
                readButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toggleVisibility(mExpandTextView);
                    }
                });

                button = (Button) itemView.findViewById(R.id.mapit_button);
                button.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                        Intent activityChangeIntent = new Intent(mContext, MapsActivity.class);
                        startActivity(activityChangeIntent);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return mBuilding.length;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int p) {
            holder.mTextView.setText(mBuilding[p]);
            holder.mExpandTextView.setText(mDescription[p]);
            holder.mBanner.setBackground(mDrawable[p]);

            toggleVisibility(holder.mExpandTextView);

            // Expanding animation goes here
//            if ( dimen[p] == -1) {
//                dimen[p] = holder.mExpandTextView.getHeight();
//            }
//
//            final int position = p;
//            final TextView tempExpandTextView = holder.mExpandTextView;
//
//            holder.readButton.setOnClickListener(new Button.OnClickListener(){
//                public void onClick(View view) {
//                    if ( ! isExpanded[position]) {
//                        HeightAnimation animation = new HeightAnimation(tempExpandTextView, dimen[position], true);
//                        animation.setDuration(300);
//                        tempExpandTextView.startAnimation(animation);
//                        isExpanded[position] = true;
//                    } else {
//                        HeightAnimation animation = new HeightAnimation(tempExpandTextView, dimen[position], false);
//                        animation.setDuration(300);
//                        tempExpandTextView.startAnimation(animation);
//                        isExpanded[position] = false;
//                    }
//                }
//            });
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.buildings_recycle, parent, false);
            return new ViewHolder(view);
        }
    }
}
