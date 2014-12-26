package com.techhab.collegeapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
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

import com.techhab.rss.SportsRssItem;
import com.techhab.rss.SportsRssService;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BasketballFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BasketballFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BasketballFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String ARG_SCROLL_Y = "ARG_SCROLL_Y";

    public static final String ARG_OBJECT = "object";

    private static final String ITEMS = "sportsRssItemList";
    public static final String RECEIVER = "sportsReceiver";

    private Intent mServiceIntent;
    private MyResultReceiver receiver;

    private List<SportsRssItem> rssItemList;

    // Commented out until later date
//    private TableLayout rosterTableLayout;

    private ScrollView basketballScrollView;

    private TableLayout scheduleTableLayout;
    private UpcomingGamesAdapter mUpcomingGamesAdapter;
    private ListView upcomingGamesListView;

    private LinearLayout upcomingGamesViewMoreButtonLayout;
    private Button upcomingGamesViewMoreButton;
    private CardView upcomingGamesCardView;
    private ProgressBar upcomingGamesProgressBar;

    private static int upcomingGamesListViewHeight;
    private int upcomingGamesCount;
    private List<SportsRssItem> upcomingGamesList = new ArrayList<>();
    private List<SportsRssItem> pastGamesList = new ArrayList<>();

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
    public static BasketballFragment newInstance(String param1, String param2) {
        BasketballFragment fragment = new BasketballFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mServiceIntent = new Intent(getActivity(), SportsRssService.class);
        receiver = new MyResultReceiver(new Handler());
        rssItemList = new ArrayList<>();
        new DownloadXmlTask().execute();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_basketball, container, false);
        scheduleTableLayout = (TableLayout) rootView.findViewById(R.id.schedule_table_layout);
        upcomingGamesListView = (ListView) rootView.findViewById(R.id.upcoming_games_listview);
        upcomingGamesViewMoreButton = (Button) rootView.findViewById(
                R.id.upcoming_games_view_more_button);
        upcomingGamesCardView = (CardView) rootView.findViewById(R.id.upcoming_games_card);
        upcomingGamesViewMoreButtonLayout = (LinearLayout) rootView.findViewById(
                R.id.upcoming_games_view_more_button_layout);
        upcomingGamesProgressBar = (ProgressBar) rootView.findViewById(
                R.id.upcoming_games_progress_bar);
        /*basketballScrollView = (ScrollView) rootView.findViewById(
                R.id.basketball_fragment_scrollview);*/

        upcomingGamesListView.setEmptyView(upcomingGamesProgressBar);

        /**
         *
         * BEGIN OBSERVABLE SCROLL VIEW
         *
         */

        final ObservableScrollView scrollView = (ObservableScrollView) rootView.findViewById(R.id.basketball_fragment_scrollview);
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
         *
         * END OBSERVABLE SCROLL VIEW
         *
         */





        upcomingGamesViewMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HeightAnimation animation = new HeightAnimation(upcomingGamesListView,
                        upcomingGamesListViewHeight, true);
                animation.setDuration(400);
                upcomingGamesCardView.startAnimation(animation);
                if (upcomingGamesCount == upcomingGamesList.size()) {
                    upcomingGamesViewMoreButtonLayout.setVisibility(View.GONE);
                }

                updateUpcomingGamesCount();
                // TODO: fix auto-scrolling
//                basketballScrollView.scrollTo(0, upcomingGamesViewMoreButton.getBottom());

            }
        });

        String[] dates = new String[] { "Nov. 9", "Nov. 15", "Dec. 20"};

        String[] opponents = new String[] { "at Western Mich.", "at Bluffton",
                "at Wis.-Whitewater"};




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
            upcomingGamesCount = upcomingGamesCount + 3;
        }
        mUpcomingGamesAdapter.notifyDataSetChanged();
    }

    public void sortGames(List<SportsRssItem> list) {
        SportsRssItem item;
        for (int i = 0; i < list.size(); i++) {
            item = list.get(i);
            if ( item.isUpcoming()) {
                upcomingGamesList.add(item);
            } else {
                pastGamesList.add(item);
            }
        }

        // Set the initial value of upcomingGamesCount, so that mUpcomingGamesAdapter
        // can initialize properly.
        if ( upcomingGamesList.size() > 6 ) {
            upcomingGamesCount = 6;
        } else if (upcomingGamesList.size() > 3 ) {
            upcomingGamesCount = 3;
        } else {
            upcomingGamesCount = upcomingGamesList.size();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUpcomingGamesAdapter = new UpcomingGamesAdapter(getActivity(), rssItemList);
        upcomingGamesListView.setAdapter(mUpcomingGamesAdapter);
    }


    private void populateScheduleResultsTable(List<SportsRssItem> pastGamesList,
                                              String[] results) {
        for (int i = 0; i < 3; i ++) {
            TableRow tableRow = new TableRow(getActivity());
            int tableRowHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12,
                    getResources().getDisplayMetrics());
//            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
//                    tableRowHeight, 10f));
            tableRow.setWeightSum(10f);
            tableRow.setPadding(0, 0, 0, tableRowHeight);

            SportsRssItem item = pastGamesList.get(i);

            addViewToRow(item.getDatePast(), tableRow, 1.5f);
            if ( item.isAtHome() ) {
                addViewToRow("vs. " + item.getOpponent(), tableRow, 6.5f);
            } else {
                addViewToRow("at " + item.getOpponent(), tableRow, 6.5f);
            }
            addViewToRow(item.getResult(), tableRow, 2f);

            scheduleTableLayout.addView(tableRow);
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

    /**
     * Adapter for the Upcoming Games card.
     */
    private class UpcomingGamesAdapter extends BaseAdapter {

        int[] teamLogos = {R.drawable.western_michigan_broncos,
                R.drawable.bluffton_beavers, R.drawable.wisconsin_whitewater};

        private List<SportsRssItem> items;

        private Context context;

        public UpcomingGamesAdapter(Context context, List<SportsRssItem> items) {
            this.context = context;
            this.items = items;
        }

        public void updateChange(List<SportsRssItem> list) {
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
                convertView = inflater.inflate(R.layout.upcoming_games_row, parent, false);

                viewHolder = new ViewHolder();
                viewHolder.date = (TextView) convertView.findViewById(R.id.game_date);
                viewHolder.title = (TextView) convertView.findViewById(R.id.opponent_title);
                viewHolder.location = (TextView) convertView.findViewById(R.id.home_away);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            SportsRssItem item = items.get(position);

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
            rssItemList = (List<SportsRssItem>) resultData.getSerializable(ITEMS);
            sortGames(rssItemList);
            mUpcomingGamesAdapter.updateChange(upcomingGamesList);
            String[] results = new String[] { "L, 81-32", "L, 81-69", "W, 100-0"};
            populateScheduleResultsTable(pastGamesList, results);
            setListViewHeightBasedOnChildren(upcomingGamesListView);
        }
    }

    /**
     *  Background task to start service for Rss
     */
    private class DownloadXmlTask extends AsyncTask<Void, Void, String> {

        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            /*progress = new ProgressDialog(getActivity());
            progress.setMessage("loading games");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.show();*/
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
