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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.techhab.collegeapp.R;
import com.techhab.collegeapp.application.CollegeApplication;


public class CampusBuildingsFragment extends Fragment {

    public static final String ARG_OBJECT = "object";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

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

//        // use a linear layout manager
//        mLayoutManager = new LinearLayoutManager(this);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//
//        // specify an adapter (see also next example)
//        mAdapter = new MyAdapter(myDataset);
//        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        String[] dataset = new String[2];
        /*for (int i = 0; i < dataset.length; i++) {
            dataset[i] = "item" + i;
        }*/
        dataset[0] = "HICKS CENTER";
        dataset[1] = "HOBEN HALL";
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
        drawable[1] = res.getDrawable(R.drawable.western_michigan_broncos);




        RecyclerAdapter mAdapter = new RecyclerAdapter(dataset, description, drawable, getActivity());
        mRecyclerView.setAdapter(mAdapter);
        super.onViewCreated(view, savedInstanceState);
    }

    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
        private String[] mDataset;
        private Context mContext;
        private String[] mDescription;
        private Drawable[] mDrawable;

        public RecyclerAdapter(String[] dataset, String[] description, Drawable[] drawable, Context context) {
            mDataset = dataset;
            mContext = context;
            mDrawable = drawable;
            mDescription = description;
        }

        // Not use static
        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView mTextView, mDescr;
            public ImageView mBanner;
            public Button button;

            public ViewHolder(View itemView) {
                super(itemView);
                mTextView = (TextView) itemView.findViewById(R.id.card_title);

                mDescr = (TextView) itemView.findViewById(R.id.card_description);
                mDescr.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v) {
                        Toast.makeText(
                                mContext,
                                "Text should expand!" , Toast.LENGTH_LONG).show();
                    }
                });

                mBanner = (ImageView) itemView.findViewById(R.id.card_banner);
                mBanner.setOnTouchListener(new View.OnTouchListener(){
                    public boolean onTouch(View v, MotionEvent event){
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                mBanner.setColorFilter(Color.GRAY, PorterDuff.Mode.DARKEN);
                                break;
                            case MotionEvent.ACTION_UP:
                                mBanner.setColorFilter(null);
                                Toast.makeText(
                                        mContext,
                                        "Text should expand!" , Toast.LENGTH_LONG).show();
                                break;
                            case MotionEvent.ACTION_CANCEL:
                                mBanner.setColorFilter(null);
                                break;
                            default: break;
                        }
                        return true;
                    }
                });
                button = (Button) itemView.findViewById(R.id.card_button);
                button.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                        Intent activityChangeIntent = new Intent(getActivity(), MapsActivity.class);

                        // currentContext.startActivity(activityChangeIntent);

                        getActivity().startActivity(activityChangeIntent);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return mDataset.length;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            String currentPos = mDataset[position];

            switch (currentPos) {
                case "Hicks Center":
                    holder.mTextView.setText(currentPos);
                    holder.mDescr.setText(mDescription[position]);
                    holder.mBanner.setImageDrawable(mDrawable[position]);
                    break;
                default:
                    holder.mTextView.setText(currentPos);
                    holder.mDescr.setText(mDescription[position]);
                    holder.mBanner.setImageDrawable(mDrawable[position]);
            }


        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.recycle_item_with_buttons, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

    }

}
