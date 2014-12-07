package com.techhab.collegeapp;



import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.zip.Inflater;
public class FallTerm extends Fragment {

    public static final String ARG_OBJECT = "object";
    public static Fragment createNewInstace() {
        return new FallTerm();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fall_term, container, false);
        ListView lvFallTerm = (ListView) v.findViewById(R.id.lvFall_term);
        lvFallTerm.setAdapter(new MenuAdapter(getActivity()));
        return v;
    }


    private class MenuAdapter extends BaseAdapter
    {
        private String[] menuList = {"Labor Day: Monday, September 1", "Orientation for Visiting International Students: September 4-9", "Orientation Program for First Year Students: September 10-14", "Classes Begin: Monday, September 15", "Homecoming: Saturday, October 18", "Mid-Term Break: Friday, October 24", "Family Weekend: October 31 - November 2", "Classes End: Friday, November 21", "Final Exams: November 23-25 (Sun-Tue)"};
        private Context context;
        private MenuAdapter(Context context)
        {
            this.context = context;
        }

        @Override
        public int getCount()
        {
            return menuList.length;
        }

        @Override
        public String getItem(int position)
        {
            return menuList[position];
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.food_menu_item, parent, false);
            TextView tvFallItem = (TextView) rowView.findViewById(R.id.tv_food_item);
            tvFallItem.setText(getItem(position));
            return rowView;
        }
    }
}
