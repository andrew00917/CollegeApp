package com.techhab.collegeapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.techhab.collegeapp.application.CollegeApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akhavantafti on 1/25/2015.
 */
public class TakeATourActivity extends ActionBarActivity {

    // Tag used when logging messages
    private static final String TAG = TakeATourActivity.class.getSimpleName();


    // Constructor
    public TakeATourActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_a_tour);
        ViewPager viewPager = (ViewPager) findViewById(R.id.tour_viewpager);
        List<Tour> tourList = new ArrayList<>();
        tourList.add(new Tour(R.drawable.dinner, "This is dinner"));
        tourList.add(new Tour(R.drawable.dinner, "Description here"));
        tourList.add(new Tour(R.drawable.dinner,"Description......"));
        viewPager.setAdapter(new TourAdapter(this, tourList));


    }

    class TourAdapter extends PagerAdapter
    {
        List<Tour> tours;
        Context context;
        public TourAdapter(Context context, List<Tour> tours)
        {
            this.tours = tours;
            this.context = context;

        }

        @Override
        public int getCount() {
            return tours.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.tour_item, null);
            ImageView ivImage = (ImageView) view.findViewById(R.id.tour_ivImage);
            TextView tvDescription = (TextView) view.findViewById(R.id.tour_tvDescription);
            Tour tour = tours.get(position);
            ivImage.setImageResource(tour.imageRes);
            tvDescription.setText(tour.description);
            container.addView(view);
            return view;

        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }
    }

    class Tour
    {
        String description;
        int imageRes;

        Tour(int imageRes, String description) {
            this.description = description;
            this.imageRes = imageRes;
        }
    }


}
