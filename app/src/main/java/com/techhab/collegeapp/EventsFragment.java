package com.techhab.collegeapp;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.techhab.kcollegecustomviews.ProgressBar;
import com.techhab.rss.EventsDom;
import com.techhab.rss.EventsRssItem;
import com.techhab.rss.EventsRssService;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventsFragment extends Fragment {

    public static final String ARG_POSITION = "position";

    private static final String ITEMS = "rssItemList";
    private static final String RECEIVER = "receiver";

    private int POSITION;

    private Intent mServiceIntent;
    private MyResultReceiver receiver;

    View v;

    private List<EventsRssItem> rssItemList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RssAdapter mAdapter;

    public EventsFragment() {
        // Required Empty Constructor
    }

    public static Fragment createNewInstance() {
        EventsFragment fragment = new EventsFragment();
        Bundle arg = new Bundle();
//        POSITION = arg.getInt(ARG_POSITION);
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        Bundle arg = getArguments();
        POSITION = arg.getInt(ARG_POSITION);

        /*
         * Creates a new Intent to start the RssService
         */
        mServiceIntent = new Intent(getActivity(), EventsRssService.class);
        receiver = new MyResultReceiver(new Handler());
        rssItemList = new ArrayList<>();
        new DownloadXmlTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_events, parent, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new RssAdapter(getActivity(), rssItemList);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public List<EventsRssItem> getEtcEvent(List<EventsRssItem> list) {
        List<EventsRssItem> ret = new ArrayList<>();
        EventsRssItem item;
        String event;
        for (int i = 0; i < list.size(); i++) {
            item = list.get(i);
            event = item.getEvent().toLowerCase();
            if ( ! (event.contains("stress free") || event.contains("tuesdays with") || event.contains("wind down wednesday")
                    || event.contains("trivia night") || event.contains("zoo flicks") || event.contains("zoo after dark"))) {
                ret.add(item);
            }
        }
        return ret;
    }

    public List<EventsRssItem> getStressEvent(List<EventsRssItem> list) {
        List<EventsRssItem> ret = new ArrayList<>();
        EventsRssItem item;
        for (int i = 0; i < list.size(); i++) {
            item = list.get(i);
            if (item.getEvent().toLowerCase().contains("stress free")) {
                ret.add(item);
            }
        }
        return ret;
    }

    public List<EventsRssItem> getTuesdayEvent(List<EventsRssItem> list) {
        List<EventsRssItem> ret = new ArrayList<>();
        EventsRssItem item;
        for (int i = 0; i < list.size(); i++) {
            item = list.get(i);
            if (item.getEvent().toLowerCase().contains("tuesdays with")) {
                ret.add(item);
            }
        }
        return ret;
    }

    public List<EventsRssItem> getWednesdayEvent(List<EventsRssItem> list) {
        List<EventsRssItem> ret = new ArrayList<>();
        EventsRssItem item;
        for (int i = 0; i < list.size(); i++) {
            item = list.get(i);
            if (item.getEvent().toLowerCase().contains("wind down wednesday")) {
                ret.add(item);
            }
        }
        return ret;
    }

    public List<EventsRssItem> getTriviaEvent(List<EventsRssItem> list) {
        List<EventsRssItem> ret = new ArrayList<>();
        EventsRssItem item;
        for (int i = 0; i < list.size(); i++) {
            item = list.get(i);
            if (item.getEvent().toLowerCase().contains("trivia night")) {
                ret.add(item);
            }
        }
        return ret;
    }

    public List<EventsRssItem> getFlicksEvent(List<EventsRssItem> list) {
        List<EventsRssItem> ret = new ArrayList<>();
        EventsRssItem item;
        for (int i = 0; i < list.size(); i++) {
            item = list.get(i);
            if (item.getEvent().toLowerCase().contains("zoo flicks")) {
                ret.add(item);
            }
        }
        return ret;
    }

    public List<EventsRssItem> getZooDarkEvent(List<EventsRssItem> list) {
        List<EventsRssItem> ret = new ArrayList<>();
        EventsRssItem item;
        for (int i = 0; i < list.size(); i++) {
            item = list.get(i);
            if (item.getEvent().toLowerCase().contains("zoo after dark")) {
                ret.add(item);
            }
        }
        return ret;
    }


    /**
     * Adapter for recycler view
     */
    public class RssAdapter extends RecyclerView.Adapter<RssAdapter.ViewHolder> {

        private Context context;
        private List<EventsRssItem> items;
        private int expandedPosition = -1;
        private ViewHolder expandedHolder;
//        private int width = -1;
//        private int height = -1;

        public RssAdapter(Context context, List<EventsRssItem> items) {
            this.context = context;
            this.items = items;
        }

        public void updateChange(List<EventsRssItem> list) {
            if ( ! items.isEmpty()) {
                items.clear();
            }
            if (list != null && ! list.isEmpty()) {
                items.addAll(list);
            }
            this.notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final EventsRssItem item;
            if (position < items.size()) {
                item = items.get(position);
            } else {
                return;
            }
            String[] event = item.getEvent().split(" ");

            if (event[0].toLowerCase().equals("stress")) {
                holder.image.setBackground(getResources().getDrawable(R.drawable.stree_free_zone));
            } else if (event[0].toLowerCase().equals("tuesdays")) {
                holder.image.setBackground(getResources().getDrawable(R.drawable.tuesdays_with));
            } else if (event[0].toLowerCase().equals("wind")) {
                holder.image.setBackground(getResources().getDrawable(R.drawable.wind_down_wed));
            } else if (event[0].toLowerCase().equals("trivia")) {
                holder.image.setBackground(getResources().getDrawable(R.drawable.trivia_night));
            } else if (event[1].toLowerCase().equals("flicks")) {
                holder.image.setBackground(getResources().getDrawable(R.drawable.zoo_flicks));
            } else if (event[1].toLowerCase().equals("after")) {
                holder.image.setBackground(getResources().getDrawable(R.drawable.zoo_after_dark));
            } else {
                holder.image.setBackground(getResources().getDrawable(R.drawable.banner));
            }
            holder.date.setText(item.getDate());
            holder.event.setText(item.getEvent());
            holder.time.setText(item.getTime());
        }

        @Override
        public RssAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.events_recycle, parent, false);

            return new ViewHolder(context, view);
        }

        /**
         *  Generate jsoup DOM to set description of the event's cards and set calendar
         *  button on click listener
         *
         * @param h
         * @param link
         */
        private void setDescription(final ViewHolder h, String link) {
            new EventsDom(context, h.event.getText().toString(), h.description,
                    h.calendarButton, link, h.progress);
        }

        /**
         *  Collapse card with description and buttons
         *
         * @param h
         */
        private void collapseCard(final ViewHolder h) {
            h.description.setVisibility(View.GONE);
            h.favoriteButton.setVisibility(View.GONE);
            h.buildingButton.setVisibility(View.GONE);
            h.calendarButton.setVisibility(View.GONE);
            h.attendButton.setVisibility(View.GONE);
        }

        /**
         *  Expand card with description and buttons
         *
         * @param h
         */
        private void expandCard(final ViewHolder h) {
            h.progress.setVisibility(View.VISIBLE);
            h.description.setVisibility(View.VISIBLE);
            h.favoriteButton.setVisibility(View.VISIBLE);
            h.buildingButton.setVisibility(View.VISIBLE);
            h.calendarButton.setVisibility(View.VISIBLE);
            h.attendButton.setVisibility(View.VISIBLE);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public Context context;

            public View v;
            public FrameLayout image;
            public TextView date, event, description, time;
            public ProgressBar progress;
            public View divider;
            public LinearLayout buttonSection;
            public ImageButton infoButton;
            public ImageView favoriteButton, buildingButton, calendarButton, attendButton;

            public ViewHolder(Context context, View itemView) {
                super(itemView);

                this.context = context;

                v = itemView;

                image = (FrameLayout) v.findViewById(R.id.image);
                date = (TextView) v.findViewById(R.id.date);
                event = (TextView) v.findViewById(R.id.event);
                description = (TextView) v.findViewById(R.id.place);
                time = (TextView) v.findViewById(R.id.time);

                progress = (ProgressBar) v.findViewById(R.id.progress_bar);

                divider = v.findViewById(R.id.divider);

                buttonSection = (LinearLayout) v.findViewById(R.id.button_section);

                infoButton = (ImageButton) v.findViewById(R.id.info_button);
                infoButton.setTag(this);
                infoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final ViewHolder vTag = (ViewHolder) v.getTag();
                        final int position = vTag.getPosition();

                        // TODO: fix auto-scrolling
                        // Check for an expanded view, collapse if you find one
                        if (expandedPosition >= 0 && expandedPosition != position) {
                            collapseCard(expandedHolder);
                            expandedPosition = -1;
                            expandedHolder = null;
                        }

                        if (expandedPosition == position) {
                            collapseCard(vTag);
                            expandedPosition = -1;
                            expandedHolder = null;
                        } else {
                            // Set the current position to "expanded"
                            expandCard(vTag);
                            expandedPosition = position;
                            setDescription(vTag, items.get(position).getLink());
                            expandedHolder = vTag;
                        }
                        Log.d("Holder clicked: ", vTag.toString());
                        Toast.makeText(getActivity(), "Holder on click " + position, Toast.LENGTH_SHORT).show();
                    }
                });

                favoriteButton = (ImageView) v.findViewById(R.id.favorite_button);
                buildingButton = (ImageView) v.findViewById(R.id.building_button);
                calendarButton = (ImageView) v.findViewById(R.id.calendar_button);
                attendButton = (ImageView) v.findViewById(R.id.attending_button);
            }
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
            ((EventsActivity) getActivity()).dismissProgressBar();
            rssItemList = (List<EventsRssItem>) resultData.getSerializable(ITEMS);
            switch (POSITION) {
                case 0:
                    // ETC
                    rssItemList = getEtcEvent(rssItemList);
                    break;
                case 1:
                    // Upcoming
                    break;
                case 2:
                    // Stress free
                    rssItemList = getStressEvent(rssItemList);
                    break;
                case 3:
                    // Tuesday
                    rssItemList = getTuesdayEvent(rssItemList);
                    break;
                case 4:
                    // Wind down
                    rssItemList = getWednesdayEvent(rssItemList);
                    break;
                case 5:
                    // Trivia
                    rssItemList = getTriviaEvent(rssItemList);
                    break;
                case 6:
                    // Zoo flicks
                    rssItemList = getFlicksEvent(rssItemList);
                    break;
                case 7:
                    // Zoo after
                    rssItemList = getZooDarkEvent(rssItemList);
                    break;
            }
            mAdapter.updateChange(rssItemList);
        }
    }

    /**
     *  Background task to start service for Rss
     */
    private class DownloadXmlTask extends AsyncTask<Void, Integer, String> {

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