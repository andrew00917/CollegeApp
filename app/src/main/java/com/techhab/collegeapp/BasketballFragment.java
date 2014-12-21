package com.techhab.collegeapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


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

    public static final String ARG_OBJECT = "object";

    private TableLayout rosterTableLayout, scheduleTableLayout;
    private UpcomingGamesAdapter mUpcomingGamesAdapter;
    private ListView upcomingGamesListView;

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_basketball, container, false);
        rosterTableLayout = (TableLayout) rootView.findViewById(R.id.roster_table_layout);
        scheduleTableLayout = (TableLayout) rootView.findViewById(R.id.schedule_table_layout);
        upcomingGamesListView = (ListView) rootView.findViewById(R.id.upcoming_games_listview);

        // I'm too lazy to make a custom class for player objects, so i'm just gonna have
        // two strings for two diff types of data.
        String[] athleteNumbers = new String[] { "4", "10", "12",
                "15", "17", "19", "20" };
        String[] athleteNames = new String[] { "Cam Schwartz", "Stephen Oliphant", "Matt Jong",
                "Mike Oravetz", "Charlie Carson", "Aaron Schoenfeldt", "Roger Hood" };

        String[] dates = new String[] { "Nov. 9", "Nov. 15", "Dec. 20"};

        String[] opponents = new String[] { "at Western Mich.", "at Bluffton",
                "at Wis.-Whitewater"};

        String[] results = new String[] { "L, 81-32", "L, 81-69", "W, 100-0"};


        populateTeamRosterTable(athleteNames, athleteNumbers);
        populateScheduleResultsTable(dates, opponents, results);

        mUpcomingGamesAdapter = new UpcomingGamesAdapter(getActivity());
        upcomingGamesListView.setAdapter(mUpcomingGamesAdapter);
        setListViewHeightBasedOnChildren(upcomingGamesListView);
        return rootView;
    }

    private void populateScheduleResultsTable(String[] dates, String[] opponents,
                                              String[] results) {
        for (int i = 0; i < dates.length; i ++) {
            TableRow tableRow = new TableRow(getActivity());
            int tableRowHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12,
                    getResources().getDisplayMetrics());
//            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
//                    tableRowHeight, 10f));
            tableRow.setWeightSum(10f);
            tableRow.setPadding(0, 0, 0, tableRowHeight);

            TextView date = new TextView(getActivity());
            TextView opponent = new TextView(getActivity());
            TextView result = new TextView(getActivity());

            date.setText(dates[i]);
            opponent.setText(opponents[i]);
            result.setText(results[i]);

            date.setTextColor(getResources().getColor(
                    R.color.primary_text_default_material_light));
            opponent.setTextColor(getResources().getColor(
                    R.color.primary_text_default_material_light));
            result.setTextColor(getResources().getColor(
                    R.color.primary_text_default_material_light));

            date.setTextSize(16);
            opponent.setTextSize(16);
            result.setTextSize(16);

//            athleteNumber.setGravity(Gravity.CENTER);

            date.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT, 1.5f));
            opponent.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT, 6.5f));
            result.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT, 2f));

            tableRow.addView(date);
            tableRow.addView(opponent);
            tableRow.addView(result);
            scheduleTableLayout.addView(tableRow);
        }
    }


    private void populateTeamRosterTable(String[] athleteNames, String[] athleteNumbers) {
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

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

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

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        UpcomingGamesAdapter listAdapter = (UpcomingGamesAdapter) listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount()));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private class UpcomingGamesAdapter extends BaseAdapter {

        // ULTIMATELY I WILL USE COMPLEX DATA OBJECTS TO POPULATE THE DIFFERENT VIEWS,
        // BUT FOR NOW I WILL USE SIMPLE ARRAYS
        String[] dates = new String[] { "Nov. 9, 2:00PM", "Nov. 15, 6:00PM", "Dec. 20, 8:30PM"};

        String[] opponents = new String[] { "at Western Mich.", "at Bluffton",
                "at Wis.-Whitewater"};

        int[] teamLogos = {R.drawable.western_michigan_broncos,
                R.drawable.bluffton_beavers, R.drawable.wisconsin_whitewater};

        private Context context;

        public UpcomingGamesAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Object getItem(int position) {
            return opponents[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = null;
            if (convertView == null) {
                LayoutInflater inflater =
                        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.upcoming_games_row, parent, false);
            } else {
                row = convertView;
            }
            ImageView opponentLogo = (ImageView) row.findViewById(R.id.opponent_logo);
            TextView opponentTitle = (TextView) row.findViewById(R.id.opponent_title);
            TextView gameDate = (TextView) row.findViewById(R.id.game_date);

            opponentLogo.setImageResource(teamLogos[position]);
            opponentTitle.setText(opponents[position]);
            gameDate.setText(dates[position]);
            return row;
        }
    }
}
