package com.techhab.collegeapp;


import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.*;


public class CafeteriaFragment extends Fragment {
    public static final String ARG_OBJECT = "object";
    // Buttons Cafeteria
    private View v;
    private FoodStore cafeteriaStore;
    private TextView tvTimeInfo;
    private ImageView image;
    private TextView tvTimeDetailInfo;
    private RecyclerView rvMenu;
    private ImageButton ibExpandble;
    private LinearLayout statusBar;
    private boolean isBreakfastCollapse = true;
    private RecyclerView.LayoutManager mLayoutManager;
    private Spinner cafeteriaSpinner;
    private int currentBarColor;
    private boolean isExpanded = false;

    private static final int RICHARDSON = 1;

    public static Fragment createNewInstance() {
        CafeteriaFragment fragment = new CafeteriaFragment();
        Bundle arg = new Bundle();
        fragment.setArguments(arg);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_cafeteria, parent, false);
        rvMenu = (RecyclerView) v.findViewById(R.id.fragment_cafeteria_rvMenu);
        tvTimeInfo = (TextView) v.findViewById(R.id.fragment_cafeteria_tvInfo);
        tvTimeDetailInfo = (TextView) v.findViewById(R.id.fragment_cafeteria_tvTimeDetail);
        ibExpandble = (ImageButton) v.findViewById(R.id.ibExpandable);
        statusBar = (LinearLayout) v.findViewById(R.id.cafeteria_status_bar);



        /*ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.cafeteria_value));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);*/

        tvTimeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isExpanded) { // Expand
                    HeightAnimation animation = new HeightAnimation(tvTimeDetailInfo, 300, true);
                    animation.setDuration(300);
                    statusBar.startAnimation(animation);
                    ibExpandble.setImageResource(R.drawable.ic_action_expand);
                    isExpanded = !isExpanded;
                } else { // Collapse
                    HeightAnimation animation = new HeightAnimation(tvTimeDetailInfo, 300, false);
                    animation.setDuration(300);
                    statusBar.startAnimation(animation);
                    ibExpandble.setImageResource(R.drawable.ic_action_collapse);
                    isExpanded = !isExpanded;
                }
            }
        });

        cafeteriaSpinner = (Spinner) v.findViewById(R.id.fragment_cafeteria_spinner);
        ArrayList<String> days = new ArrayList<>();
        // TODO populate the days arraylist with 4 entries. The first two will always be
        // "Today" and "Tomorrow", but the last 2 entries must be programmatically determined
        // using the current date.
        days.add("Today");
        days.add("Tomorrow");
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.add(Calendar.DATE, 2);
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
        String nextDay = dayFormat.format(gregorianCalendar.getTime());
        days.add(nextDay);
        CustomAdapter spinnerAdapter = new CustomAdapter(getActivity(), android.R.layout.simple_spinner_item, days);
//        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cafeteriaSpinner.setAdapter(spinnerAdapter);

        //todo: fake data for cafe
        cafeteriaStore = new FoodStore();
        cafeteriaStore.setStoreName("Cafeteria");
        cafeteriaStore.setOpenHour(4);
        cafeteriaStore.setCloseHour(22);
        Meal breakfast = new Meal();
        breakfast.setMealTitle("Breakfast");
        List<String> breakfastMainLines = new ArrayList<String>();
        breakfastMainLines.add("item 1");
        breakfast.setMainLineItems(breakfastMainLines);
        List<String> breakfastInternationalCorner = new ArrayList<String>();
        breakfastInternationalCorner.add("1");
        breakfastInternationalCorner.add("2");
        breakfastInternationalCorner.add("3");
        breakfastInternationalCorner.add("4");
        breakfast.setInternationalCornerItems(breakfastInternationalCorner);

        Meal lunch = new Meal();
        lunch.setMealTitle("Lunch");
        List<String> mainLines1 = new ArrayList<String>();
        mainLines1.add("item1");
        mainLines1.add("item2");
        mainLines1.add("item3");
        mainLines1.add("item4");
        mainLines1.add("item5");
        mainLines1.add("item6");
        mainLines1.add("item7");

        lunch.setMainLineItems(mainLines1);
        List<String> internationalCorner1 = new ArrayList<String>();
        internationalCorner1.add("item1");
        internationalCorner1.add("item2");
        internationalCorner1.add("item3");
        internationalCorner1.add("item4");
        lunch.setInternationalCornerItems(internationalCorner1);
        Meal dinner = new Meal();
        dinner.setMealTitle("Dinner");
        List<String> mainLines2 = new ArrayList<String>();
        mainLines2.add("item1");
        mainLines2.add("item2");
        mainLines2.add("item3");
        mainLines2.add("item4");
        mainLines2.add("item5");
        dinner.setMainLineItems(mainLines2);
        List<String> internationalCorner2 = new ArrayList<String>();
        internationalCorner2.add("item1");
        internationalCorner2.add("item2");
        internationalCorner2.add("item3");
        internationalCorner2.add("item4");
        internationalCorner2.add("item5");
        internationalCorner2.add("item6");
        dinner.setInternationalCornerItems(internationalCorner2);

        List<Meal> meals = new ArrayList<Meal>();
        meals.add(breakfast);
        meals.add(lunch);
        meals.add(dinner);
        cafeteriaStore.setMealList(meals);
        mLayoutManager = new LinearLayoutManager(getActivity());
        rvMenu.setLayoutManager(mLayoutManager);
        rvMenu.setAdapter(new MenuAdapter(getActivity(), meals));
        if (isOpened(cafeteriaStore))
        {
            currentBarColor = R.color.green;
            statusBar.setBackgroundColor(getResources().getColor(R.color.green));
            ((ImageView) v.findViewById(R.id.fragment_cafeteria_image)).setImageResource(R.drawable.open_sign);
        }
        else
        {
            currentBarColor = R.color.red;
            statusBar.setBackgroundColor(getResources().getColor(R.color.red));
            ((ImageView) v.findViewById(R.id.fragment_cafeteria_image)).setImageResource(R.drawable.open_sign);
        }
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
        long openMillis = getTimeOFDay(cafeteriaStore.getOpenHour(), cafeteriaStore.getOpenMinutes());
        long closeMillis = getTimeOFDay(cafeteriaStore.getCloseHour(), cafeteriaStore.getCloseMinutes());
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


                if (isOpened(cafeteriaStore))
                {
                    if (hours == 0 && minutes <= 30 && currentBarColor != R.color.Yellow)
                    {
                        currentBarColor = R.color.Yellow;
                        statusBar.setBackgroundColor(getResources().getColor(R.color.Yellow));
                    }
                    else if ((hours > 0 || minutes > 30) && currentBarColor != R.color.green)
                    {
                        currentBarColor = R.color.green;
                        statusBar.setBackgroundColor(getResources().getColor(R.color.green));
                    }
                    tvTimeInfo.setText(
                            "Closing in " + hours + " hours and " + minutes + " minutes"
                    );}
                else
                {
                    if (currentBarColor != R.color.red)
                    {
                        currentBarColor = R.color.red;
                        statusBar.setBackgroundColor(getResources().getColor(R.color.red));
                    }
                    tvTimeInfo.setText(
                            "Opening in"+ " " + hours + "hours and " + minutes + " minutes");
                }

            }

            public void onFinish() {
                getRemainTime();

            }
        }.start();
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
        List<Meal> mealList;
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

        public List<Meal> getMealList()
        {
            return mealList;
        }

        public void setMealList(List<Meal> mealList)
        {
            this.mealList = mealList;
        }
    }

    private class Meal
    {
        String mealTitle;
        private List<String> mainLineItems;
        private List<String> internationalCornerItems;

        public String getMealTitle()
        {
            return mealTitle;
        }

        public void setMealTitle(String mealTitle)
        {
            this.mealTitle = mealTitle;
        }

        public List<String> getMainLineItems()
        {
            return mainLineItems;
        }

        public void setMainLineItems(List<String> mainLineItems)
        {
            this.mainLineItems = mainLineItems;
        }

        public List<String> getInternationalCornerItems()
        {
            return internationalCornerItems;
        }

        public void setInternationalCornerItems(List<String> internationalCornerItems)
        {
            this.internationalCornerItems = internationalCornerItems;
        }
    }
    private class MenuAdapter extends RecyclerView.Adapter
    {
        private List<Meal> mealList;
        private Context context;
        private int heightToAdd;
        private MenuAdapter(Context context, List<Meal> mealList)
        {
            this.context = context;
            this.mealList = mealList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.food_wells_menu_item, viewGroup, false);
            return new ViewHolder(rowView);
        }


        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int i)
        {
            final Meal meal = mealList.get(i);
            final MenuAdapter.ViewHolder menuViewHolder = (ViewHolder) viewHolder;
            menuViewHolder.tvTitle.setText(meal.getMealTitle());
            menuViewHolder.tvTitle.setText(meal.getMealTitle());

            loadMenu(menuViewHolder, meal);
//            collapseMenu(menuViewHolder, meal);
            menuViewHolder.tbViewMore.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                    {
                        HeightAnimation animation = new HeightAnimation(((ViewHolder) viewHolder).llMainLines,
                                getHeightToAdd(((ViewHolder) viewHolder).llMainLines, true), true);
                        animation.setDuration(300);
                        ((ViewHolder) viewHolder).llMainLines.startAnimation(animation);
                        HeightAnimation animation1 = new HeightAnimation(((ViewHolder) viewHolder).llInternationalCorner,
                                getHeightToAdd(((ViewHolder) viewHolder).llInternationalCorner, true), true);
                        animation1.setDuration(300);
                        ((ViewHolder) viewHolder).llInternationalCorner.startAnimation(animation1);

                    }
                    else
                    {
                        HeightAnimation animation = new HeightAnimation(((ViewHolder) viewHolder).llMainLines,
                                getHeightToAdd(((ViewHolder) viewHolder).llMainLines, false), false);
                        animation.setDuration(300);
                        ((ViewHolder) viewHolder).llMainLines.startAnimation(animation);
                        HeightAnimation animation1 = new HeightAnimation(((ViewHolder) viewHolder).llInternationalCorner,
                                getHeightToAdd(((ViewHolder) viewHolder).llInternationalCorner, false), false);
                        animation1.setDuration(300);
                        ((ViewHolder) viewHolder).llInternationalCorner.startAnimation(animation1);
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
            return mealList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView tvTitle;
            public LinearLayout llMainLines;
            public LinearLayout llInternationalCorner;
            public ToggleButton tbViewMore;

            public ViewHolder(View itemView) {
                super(itemView);
                tvTitle = (TextView) itemView.findViewById(R.id.menu_item_tvTitle);
                llMainLines = (LinearLayout) itemView.findViewById(R.id.fragment_cafeteria_llMainLine);
                llInternationalCorner = (LinearLayout) itemView.findViewById(R.id.fragment_cafeteria_llInternationalCorner);
                tbViewMore = (ToggleButton) itemView.findViewById(R.id.fragment_cafeteria_tbViewMore);
            }

        }

        private void loadMenu(MenuAdapter.ViewHolder viewHolder, Meal meal) {

            for (int i = 0; i < meal.getMainLineItems().size(); i++) {
                viewHolder.llMainLines.addView(createFoodItemView(meal.getMainLineItems().get(i)));
            }

            setListViewHeightBasedOnChildren(viewHolder.llMainLines);

            for (int i = 0; i < meal.getInternationalCornerItems().size(); i++) {
                viewHolder.llInternationalCorner.addView(createFoodItemView(meal.getInternationalCornerItems().get(i)));
            }

            setListViewHeightBasedOnChildren(viewHolder.llInternationalCorner);
        }

        /**
         *
         */
        public void setListViewHeightBasedOnChildren(LinearLayout linearLayout) {
            if (linearLayout == null) {
                // pre-condition
                return;
            }
            int starterHeight;
            View listItem = linearLayout.getChildAt(0);
            listItem.measure(0, 0);
            int heightOfChild = listItem.getMeasuredHeight();
            starterHeight = (linearLayout.getChildCount() < 3 ? linearLayout.getChildCount() : 3) * heightOfChild;

            ViewGroup.LayoutParams params = linearLayout.getLayoutParams();
            params.height = starterHeight;
            linearLayout.setLayoutParams(params);
            linearLayout.requestLayout();

        }
    }


    private int getHeightToAdd(LinearLayout parent, boolean isExpand)
    {
        View listItem = parent.getChildAt(0);
        listItem.measure(0, 0);
        int heightOfChild = listItem.getMeasuredHeight();
        if (parent.getChildCount() <= 3)
            return 0;
        else
            return heightOfChild * (parent.getChildCount() - 3);


    }

    private class CustomAdapter extends ArrayAdapter<String> {

        private ArrayList<String> days;
        private LayoutInflater inflater;
        private Context context;

        /*************  CustomAdapter Constructor *****************/
        public CustomAdapter(Context context, int rowResourceLayout, ArrayList<String> days ) {
            super(context, rowResourceLayout, days);
            this.context = context;
            this.days = days;

            Log.d("CustomAdapter", "days = " + this.days);

            /***********  Layout inflator to call external xml layout () **********************/
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        // This funtion called for each row ( Called data.size() times )
        public View getCustomView(int position, View convertView, ViewGroup parent) {

            Log.d("CustomAdapter", "getCustomView is called!");
            /********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
            View row = inflater.inflate(R.layout.cafeteria_spinner_rows, parent, false);


            TextView label = (TextView)row.findViewById(R.id.day);

            label.setText(days.get(position));

            Log.d("CustomAdapter", "days.get(position)" + days.get(position));

            return row;
        }

    }

}
