package com.techhab.collegeapp;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.techhab.rss.GenericSportsRssItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic sports fragment that holds functions used by most or all sports fragments.
 *
 * Created by Griffin on 12/29/2014.
 */
public abstract class GenericSportsFragment extends Fragment {

    private int upcomingEventsHeightToAdd;
    private int upcomingEventsHeightAdded;
    private int upcomingEventsItemHeight;
    private int upcomingEventsCount;

    private int pastEventsItemHeight;
    private int pastEventsHeightAdded;
    private int pastEventsRemainingToShow;

    private View rootView;

    private Button upcomingEventsViewMoreButton, pastEventsViewMoreButton;
    private ListView upcomingEventsListView;
    private TableLayout pastEventsTableLayout;
    private ProgressBar upcomingEventsProgressBar;

    public List<GenericSportsRssItem> upcomingEventsList = new ArrayList<>();
    public List<GenericSportsRssItem> pastEventsList = new ArrayList<>();

    public GenericSportsFragment() {

    }

    public void initializeGenericOnCreateView(View rootView) {
        this.rootView = rootView;
        pastEventsTableLayout = (TableLayout) rootView.findViewById(R.id.past_events_table_layout);
        upcomingEventsViewMoreButton = (Button) rootView.findViewById(R.id.upcoming_events_view_more_button);
        upcomingEventsListView = (ListView) rootView.findViewById(R.id.upcoming_events_listview);
        pastEventsViewMoreButton = (Button) rootView.findViewById(R.id.past_events_view_more_button);
        upcomingEventsProgressBar = (ProgressBar) rootView.findViewById(
                R.id.upcoming_games_progress_bar);
        upcomingEventsListView.setEmptyView(upcomingEventsProgressBar);

        upcomingEventsViewMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (upcomingEventsViewMoreButton.getText().equals("VIEW MORE")) {
                    // Sometimes the height to add variable doesn't get set, so check to make
                    // sure it has a value.
                    if (upcomingEventsHeightToAdd == 0) {
                        initializeUpcomingGamesCount();
                    }
                    HeightAnimation animation = new HeightAnimation(upcomingEventsListView,
                            upcomingEventsHeightToAdd, true);
                    animation.setDuration(300);
                    upcomingEventsListView.startAnimation(animation);
                    if (upcomingEventsCount == upcomingEventsList.size()) {
                        upcomingEventsViewMoreButton.setText(R.string.collapse_list);
                    }
                    upcomingEventsHeightAdded += upcomingEventsHeightToAdd;
                    updateUpcomingGamesCount();
                } else {
                    HeightAnimation animation = new HeightAnimation(upcomingEventsListView,
                            upcomingEventsHeightAdded, false);
                    animation.setDuration(300);
                    upcomingEventsListView.startAnimation(animation);
                    upcomingEventsHeightAdded = 0;
                    initializeUpcomingGamesCount();
                    upcomingEventsViewMoreButton.setText(R.string.view_more);
                }
                // TODO: add auto-scrolling
            }
        });

        pastEventsViewMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pastEventsRemainingToShow > 0) {
                    int heightToIncrease = getPastEventsTableHeightToIncrease();
                    HeightAnimation animation = new HeightAnimation(pastEventsTableLayout,
                            heightToIncrease, true);
                    animation.setDuration(300);
                    pastEventsTableLayout.startAnimation(animation);
                    pastEventsHeightAdded += heightToIncrease;
                    if ( pastEventsRemainingToShow == 0 ) {
                        pastEventsViewMoreButton.setText(R.string.collapse_list);
                    }
                } else {
                    HeightAnimation animation = new HeightAnimation(pastEventsTableLayout,
                            pastEventsHeightAdded, false);
                    animation.setDuration(300);
                    pastEventsTableLayout.startAnimation(animation);
                    pastEventsViewMoreButton.setText(R.string.view_more);
                    pastEventsHeightAdded = 0;
                    pastEventsRemainingToShow = (pastEventsList.size() - 3);
                }
                // TODO: fix auto-scrolling
//                scrollView.scrollTo(0, pastEventsTableLayout.getBottom());
            }
        });
    }

    public void addViewToRow(String text, TableRow tableRow, float rowWeight) {
        TextView newTextView = new TextView(getActivity());
        newTextView.setText(text);
        newTextView.setTextColor(getResources().getColor(
                R.color.primary_text_default_material_light));
        newTextView.setTextSize(16);
        newTextView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT, rowWeight));
        tableRow.addView(newTextView);
    }

    /**
     * Updates the upcomingEventsCount for the mUpcomingGamesAdapter. This way, the
     * adapter knows how many more ListView items to load before the user hits "View More".
     */
    public void updateUpcomingGamesCount() {
        // If upcoming games list does not have 3 more items to add, just add however many
        // are remaining.
        int upcomingGamesRemaining = upcomingEventsList.size() - upcomingEventsCount;
        if ( upcomingGamesRemaining < 3 ) {
            upcomingEventsCount += upcomingGamesRemaining;
            upcomingEventsHeightToAdd = upcomingGamesRemaining * upcomingEventsItemHeight;
        } else {
            upcomingEventsCount += 3;
            upcomingEventsHeightToAdd = 3 * upcomingEventsItemHeight;
        }
    }

    public void initializeUpcomingGamesCount() {
        if ( upcomingEventsList.size() > 6 ) {
            upcomingEventsCount = 6;
            upcomingEventsHeightToAdd = 3 * upcomingEventsItemHeight;
        } else if ( upcomingEventsList.size() > 3 ){
            upcomingEventsCount = upcomingEventsList.size();
            upcomingEventsHeightToAdd = (upcomingEventsCount - 3) * upcomingEventsItemHeight;
        } else {
            upcomingEventsCount = upcomingEventsList.size();
            upcomingEventsHeightToAdd = 0;
        }
    }

    /**
     * Sets the ListView height based on the height of it's currently showing children. Right
     * now, the height is calculated using 3 children, due to the expanding nature of the
     * CardView that the ListView is in.
     *
     * @param listView - the ListView to have its height set
     */
    public void setListViewHeightBasedOnChildren(ListView listView) {
        UpcomingGamesAdapter listAdapter = (UpcomingGamesAdapter) listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        try {
            for (int i = 0; i < 3; i++) {
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(0, 0);
                upcomingEventsItemHeight = listItem.getMeasuredHeight() + listView.getDividerHeight();
                totalHeight += listItem.getMeasuredHeight();
            }
        } catch (Exception e) {
            Log.d("setListViewHeight",
                    "Failed (probably due to the activity not being fully constructed");
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (3));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public int getUpcomingEventsCount() {
        return upcomingEventsCount;
    }

    public void sortGames(List<? extends GenericSportsRssItem> list) {
        GenericSportsRssItem item;
        try {
            for (int i = 0; i < list.size(); i++) {
                item = list.get(i);
                if ( item.isUpcoming() ) {
                    upcomingEventsList.add(item);
                } else {
                    pastEventsList.add(item);
                }
            }

            if ( upcomingEventsList.isEmpty() ) {
                setUpcomingEventsCardToEmpty();
            }
            if ( pastEventsList.isEmpty() ) {
                setPastEventsCardToEmpty();
            }

            initializeUpcomingGamesCount();

            // Set the initial value of upcomingEventsCount, so that mUpcomingGamesAdapter
            // can initialize properly.
        } catch (Exception e) {
            Log.d("sortGames", "sortGames broke it!");
        }
    }

    public void setUpcomingEventsCardToEmpty() {
        LinearLayout upcomingEventsCardFooter =
                (LinearLayout) rootView.findViewById(R.id.upcoming_events_footer);
        TextView noUpcomingEventsText =
                (TextView) rootView.findViewById(R.id.upcoming_events_no_events_text);

        upcomingEventsProgressBar.setVisibility(View.GONE);
        upcomingEventsListView.setEmptyView(null);
        upcomingEventsListView.setVisibility(View.GONE);
        upcomingEventsCardFooter.setVisibility(View.GONE);
        noUpcomingEventsText.setVisibility(View.VISIBLE);
    }

    public void setPastEventsCardToEmpty() {
        LinearLayout pastEventsCardFooter =
                (LinearLayout) rootView.findViewById(R.id.past_events_card_footer);
        TextView noUpcomingPastGamesText =
                (TextView) rootView.findViewById(R.id.past_events_no_events_text);

        pastEventsCardFooter.setVisibility(View.GONE);
        noUpcomingPastGamesText.setVisibility(View.VISIBLE);
    }

    public void populateResultsTable(List<GenericSportsRssItem> pastGamesList) {
        for (int i = pastGamesList.size() - 1; i >= 0; i --) {

            // Since switching genders often cause this method to break the app, I put in a
            // check to see if the activity was fully initialized before trying to generate a
            // new table row. Probably not a good long term solution.
            if (getActivity() == null) {
                Log.d("popSched", "getActivity returned null!");
                return;
            }
            TableRow tableRow = new TableRow(getActivity());
            int tableRowHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12,
                    getResources().getDisplayMetrics());
            tableRow.setWeightSum(10f);
            tableRow.setPadding(0, 0, 0, tableRowHeight);

            GenericSportsRssItem item = pastGamesList.get(i);

            addViewToRow(item.getDatePast(), tableRow, 1.5f);
            addViewToRow(item.getOpponentOrEventWithPrefix(), tableRow, 6.5f);
            addViewToRow(item.getResult(), tableRow, 2f);

            pastEventsTableLayout.addView(tableRow);
        }
    }

    public void setTableLayoutHeightBasedOnChildren(TableLayout tableLayout) {
        if (tableLayout == null) {
            return;
        }

        // Check to make sure there's at least one past game
        if ( tableLayout.getChildAt(1) != null ) {
            View headerRow = tableLayout.getChildAt(0);
            headerRow.measure(0, 0);
            int heightOfHeaderRow = headerRow.getMeasuredHeight();


            View gameRow = tableLayout.getChildAt(1);
            gameRow.measure(0, 0);
            pastEventsItemHeight = gameRow.getMeasuredHeight();

            int starterCount;
            if (pastEventsList.size() <= 3) {
                starterCount = pastEventsList.size();
            } else {
                starterCount = 3;
                pastEventsRemainingToShow = pastEventsList.size() - starterCount;
            }

            int pastEventTableLayoutHeight = (pastEventsItemHeight * starterCount);

            int starterHeight = pastEventTableLayoutHeight + heightOfHeaderRow;

            ViewGroup.LayoutParams params = tableLayout.getLayoutParams();
            params.height = starterHeight;
            tableLayout.setLayoutParams(params);
            tableLayout.requestLayout();
        }
    }

    public int getPastEventsTableHeightToIncrease() {
        Log.d("getPastGames", "pastEventsRemainingToShow: " + pastEventsRemainingToShow);
        if ( pastEventsRemainingToShow >= 3) {
            int ret = measurePastEventsTableRows(3, pastEventsTableLayout);
            pastEventsRemainingToShow -= 3;
            return ret;
        } else {
            int ret = measurePastEventsTableRows(pastEventsRemainingToShow, pastEventsTableLayout);
            // Set pastEventsRemaining to show to zero, since the height to add will be
            // however many items are left.
            pastEventsRemainingToShow = 0;
            return ret;
        }
    }

    private int measurePastEventsTableRows(int numberToMeasure, TableLayout tableLayout) {
        int startingIndex = ((tableLayout.getChildCount() - 1) - pastEventsRemainingToShow);
        int totalHeight = 0;
        for (int i = startingIndex; i < startingIndex + numberToMeasure; i ++) {
            View pastItemRow = tableLayout.getChildAt(i);
            pastItemRow.measure(0, 0);
            totalHeight += pastItemRow.getMeasuredHeight();
        }

        return totalHeight;
    }

    /**
     * Adapter for the Upcoming Games card.
     */
    public abstract class UpcomingGamesAdapter extends BaseAdapter {

        int[] teamLogos = {R.drawable.western_michigan_broncos,
                R.drawable.bluffton_beavers, R.drawable.wisconsin_whitewater};

        public List<GenericSportsRssItem> items;

        public Context context;

        public UpcomingGamesAdapter(Context context, List<GenericSportsRssItem> items) {
            this.context = context;
            this.items = items;
        }

        public void updateChange(List<GenericSportsRssItem> list) {
            if ( ! items.isEmpty()) {
                items.clear();
            }
            items.addAll(list);
            this.notifyDataSetChanged();
        }

        // Not use static
        public class ViewHolder {
            public TextView date, title, location;
        }

        // Get the items to show at first. If the list is larger than 3 items, only show
        // the first 3. If it's not, show all of them.
        @Override
        public int getCount() {
            return getUpcomingEventsCount();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public abstract View getView(int position, View convertView, ViewGroup parent);
    }
}
