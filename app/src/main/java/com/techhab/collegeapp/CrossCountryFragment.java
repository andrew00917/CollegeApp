package com.techhab.collegeapp;

import android.app.Activity;
import android.support.v4.app.Fragment;
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

import com.techhab.rss.CrossCountryRssItem;
import com.techhab.rss.SportsRssService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Griffin on 12/26/2014.
 */
public class CrossCountryFragment extends Fragment {
    public static final String ARG_SCROLL_Y = "ARG_SCROLL_Y";

    public static final String GENDER = "gender";

    public static final String SPORT = "cross_country";

    public static final String ARG_OBJECT = "object";

    private static final String ITEMS = "xcRssItemList";
    public static final String RECEIVER = "xcReceiver";

    private Intent mServiceIntent;
    private MyResultReceiver receiver;
    private boolean gender;

    private List<CrossCountryRssItem> rssItemList;

    // Commented out until later date
//    private TableLayout rosterTableLayout;

    private ScrollView basketballScrollView;

    private TableLayout pastGamesTableLayout;
    private UpcomingGamesAdapter mUpcomingGamesAdapter;
    private ListView upcomingEventsListView;

    private LinearLayout upcomingGamesViewMoreButtonLayout, resultsCardFooter;
    private Button upcomingGamesViewMoreButton, pastGamesViewMoreButton;
    private CardView upcomingEventsCardView;
    private ProgressBar upcomingGamesProgressBar;

    private static int pastGamesTableLayoutHeight;
    private static int upcomingGamesListViewHeight;
    private int pastGamesRemainingToShow;
    private int upcomingGamesCount;
    private int gameRowHeight;
    private List<CrossCountryRssItem> upcomingGamesList = new ArrayList<>();
    private List<CrossCountryRssItem> pastGamesList = new ArrayList<>();

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
    public static CrossCountryFragment newInstance(String param1, String param2,
                                                 String genderPreference) {
        CrossCountryFragment fragment = new CrossCountryFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public CrossCountryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
            gender = getArguments().getBoolean(GENDER);
        }
        Log.d("CrossCountry Fragment onCreate", "gender = " + gender);
        mServiceIntent = new Intent(getActivity(), SportsRssService.class);
        if (gender) {
            mServiceIntent.putExtra("gender_preference",
                    "http://hornets.kzoo.edu/sports/mxc/2014-15/schedule?print=rss");
        } else {
            mServiceIntent.putExtra("gender_preference",
                    "http://hornets.kzoo.edu/sports/wxc/2014-15/schedule?print=rss");
        }
        mServiceIntent.putExtra("sport", "cross_country");
        receiver = new MyResultReceiver(new Handler());
        rssItemList = new ArrayList<>();
        new DownloadXmlTask().execute();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_cross_country, container, false);
        upcomingEventsListView = (ListView) rootView.findViewById(R.id.upcoming_events_listview);
        upcomingGamesViewMoreButton = (Button) rootView.findViewById(
                R.id.upcoming_games_view_more_button);
        upcomingEventsCardView = (CardView) rootView.findViewById(R.id.upcoming_events_card);
        upcomingGamesViewMoreButtonLayout = (LinearLayout) rootView.findViewById(
                R.id.upcoming_games_view_more_button_layout);
        upcomingGamesProgressBar = (ProgressBar) rootView.findViewById(
                R.id.upcoming_games_progress_bar);
        pastGamesTableLayout = (TableLayout) rootView.findViewById(R.id.past_games_table_layout);
        pastGamesViewMoreButton = (Button) rootView.findViewById(R.id.past_games_view_more_button);
        resultsCardFooter = (LinearLayout) rootView.findViewById(R.id.results_card_footer);
        upcomingEventsListView.setEmptyView(upcomingGamesProgressBar);

        /**
         * BEGIN OBSERVABLE SCROLL VIEW
         */

        final ObservableScrollView scrollView = (ObservableScrollView) rootView.findViewById(R.id.scrollview);
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

        upcomingGamesViewMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HeightAnimation animation = new HeightAnimation(upcomingEventsListView,
                        upcomingGamesListViewHeight, true);
                animation.setDuration(300);
                upcomingEventsCardView.startAnimation(animation);
                if (upcomingGamesCount == upcomingGamesList.size()) {
                    upcomingGamesViewMoreButtonLayout.setVisibility(View.GONE);
                }
                updateUpcomingGamesCount();
                // TODO: fix auto-scrolling
            }
        });

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
     * adapter knows how many more ListView items to load before the user hits "View More"
     */
    public void updateUpcomingGamesCount() {
        // If upcoming games list does not have 3 more items to add, just add however many
        // are remaining.
        if ( (upcomingGamesList.size() - upcomingGamesCount) < 3 ) {
            upcomingGamesCount = upcomingGamesCount +
                    (upcomingGamesList.size() - upcomingGamesCount);
        } else {
            upcomingGamesCount += 3;
        }
        mUpcomingGamesAdapter.notifyDataSetChanged();
    }

    public int getHeightToIncrease() {
        if ( pastGamesRemainingToShow >= 3) {
            pastGamesRemainingToShow -= 3;
            return (gameRowHeight * 3);
        } else {
            // Hide "View More" button
            resultsCardFooter.setVisibility(View.GONE);
            return gameRowHeight * pastGamesRemainingToShow;
        }
    }

    public void sortGames(List<CrossCountryRssItem> list) {
        CrossCountryRssItem item;
        try {
            for (int i = 0; i < list.size(); i++) {
                item = list.get(i);
                if (item.isUpcoming()) {
                    upcomingGamesList.add(item);
                } else {
                    pastGamesList.add(item);
                }
            }

            // Set the initial value of upcomingGamesCount, so that mUpcomingGamesAdapter
            // can initialize properly.
            if (upcomingGamesList.size() > 6) {
                upcomingGamesCount = 6;
            } else if (upcomingGamesList.size() > 3) {
                upcomingGamesCount = 3;
            } else {
                upcomingGamesCount = upcomingGamesList.size();
            }
        } catch (Exception e) {
            Log.d("sortGames", "sortGames broke it!");
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUpcomingGamesAdapter = new UpcomingGamesAdapter(getActivity(), rssItemList);
        upcomingEventsListView.setAdapter(mUpcomingGamesAdapter);
    }


    private void populateScheduleResultsTable(List<CrossCountryRssItem> pastGamesList) {
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

            CrossCountryRssItem item = pastGamesList.get(i);

            addViewToRow(item.getDatePast(), tableRow, 1.5f);
            addViewToRow("at " + item.getEvent(), tableRow, 6.5f);
            addViewToRow(item.getResult(), tableRow, 2f);

            pastGamesTableLayout.addView(tableRow);
        }
    }

    private void addViewToRow(String text, TableRow tableRow, float rowWeight) {
        TextView newTextView = new TextView(getActivity());
        newTextView.setText(text);
        newTextView.setTextColor(getResources().getColor(
                R.color.primary_text_default_material_light));
        newTextView.setTextSize(16);
        newTextView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT, rowWeight));
        tableRow.addView(newTextView);
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
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        UpcomingGamesAdapter listAdapter = (UpcomingGamesAdapter) listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < 3; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = upcomingGamesListViewHeight = totalHeight +
                (listView.getDividerHeight() * (3));
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
            gameRowHeight = gameRow.getMeasuredHeight();

            int starterCount;
            if (pastGamesList.size() <= 3) {
                starterCount = pastGamesList.size();
            } else {
                starterCount = 3;
                pastGamesRemainingToShow = pastGamesList.size() - starterCount;
            }

            pastGamesTableLayoutHeight = (gameRowHeight * starterCount);


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

        private List<CrossCountryRssItem> items;

        private Context context;

        public UpcomingGamesAdapter(Context context, List<CrossCountryRssItem> items) {
            this.context = context;
            this.items = items;
        }

        public void updateChange(List<CrossCountryRssItem> list) {
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
                convertView = inflater.inflate(R.layout.upcoming_xc_events_row, parent, false);

                viewHolder = new ViewHolder();
                viewHolder.date = (TextView) convertView.findViewById(R.id.event_date);
                viewHolder.title = (TextView) convertView.findViewById(R.id.event_title);
                viewHolder.location = (TextView) convertView.findViewById(R.id.event_location);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            CrossCountryRssItem item = items.get(position);

            viewHolder.date.setText(item.getDateAndTimeUpcoming());
            viewHolder.title.setText(item.getEvent());

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
            rssItemList = (List<CrossCountryRssItem>) resultData.getSerializable(ITEMS);
            Log.d("onReceiveResult", "Fragment: " + this.getClass() + ". This is RssItemList = " + rssItemList);
            sortGames(rssItemList);
            mUpcomingGamesAdapter.updateChange(upcomingGamesList);
            setListViewHeightBasedOnChildren(upcomingEventsListView);
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
