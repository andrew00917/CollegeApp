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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;

import com.techhab.rss.SportsRssItem;
import com.techhab.rss.SportsRssService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Griffin on 12/30/2014.
 */
public class StandardSportsFragment extends GenericSportsFragment {
    public static final String ARG_SCROLL_Y = "ARG_SCROLL_Y";

    public static final String GENDER = "gender";
    public static final String SPORT_TITLE = "sport_title";

    public static final String ARG_OBJECT = "object";

    private static final String ITEMS = "sportsRssItemList";
    public static final String RECEIVER = "sportsReceiver";

    private Intent mServiceIntent;
    private MyResultReceiver receiver;
    private boolean gender;
    private String sportTitle;

    private List<SportsRssItem> rssItemList;

    // Commented out until later date
//    private TableLayout rosterTableLayout;

    private StandardSportUpcomingEventsAdapter mStandardSportUpcomingEventsAdapter;
    private ListView upcomingGamesListView;
    private ProgressBar upcomingGamesProgressBar;
    private TableLayout pastGamesTableLayout;
    private TextView upcomingEventsCardTitle;

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
     * @return A new instance of fragment StandardSportsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StandardSportsFragment newInstance(String param1, String param2,
                                           String genderPreference) {
        StandardSportsFragment fragment = new StandardSportsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public StandardSportsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            gender = getArguments().getBoolean(GENDER);
            sportTitle = getArguments().getString(SPORT_TITLE);
        }
        mServiceIntent = new Intent(getActivity(), SportsRssService.class);
        mServiceIntent.putExtra("gender_preference", getRssUrl());
        receiver = new MyResultReceiver(new Handler());
        rssItemList = new ArrayList<>();
        new DownloadXmlTask().execute();

    }

    private String getRssUrl() {
        String idSuffix = sportTitle.toLowerCase();
        idSuffix = idSuffix.replace(" ", "_");
        idSuffix = idSuffix.replace("/", "_");
        String id;

        Log.d("getRssUrl", "gender = " + gender );

        if ( gender ) {
            id = "mens_" + idSuffix;
        } else {
            id = "womens_" + idSuffix;
        }

        Log.d("getRssUrl", "id = " + id );

        int finalID = getActivity().getResources().getIdentifier(id, "string", getActivity().getPackageName());

//        return getResources().getString(R.string.mens_golf);

        return getResources().getString(finalID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_sport, container, false);
        upcomingGamesListView = (ListView) rootView.findViewById(R.id.upcoming_events_listview);
        upcomingGamesProgressBar = (ProgressBar) rootView.findViewById(
                R.id.upcoming_games_progress_bar);
        pastGamesTableLayout = (TableLayout) rootView.findViewById(R.id.past_events_table_layout);
        upcomingEventsCardTitle = (TextView) rootView.findViewById(
                R.id.upcoming_events_card_title);

        if ( isTeamSport(sportTitle) ) {
            upcomingEventsCardTitle.setText(getResources().getString(R.string.upcoming_games));
        } else {
            upcomingEventsCardTitle.setText(getResources().getString(R.string.upcoming_events));
        }

        upcomingGamesListView.setEmptyView(upcomingGamesProgressBar);



        initializeGenericOnCreateView(rootView);

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

//        populateTeamRosterTable(athleteNames, athleteNumbers);

        return rootView;
    }

    private boolean isTeamSport(String sportTitle) {
        sportTitle = sportTitle.toLowerCase();
        String[] teamSports = new String[] {
                "baseball",
                "softball",
                "basketball",
                "football",
                "soccer",
                "volleyball",
                "lacrosse"
        };
        for (int i = 0; i < teamSports.length; i ++) {
            if (sportTitle.contains(teamSports[i]) ) {
                return true;
            }
        }
        return false;
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
     * Fragment-specific upcoming games adapter, which basically just overrides the
     * "getView" method of its superclass. getView() is responsible for how each ListView item
     * is layed out and such.
     */
    public class StandardSportUpcomingEventsAdapter extends GenericSportsFragment.UpcomingGamesAdapter {

        public StandardSportUpcomingEventsAdapter(Context context, List<SportsRssItem> items) {
            super(context, items);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null && context != null) {

                LayoutInflater inflater =
                        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                if (sportTitle.equalsIgnoreCase("cross country") ) {
                    convertView = inflater.inflate(R.layout.upcoming_xc_events_row, parent, false);
                } else {
                    convertView = inflater.inflate(R.layout.upcoming_team_games_row, parent, false);
                }

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
            viewHolder.title.setText(item.getOpponentOrEvent());

            if ( item.isTeamSport() ) {
                if (item.isAtHome()) {
                    viewHolder.location.setText("HOME");
                } else {
                    viewHolder.location.setText("AWAY");
                }
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
            mStandardSportUpcomingEventsAdapter = new StandardSportUpcomingEventsAdapter(getActivity(),
                    upcomingEventsList);
            upcomingGamesListView.setAdapter(mStandardSportUpcomingEventsAdapter);
            setListViewHeightBasedOnChildren(upcomingGamesListView);
            populateResultsTable(pastEventsList);
            setTableLayoutHeightBasedOnChildren(pastGamesTableLayout);
        }
    }

    /**
     * Background task to start service for Rss
     */
    private class DownloadXmlTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(Void... voids) {
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
