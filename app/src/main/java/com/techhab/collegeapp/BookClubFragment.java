package com.techhab.collegeapp;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookClubFragment extends Fragment implements View.OnClickListener {
    public static final String ARG_OBJECT = "object";
    // Buttons Cafeteria
    View v;
    FoodStore Richardson;
    TextView tvTimeInfo;
    TextView tvTimeInfo1;
    TextView tvTimeDetailInfo;
    RecyclerView rvMenu;
    private boolean isBreakfastCollapse = true;
    RecyclerView.LayoutManager mLayoutManager;


    private static final int RICHARDSON = 1;

    public static Fragment createNewIntace() {
        RichardsonFragment fragment = new RichardsonFragment();
        Bundle arg = new Bundle();
        fragment.setArguments(arg);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_richardson, parent, false);
        rvMenu = (RecyclerView) v.findViewById(R.id.fragment_richardson_rvMenu);
        tvTimeInfo = (TextView) v.findViewById(R.id.fragment_richardson_tvInfo);
        tvTimeDetailInfo = (TextView) v.findViewById(R.id.status_bar_extended_info);

        tvTimeInfo.setOnClickListener(this);
        //todo: fake data for Richardson
        Richardson = new FoodStore();
        Richardson.setStoreName("Richardson Room");
        Richardson.setOpenHour(4);
        Richardson.setCloseHour(22);
        MenuItem Specials = new MenuItem();
        Specials.setTitle("Specials");
        Specials.setSubTitle("Sandwich");
        List<String> specialSandwiches = new ArrayList<String>();
        specialSandwiches.add("meant ball mea");
        specialSandwiches.add("");
        specialSandwiches.add("Bacon");
        Specials.setMainLines(specialSandwiches);
        List<String> specialSoups = new ArrayList<String>();
        specialSoups.add("Pho soups");
        specialSoups.add("Dumpling");
        Specials.setInternationalCorner(specialSoups);

        MenuItem lunch = new MenuItem();
        lunch.setTitle("Lunch");
        List<String> mainLines1 = new ArrayList<String>();
        mainLines1.add("item1");
        mainLines1.add("item2");
        mainLines1.add("item3");
        mainLines1.add("item4");
        mainLines1.add("item5");
        lunch.setMainLines(mainLines1);
        List<String> internationalCorner1 = new ArrayList<String>();
        internationalCorner1.add("item1");
        internationalCorner1.add("item2");
        lunch.setInternationalCorner(internationalCorner1);

        MenuItem dinner = new MenuItem();
        dinner.setTitle("Dinner");
        List<String> mainLines2 = new ArrayList<String>();
        mainLines2.add("item1");
        mainLines2.add("item2");
        mainLines2.add("item3");
        mainLines2.add("item4");
        mainLines2.add("item5");
        dinner.setMainLines(mainLines2);
        List<String> internationalCorner2 = new ArrayList<String>();
        internationalCorner2.add("item1");
        internationalCorner2.add("item2");
        dinner.setInternationalCorner(internationalCorner2);

        List<MenuItem> menuItems = new ArrayList<MenuItem>();
        menuItems.add(Specials);
        menuItems.add(lunch);
        menuItems.add(dinner);
        Richardson.setMenuItemList(menuItems);
        mLayoutManager = new LinearLayoutManager(getActivity());
        rvMenu.setLayoutManager(mLayoutManager);
        rvMenu.setAdapter(new MenuAdapter(getActivity(), menuItems));
//        if (isOpened(Richardson))
//        {
//            v.findViewById(R.id.fragment_careteria_llHeader).setBackgroundColor(getResources().getColor(R.color.green));
//            ((TextView) v.findViewById(R.id.fragment_cafeteria_tvInfo1)).setText("Open");
//        }
//        else
//        {
//            v.findViewById(R.id.fragment_careteria_llHeader).setBackgroundColor(getResources().getColor(R.color.red));
//            ((ImageView) v.findViewById(R.id.fragment_cafeteria_tvInfo1)).setText("Closed");
//        }
       getRemainTime();
       return v;
   }


    private boolean isOpened(FoodStore foodStore)
    {
        Long currentTime = System.currentTimeMillis();
        long openTime = getTimeOFDay(foodStore.getOpenHour(), foodStore.getOpenMinutes());
        Calendar closeCalendar = Calendar.getInstance();
        closeCalendar.set(Calendar.HOUR_OF_DAY, foodStore.getCloseHour());
        closeCalendar.set(Calendar.MINUTE, foodStore.getCloseMinutes());
        closeCalendar.set(Calendar.SECOND, 0);
        long closedTIme = getTimeOFDay(foodStore.getCloseHour(), foodStore.getCloseMinutes());
        if (currentTime >= openTime && currentTime <= closedTIme)
            return true;
        return false;
    }

    private long getTimeOFDay(int hour, int minute)
    {
        Calendar openCalendar = Calendar.getInstance();
        openCalendar.set(Calendar.HOUR_OF_DAY, hour);
        openCalendar.set(Calendar.MINUTE, minute);
        openCalendar.set(Calendar.SECOND, 0);
        return openCalendar.getTimeInMillis();
    }



    private void getRemainTime()
    {
        long openMillis = getTimeOFDay(Richardson.getOpenHour(), Richardson.getOpenMinutes());
        long closeMillis = getTimeOFDay(Richardson.getCloseHour(), Richardson.getCloseMinutes());
        Time TimeNow = new Time();
        TimeNow.setToNow(); // set the date to Current Time
        TimeNow.normalize(true);
        long nowMillis = TimeNow.toMillis(true);
        long millisset = 0;
        if (nowMillis >= openMillis && nowMillis <= closeMillis)
        {
            millisset = closeMillis - nowMillis;

        }
        else
        {
            millisset = openMillis - nowMillis > 0 ? openMillis - nowMillis : openMillis - nowMillis + 86400*1000;

        }

        new CountDownTimer(millisset, 1000) {
            public void onTick(long millisUntilFinished) {

                int days = (int) ((millisUntilFinished / 1000) / 86400);
                int hours = (int) (((millisUntilFinished / 1000) - (days
                        * 86400)) / 3600);
                int minutes = (int) (((millisUntilFinished / 1000) - ((days
                        * 86400) + (hours * 3600))) / 60);
                int seconds = (int) ((millisUntilFinished / 1000) % 60);


                tvTimeInfo.setText(
                        " "+ "for " + hours + "hours and " + minutes + " minutes"
                );

            }

            public void onFinish() {
                tvTimeInfo.setText("done");
            }
        }.start();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.time_left_text:
                tvTimeDetailInfo.setVisibility(tvTimeDetailInfo.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);



        }
    }



    private void PhoneCall(String phoneNumber) {
        Intent phoneIntent = new Intent(Intent.ACTION_CALL);
        phoneIntent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(phoneIntent);
    }

    private TextView createFoodItemView(String foodName)
    {
        TextView tvFoodItem = new TextView(getActivity());
        tvFoodItem.setText("- " + foodName);
        tvFoodItem.setTextColor(getResources().getColor(R.color.primary_text_default_material_light));

        return tvFoodItem;
    }

    private class FoodStore
    {
        int openHour;
        int openMinutes;
        int closeHour;
        int closeMinutes;
        String storeName;
        List<MenuItem> menuItemList;
        public int getOpenHour() {
            return openHour;
        }

        public void setOpenHour(int openHour) {
            this.openHour = openHour;
        }

        public int getOpenMinutes() {
            return openMinutes;
        }

        public void setOpenMinutes(int openMinutes) {
            this.openMinutes = openMinutes;
        }

        public int getCloseHour() {
            return closeHour;
        }

        public void setCloseHour(int closeHour) {
            this.closeHour = closeHour;
        }

        public int getCloseMinutes() {
            return closeMinutes;
        }

        public void setCloseMinutes(int closeMinutes) {
            this.closeMinutes = closeMinutes;
        }

        public String getStoreName() {
            return storeName;
        }

        public void setStoreName(String storeName) {
            this.storeName = storeName;
        }

        public List<MenuItem> getMenuItemList()
        {
            return menuItemList;
        }

        public void setMenuItemList(List<MenuItem> menuItemList)
        {
            this.menuItemList = menuItemList;
        }
    }

    private class MenuItem
    {
        String title;
        String subTitle;
        private List<String> mainLines;
        private List<String> internationalCorner;

        public String getTitle()
        {
            return title;
        }

        public String getSubTitle()
        {
            return subTitle;
        }
        public void setTitle(String title)
        {
            this.title = title;
        }
        public void setSubTitle(String subTitle)
        {
            this.subTitle = subTitle;
        }

        public List<String> getMainLines()
        {
            return mainLines;
        }

        public void setMainLines(List<String> mainLines)
        {
            this.mainLines = mainLines;
        }

        public List<String> getInternationalCorner()
        {
            return internationalCorner;
        }

        public void setInternationalCorner(List<String> internationalCorner)
        {
            this.internationalCorner = internationalCorner;
        }
    }
    private class MenuAdapter extends RecyclerView.Adapter
    {
        private List<MenuItem> menuList;
        private Context context;
        private MenuAdapter(Context context, List<MenuItem> menuList)
        {
            this.context = context;
            this.menuList = menuList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.caf_meal_card, viewGroup, false);
            return new ViewHolder(rowView);
        }


        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int i)
        {
            final MenuItem menuItem = menuList.get(i);
            final MenuAdapter.ViewHolder menuViewHolder = (ViewHolder) viewHolder;
            menuViewHolder.tvTitle.setText(menuItem.getTitle());
            menuViewHolder.tvTitle1.setText(menuItem.getSubTitle());
            menuViewHolder.tvTitle2.setText(menuItem.getSubTitle());

            collapseMenu(menuViewHolder, menuItem);
            menuViewHolder.tbViewMore.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                    {
                        openMenu(menuViewHolder, menuItem);
                    }
                    else
                    {
                        collapseMenu(menuViewHolder, menuItem);
                    }
                }
            });
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public int getItemCount()
        {
            return menuList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView tvTitle;
            public TextView tvTitle1;
            public TextView tvTitle2;

            public LinearLayout llMainLines;
            public LinearLayout llInternationalCorner;
            public ToggleButton tbViewMore;

            public ViewHolder(View itemView) {
                super(itemView);
                tvTitle = (TextView) itemView.findViewById(R.id.meal_title);
                tvTitle1 = (TextView) itemView.findViewById(R.id.fragment_cafeteria_tvMainLine);
                tvTitle2 = (TextView) itemView.findViewById(R.id.fragment_cafeteria_tvInternationalCorner);
                llMainLines = (LinearLayout) itemView.findViewById(R.id.fragment_cafeteria_llMainLine);
                llInternationalCorner = (LinearLayout) itemView.findViewById(R.id.fragment_cafeteria_llInternationalCorner);
                tbViewMore = (ToggleButton) itemView.findViewById(R.id.fragment_cafeteria_tbViewMore);
            }

        }

        private void collapseMenu(MenuAdapter.ViewHolder viewHolder, MenuItem menuItem) {
            viewHolder.llMainLines.removeAllViews();
            viewHolder.llInternationalCorner.removeAllViews();
            int maxShortLength = menuItem.getMainLines().size() > 3 ? 3 : menuItem.getMainLines().size();
            for (int i = 0; i < maxShortLength; i ++)
            {
                viewHolder.llMainLines.addView(createFoodItemView(menuItem.getMainLines().get(i)));

            }
            maxShortLength = menuItem.getInternationalCorner().size() > 3 ? 3 : menuItem.getInternationalCorner().size();
            for (int i = 0; i < maxShortLength; i++)
            {
                viewHolder.llInternationalCorner.addView(createFoodItemView(menuItem.getInternationalCorner().get(i)));
            }
        }

        private void openMenu(MenuAdapter.ViewHolder viewHolder, MenuItem menuItem) {
            viewHolder.llMainLines.removeAllViews();
            for (String foodItem : menuItem.getMainLines())
            {
                viewHolder.llMainLines.addView(createFoodItemView(foodItem));
            }

            viewHolder.llInternationalCorner.removeAllViews();
            for (String foodItem : menuItem.getInternationalCorner())
            {
                viewHolder.llInternationalCorner.addView(createFoodItemView(foodItem));
            }
        }
    }

}
