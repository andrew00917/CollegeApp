package com.techhab.collegeapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by akhavantafti on 1/25/2015.
 */
public class TakeATourFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.take_a_tour, container, false);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.tour_viewpager);
        List<Tour> tourList = new ArrayList<>();
        tourList.add(new Tour(R.drawable.dinner, "This is dinner"));
        tourList.add(new Tour(R.drawable.dinner, "Description here"));
        tourList.add(new Tour(R.drawable.dinner,"Description......"));
        viewPager.setAdapter(new TourAdapter(getActivity(), tourList));
        return view;
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
            View view = inflater.inflate(R.layout.tour_item, container, false);
            ImageView ivImage = (ImageView) view.findViewById(R.id.tour_ivImage);
            TextView tvDescription = (TextView) view.findViewById(R.id.tour_tvDescription);
            Tour tour = tours.get(position);
            ivImage.setImageResource(tour.imageRes);
            tvDescription.setText(tour.description);
            return view;

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
