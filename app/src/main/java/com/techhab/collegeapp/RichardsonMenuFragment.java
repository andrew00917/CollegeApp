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


public class RichardsonMenuFragment extends Fragment {
    public static final String ARG_OBJECT = "object";
    public static Fragment createNewInstace() {
        return new RichardsonMenuFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_richardson_menu_breakfast, container, false);
        ListView lvRichardsonMenuBreakFast = (ListView) v.findViewById(R.id.lvRichardson_menu_breakfast);
        lvRichardsonMenuBreakFast.setAdapter(new MenuAdapter(getActivity()));
        return v;
    }


    private class MenuAdapter extends BaseAdapter
    {
        private String[] menuList = {"Bacon", "Soup", "Cake", "Pizza", "Bread", "Omelette"};
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
            TextView tvFoodItem = (TextView) rowView.findViewById(R.id.tv_food_item);
            tvFoodItem.setText(getItem(position));
            return rowView;
        }
    }
}
