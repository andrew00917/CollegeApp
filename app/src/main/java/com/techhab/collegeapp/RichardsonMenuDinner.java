package com.techhab.collegeapp;



import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.zip.Inflater;




public class RichardsonMenuDinner extends Fragment {
    public static final String ARG_OBJECT = "object";
    RecyclerView.LayoutManager mLayoutManager;
    public static Fragment createNewInstace() {
        return new RichardsonMenuDinner();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_richardson_menu_dinner, container, false);
        RecyclerView rvRichardsonMenuDinner = (RecyclerView) v.findViewById(R.id.rvRichardson_menu_dinner);
        mLayoutManager = new LinearLayoutManager(getActivity());
        rvRichardsonMenuDinner.setLayoutManager(mLayoutManager);
        rvRichardsonMenuDinner.setAdapter(new MenuAdapter(getActivity()));
        return v;
    }


    private class MenuAdapter extends RecyclerView.Adapter
    {
        private String[] menuList = {"Spinach & Feta Stuffed Pork Loin", "Home Style Stuffing w/Gravy", "Roasted Redskin Potatoes", "Brown & Thyme Glazed Carrots", "Creamy Risotto Action Station", "Pesto Grilled Chicken Pizza", "Corn on the Cob", "Veggie Medley", "Chicken Noodle Bowl", "Fried Green Tomatoes"};

        private Context context;
        private MenuAdapter(Context context)
        {
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.food_menu_item, viewGroup, false);
            return new ViewHolder(rowView);
        }


        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i)
        {
            String foodItem = menuList[i];
            ((ViewHolder)viewHolder).tvFoodIem.setText(foodItem);

        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public int getItemCount()
        {
            return menuList.length;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView tvFoodIem;

            public ViewHolder(View itemView) {
                super(itemView);
                tvFoodIem = (TextView) itemView.findViewById(R.id.tv_food_item);
            }

        }
    }

}
