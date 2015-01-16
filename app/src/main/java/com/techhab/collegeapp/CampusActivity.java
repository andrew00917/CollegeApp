package com.techhab.collegeapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


public class CampusActivity extends ActionBarActivity implements NavigationDrawerCallbacks {


    private final Handler handler = new Handler();

    //from Fragment
    public static final String ARG_OBJECT = "object";

    Toolbar toolbar;

    private int currentPosition;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public CampusActivity() {
            super();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus);

        mRecyclerView = (RecyclerView) findViewById(R.id.campus_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.main_menu_01));

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

        description[0] = "This is a description of Hicks Center We get a reference to the ExpandableListView, expandableListView. Then we call MyDataProvider.getDataHashMap() to get the HashMap containing the lists of countries and cities. We assign this to our HashMap, countriesHashMap.\n"
        + "\n";
        description[1] = "This is a description for Hoben Halllllllllllllllll";

        Drawable[] drawable = new Drawable[2];
        Resources res = getResources();
        drawable[0] = res.getDrawable(R.drawable.domo);
        drawable[1] = res.getDrawable(R.drawable.western_michigan_broncos);





        RecyclerAdapter mAdapter = new RecyclerAdapter(dataset, description, drawable, this);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds rssItemList to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_campus, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }


    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
        private String[] mDataset;
        private Context mContext;
        private String[] mDescription;
        private Drawable[] mDrawable;

        public boolean isExpanded = false;

        private void toggleVisibility (TextView text) {
            if (text.getVisibility() == View.GONE)
                text.setVisibility(View.VISIBLE);
            else
                text.setVisibility(View.GONE);
        }

        public RecyclerAdapter(String[] dataset, String[] description, Drawable[] drawable, Context context) {
            mDataset = dataset;
            mContext = context;
            mDrawable = drawable;
            mDescription = description;
        }

        // Not use static
        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView mTextView, mExpandTextView;
            public FrameLayout mBanner;
            public Button button, readButton;

            public ViewHolder(View itemView) {
                super(itemView);

                mTextView = (TextView) itemView.findViewById(R.id.building_title);

                mExpandTextView = (TextView) itemView.findViewById(R.id.building_description);
                readButton = (Button) itemView.findViewById(R.id.read_more_button);


                mExpandTextView = (TextView) itemView.findViewById(R.id.building_description);
                mExpandTextView.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v) {
                        Toast.makeText(
                                mContext,
                                "Text should expand!", Toast.LENGTH_LONG).show();
                    }
                });

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
                button = (Button) itemView.findViewById(R.id.mapit_button);
                button.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                        Intent activityChangeIntent = new Intent(mContext, MapsActivity.class);

                        // currentContext.startActivity(activityChangeIntent);

                        startActivity(activityChangeIntent);
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
            final int heightToExpand;
            final TextView tempExpandTextView = holder.mExpandTextView;
            switch (currentPos) {
                case "Hicks Center":
                    holder.mTextView.setText(currentPos);
                    tempExpandTextView.setText(mDescription[position]);
                    holder.mBanner.setBackground(mDrawable[position]);
                    break;
                default:
                    holder.mTextView.setText(currentPos);
                    tempExpandTextView.setText(mDescription[position]);
                    holder.mBanner.setBackground(mDrawable[position]);
            }

            tempExpandTextView.measure(0, 0);
            heightToExpand = tempExpandTextView.getMeasuredHeight();

            tempExpandTextView.setHeight(0);

            holder.readButton.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    if ( ! isExpanded ) {
                        Log.d("onClick", "heightToExpand: " + heightToExpand );
                        HeightAnimation animation = new HeightAnimation(tempExpandTextView,
                                heightToExpand, true);
                        animation.setDuration(300);
                        v.startAnimation(animation);
                        isExpanded = !isExpanded;
                    } else {
                        Log.d("onClick", "heightToExpand: " + heightToExpand );
                        HeightAnimation animation = new HeightAnimation(tempExpandTextView,
                                heightToExpand, false);
                        animation.setDuration(300);
                        v.startAnimation(animation);
                        isExpanded = !isExpanded;
                    }
                }
            });


        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.buildings_recycle, parent, false);
            ViewHolder holder = new ViewHolder(view);

            return holder;
        }

    }
}
