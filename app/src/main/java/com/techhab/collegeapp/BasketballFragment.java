package com.techhab.collegeapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.techhab.rss.BasketballRssItem;
import com.techhab.rss.SportsRssService;

import java.util.ArrayList;
import java.util.List;

public class BasketballFragment extends GenericSportsFragment {
    public static final String ARG_SCROLL_Y = "ARG_SCROLL_Y";

    public static final String GENDER = "gender";
    public static final String SPORT = "sport";

    public static final String ARG_OBJECT = "object";

    private static final String ITEMS = "basketballRssItemList";
    public static final String RECEIVER = "basketballReceiver";

    private Intent mServiceIntent;
    private MyResultReceiver receiver;
    private boolean gender;

    private List<BasketballRssItem> rssItemList;

    // Commented out until later date
//    private TableLayout rosterTableLayout;

    private ScrollView basketballScrollView;

    private TableLayout pastGamesTableLayout;
    private UpcomingGamesAdapter mUpcomingGamesAdapter;
    private ListView upcomingGamesListView;

    private LinearLayout upcomingGamesViewMoreButtonLayout, resultsCardFooter;
    private Button upcomingGamesViewMoreButton, pastGamesViewMoreButton;
    private CardView upcomingGamesCardView;
    private ProgressBar upcomingGamesProgressBar;

    private static int pastGamesTableLayoutHeight;
    private static int upcomingGamesListViewHeight;
    private int upcomingGamesItemHeight;
    private int upcomingGamesHeightToAdd;
    private int upcomingGamesHeightAdded;
    private int pastGamesRemainingToShow;
    private int upcomingGamesCount; // Count to keep track of how many items to
    // load in the adapter
    private int pastGamesItemHeight;
    private List<BasketballRssItem> upcomingGamesList = new ArrayList<>();
    private List<BasketballRssItem> pastGamesList = new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BasketballFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BasketballFragment newInstance(String param1, String param2,
                                                 String genderPreference) {
        BasketballFragment fragment = new BasketballFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public BasketballFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            gender = getArguments().getBoolean(GENDER);
        }
        mServiceIntent = new Intent(getActivity(), SportsRssService.class);
        if (gender) {
            mServiceIntent.putExtra("gender_preference",
                    "http://hornets.kzoo.edu/sports/mbkb/2014-15/schedule?print=rss");
        } else {
            mServiceIntent.putExtra("gender_preference",
                    "http://hornets.kzoo.edu/sports/wbkb/2014-15/schedule?print=rss");
        }
        mServiceIntent.putExtra("sport", "basketball");
        receiver = new MyResultReceiver(new Handler());
        rssItemList = new ArrayList<>();
        new DownloadXmlTask().execute();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_basketball, container, false);
        upcomingGamesListView = (ListView) rootView.findViewById(R.id.upcoming_games_listview);
        upcomingGamesViewMoreButton = (Button) rootView.findViewById(
                R.id.upcoming_games_view_more_button);
        upcomingGamesCardView = (CardView) rootView.findViewById(R.id.upcoming_games_card);
        upcomingGamesViewMoreButtonLayout = (LinearLayout) rootView.findViewById(
                R.id.upcoming_games_view_more_button_layout);
        upcomingGamesProgressBar = (ProgressBar) rootView.findViewById(
                R.id.upcoming_games_progress_bar);
        pastGamesTableLayout = (TableLayout) rootView.findViewById(R.id.past_games_table_layout);
        pastGamesViewMoreButton = (Button) rootView.findViewById(
                R.id.past_games_view_more_button);
        resultsCardFooter = (LinearLayout) rootView.findViewById(R.id.results_card_footer);
        upcomingGamesListView.setEmptyView(upcomingGamesProgressBar);

        mUpcomingGamesAdapter = new UpcomingGamesAdapter(getActivity(), rssItemList);
        upcomingGamesListView.setAdapter(mUpcomingGamesAdapter);

        /**
         * BEGIN OBSERVABLE SCROLL VIEW
         */

        final ObservableScrollView scrollView =
                (ObservableScrollView) rootView.findViewById(R.id.scrollview);
        Activity parentActivity = getActivity();

        if (parentActivity instanceof ObservableScrollViewCallbacks) {
            // Scroll to the specified offset after layout
            Bundle args = getArguments();
            if (args != null && args.containsKey(ARG_SCROLL_Y)) {
                final int scrollY = args.getInt(ARG_SCROLL_Y, 0);
                ViewTreeObserver vto = scrollView.getViewTreeObserver();
                vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            scrollView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        } else {
                            scrollView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                        scrollView.scrollTo(0, scrollY);
                    }
                });
            }
            scrollView.setScrollViewCallbacks((ObservableScrollViewCallbacks) parentActivity);
        }

        /**
         * END OBSERVABLE SCROLL VIEW
         */

        setButtonOnClickListener(upcomingGamesViewMoreButton);

        /*upcomingGamesViewMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (upcomingGamesViewMoreButton.getText().equals("VIEW MORE")) {
                    // Sometimes the height to add variable doesn't get set, so check to make
                    // sure it has a value.
                    if (upcomingGamesHeightToAdd == 0 ) {
                        initializeUpcomingGamesCount();
                    }
                    HeightAnimation animation = new HeightAnimation(upcomingGamesListView,
                            upcomingGamesHeightToAdd, true);
                    animation.setDuration(300);
                    upcomingGamesCardView.startAnimation(animation);
                    if (upcomingGamesCount == upcomingGamesList.size()) {
                        upcomingGamesViewMoreButton.setText(R.string.collapse_list);
                    }
                    upcomingGamesHeightAdded += upcomingGamesHeightToAdd;
                    updateUpcomingGamesCount();
                } else {
                    HeightAnimation animation = new HeightAnimation(upcomingGamesListView,
                            upcomingGamesHeightAdded, false);
                    animation.setDuration(300);
                    upcomingGamesCardView.startAnimation(animation);
                    upcomingGamesHeightAdded = 0;
                    initializeUpcomingGamesCount();
                    upcomingGamesViewMoreButton.setText(R.string.view_more);
                }
                // TODO: fix auto-scrolling
            }
        });*/

        pastGamesViewMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HeightAnimation animation = new HeightAnimation(pastGamesTableLayout,
                        getHeightToIncrease(), true);
                animation.setDuration(300);
                pastGamesTableLayout.startAnimation(animation);

                // TODO: fix auto-scrolling
//                scrollView.scrollTo(0, pastGamesTableLayout.getBottom());
            }
        });

//        populateTeamRosterTable(athleteNames, athleteNumbers);

        return rootView;
    }

    /**
     * Updates the upcomingGamesCount for the mUpcomingGamesAdapter. This way, the
     * adapter knows how many more ListView items to load before the user hits "View More".
     */
    public void updateUpcomingGamesCount() {
        // If upcoming games list does not have 3 more items to add, just add however many
        // are remaining.
        int upcomingGamesRemaining = upcomingGamesList.size() - upcomingGamesCount;
        if ( upcomingGamesRemaining < 3 ) {
            upcomingGamesCount += upcomingGamesRemaining;
            upcomingGamesHeightToAdd = upcomingGamesRemaining * upcomingGamesItemHeight;
        } else {
            upcomingGamesCount += 3;
            upcomingGamesHeightToAdd = 3 * upcomingGamesItemHeight;
        }


        mUpcomingGamesAdapter.notifyDataSetChanged();
    }

    public int getHeightToIncrease() {
        if ( pastGamesRemainingToShow >= 3) {
            pastGamesRemainingToShow -= 3;
            return (pastGamesItemHeight * 3);
        } else {
            // Hide "View More" button
            resultsCardFooter.setVisibility(View.GONE);
            return pastGamesItemHeight * pastGamesRemainingToShow;
        }
    }

    public void sortGames(List<BasketballRssItem> list) {
        BasketballRssItem item;
        try {
            for (int i = 0; i < list.size(); i++) {
                item = list.get(i);
                if ( item.isUpcoming() ) {
                    upcomingGamesList.add(item);
                } else {
                    pastGamesList.add(item);
                }
            }

            initializeUpcomingGamesCount();

            // Set the initial value of upcomingGamesCount, so that mUpcomingGamesAdapter
            // can initialize properly.
        } catch (Exception e) {
            Log.d("sortGames", "sortGames broke it!");
        }
    }

    private void initializeUpcomingGamesCount() {
        if ( upcomingGamesList.size() > 6 ) {
            upcomingGamesCount = 6;
            upcomingGamesHeightToAdd = 3 * upcomingGamesItemHeight;
        } else if ( upcomingGamesList.size() > 3 ){
            upcomingGamesCount = upcomingGamesList.size();
            upcomingGamesHeightToAdd = (upcomingGamesCount - 3) * upcomingGamesItemHeight;
        } else {
            upcomingGamesCount = upcomingGamesList.size();
            upcomingGamesHeightToAdd = 0;
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*mUpcomingGamesAdapter = new UpcomingGamesAdapter(getActivity(), rssItemList);
        upcomingGamesListView.setAdapter(mUpcomingGamesAdapter);*/
    }


    private void populateScheduleResultsTable(List<BasketballRssItem> pastGamesList) {
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

            BasketballRssItem item = pastGamesList.get(i);

            addViewToRow(item.getDatePast(), tableRow, 1.5f);
            if ( item.isAtHome() ) {
                addViewToRow("vs. " + item.getOpponent(), tableRow, 6.5f);
            } else {
                addViewToRow("at " + item.getOpponent(), tableRow, 6.5f);
            }
            addViewToRow(item.getResult(), tableRow, 2f);

            pastGamesTableLayout.addView(tableRow);
        }
    }

    // Commented out for future usage
    /*private void populateTeamRosterTable(String[] athleteNames, String[] athleteNumbers) {
        for (int i = 0; i < athleteNames.length; i ++) {
            TableRow tableRow = new TableRow(getActivity());
            int tableRowHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12,
                    getResources().getDisplayMetrics());
//            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
//                    tableRowHeight, 10f));
            tableRow.setWeightSum(10f);
            tableRow.setPadding(0, 0, 0, tableRowHeight);

            TextView athleteName = new TextView(getActivity());
            TextView athleteNumber = new TextView(getActivity());

            athleteName.setText(athleteNames[i]);
            athleteNumber.setText(athleteNumbers[i]);

            athleteName.setTextColor(getResources().getColor(
                    R.color.primary_text_default_material_light));
            athleteNumber.setTextColor(getResources().getColor(
                    R.color.primary_text_default_material_light));

            athleteName.setTextSize(16);
            athleteNumber.setTextSize(16);

            athleteNumber.setGravity(Gravity.CENTER);

            athleteNumber.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT, 1.5f));
            athleteName.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT, 8.5f));

            tableRow.addView(athleteNumber);
            tableRow.addView(athleteName);
            rosterTableLayout.addView(tableRow);
        }

    }*/

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
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
        for (int i = 0; i < 3; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            upcomingGamesItemHeight = listItem.getMeasuredHeight() + listView.getDividerHeight();
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (3));
        listView.setLayoutParams(params);
        listView.requestLayout();
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
            pastGamesItemHeight = gameRow.getMeasuredHeight();

            int starterCount;
            if (pastGamesList.size() <= 3) {
                starterCount = pastGamesList.size();
            } else {
                starterCount = 3;
                pastGamesRemainingToShow = pastGamesList.size() - starterCount;
            }

            pastGamesTableLayoutHeight = (pastGamesItemHeight * starterCount);


            int starterHeight = pastGamesTableLayoutHeight + heightOfHeaderRow;

            ViewGroup.LayoutParams params = tableLayout.getLayoutParams();
            params.height = starterHeight;
            tableLayout.setLayoutParams(params);
            tableLayout.requestLayout();
        }

        /*for (int i = 0; i < 3; i++) {
            listItem.measure(0, 0);
            heightToAdd += listItem.getMeasuredHeight();
        }
        View listItem = linearLayout.getChildAt(0);*/
    }

    /**
     * Adapter for the Upcoming Games card.
     */
    private class UpcomingGamesAdapter extends BaseAdapter {

        int[] teamLogos = {R.drawable.western_michigan_broncos,
                R.drawable.bluffton_beavers, R.drawable.wisconsin_whitewater};

        private List<BasketballRssItem> items;

        private Context context;

        public UpcomingGamesAdapter(Context context, List<BasketballRssItem> items) {
            this.context = context;
            this.items = items;
        }

        public void updateChange(List<BasketballRssItem> list) {
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
            return upcomingGamesCount;
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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                LayoutInflater inflater =
                        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.upcoming_basketball_games_row, parent, false);

                viewHolder = new ViewHolder();
                viewHolder.date = (TextView) convertView.findViewById(R.id.game_date);
                viewHolder.title = (TextView) convertView.findViewById(R.id.opponent_title);
                viewHolder.location = (TextView) convertView.findViewById(R.id.home_away);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            BasketballRssItem item = items.get(position);

            viewHolder.date.setText(item.getDateAndTimeUpcoming());
            viewHolder.title.setText(item.getOpponent());

            if ( item.isAtHome() ) {
                viewHolder.location.setText("HOME");
            } else {
                viewHolder.location.setText("AWAY");
            }

            return convertView;
        }
    }


    /**
     * Rss Receiver
     */
    public class MyResultReceiver extends ResultReceiver {

        public MyResultReceiver(Handler handler) {
            super(handler);
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            rssItemList = (List<BasketballRssItem>) resultData.getSerializable(ITEMS);
            sortGames(rssItemList);
            mUpcomingGamesAdapter.updateChange(upcomingGamesList);
            setListViewHeightBasedOnChildren(upcomingGamesListView);
            populateScheduleResultsTable(pastGamesList);
            setTableLayoutHeightBasedOnChildren(pastGamesTableLayout);
        }
    }

    /**
     *  Background task to start service for Rss
     */
    private class DownloadXmlTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(Void...voids) {
            try {
                mServiceIntent.putExtra(RECEIVER, receiver);
                // Starts the IntentService
                getActivity().startService(mServiceIntent);
                return "Intent Service Started";
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "Error";
        }

        @Override
        protected void onPostExecute(String result) {
        }
    }
}
