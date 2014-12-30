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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.techhab.rss.CrossCountryRssItem;
import com.techhab.rss.GenericSportsRssItem;
import com.techhab.rss.SportsRssService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Griffin on 12/26/2014.
 */
public class CrossCountryFragment extends GenericSportsFragment {
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

    private TableLayout pastEventsTableLayout;
    private ListView upcomingEventsListView;

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
            gender = getArguments().getBoolean(GENDER);
        }
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
        pastEventsTableLayout = (TableLayout) rootView.findViewById(R.id.past_events_table_layout);

        initializeGenericOnCreateView(rootView);

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

//        populateTeamRosterTable(athleteNames, athleteNumbers);

        return rootView;
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

    public class CrossCountryUpcomingEventsAdapter extends UpcomingGamesAdapter {

        public CrossCountryUpcomingEventsAdapter(Context context, List<GenericSportsRssItem> items) {
            super(context, items);
        }

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
            GenericSportsRssItem item = items.get(position);

            viewHolder.date.setText(item.getDateAndTimeUpcoming());
            viewHolder.title.setText(item.getOpponentOrEvent());
            viewHolder.location.setText(item.getLocation());

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
            sortGames(rssItemList);
            CrossCountryUpcomingEventsAdapter mUpcomingGamesAdapter =
                    new CrossCountryUpcomingEventsAdapter(getActivity(),upcomingEventsList);
            upcomingEventsListView.setAdapter(mUpcomingGamesAdapter);
            setListViewHeightBasedOnChildren(upcomingEventsListView);
            populateResultsTable(pastEventsList);
            setTableLayoutHeightBasedOnChildren(pastEventsTableLayout);
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
