package com.techhab.collegeapp;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.techhab.rss.EventsRssItem;
import com.techhab.rss.EventsRssService;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventsWednesdayFragment extends Fragment {

    public static final String ARG_OBJECT = "object";

    private static final String ITEMS = "rssItemList";
    private static final String RECEIVER = "receiver";

    private Intent mServiceIntent;
    private MyResultReceiver receiver;

    View v;

    private List<EventsRssItem> rssItemList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RssAdapter mAdapter;

    public EventsWednesdayFragment() {
        // Required Empty Constructor
    }

    public static Fragment createNewInstance() {
        EventsWednesdayFragment fragment = new EventsWednesdayFragment();
        Bundle arg = new Bundle();
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

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

    public List<EventsRssItem> getWednesdayEvent(List<EventsRssItem> list) {
        List<EventsRssItem> ret = new ArrayList<>();
        EventsRssItem item;
        for (int i = 0; i < list.size(); i++) {
            item = list.get(i);
            if (item.getEvent().contains("Wednesday")) {
                ret.add(item);
            }
        }
        return ret;
    }


    /**
     * Adapter for recycler view
     */
    public class RssAdapter extends RecyclerView.Adapter<RssAdapter.ViewHolder> {

        private List<EventsRssItem> items;

        public RssAdapter(Context context, List<EventsRssItem> items) {
            this.items = items;
        }

        public void updateChange(List<EventsRssItem> list) {
            if ( ! items.isEmpty()) {
                items.clear();
            }
            items.addAll(list);
            this.notifyDataSetChanged();
        }

        // Not use static
        public class ViewHolder extends RecyclerView.ViewHolder {

            public ImageView image;
            public TextView date, event, place, time;

            public ViewHolder(View itemView) {
                super(itemView);
                image = (ImageView) itemView.findViewById(R.id.image);
                date = (TextView) itemView.findViewById(R.id.date);
                event = (TextView) itemView.findViewById(R.id.event);
                place = (TextView) itemView.findViewById(R.id.place);
                time = (TextView) itemView.findViewById(R.id.time);
                itemView.setOnTouchListener(new View.OnTouchListener(){

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                ((EventsActivity) getActivity()).buttonPressed(v);
                                break;
                            case MotionEvent.ACTION_CANCEL:
                            case MotionEvent.ACTION_OUTSIDE:
                                ((EventsActivity) getActivity()).buttonReleased(v);
                                break;
                            case MotionEvent.ACTION_UP:
                                ((EventsActivity) getActivity()).buttonReleased(v);
                                break;
                        }
                        return true;
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            EventsRssItem item = items.get(position);
            holder.date.setText(item.getDate());
            holder.event.setText(item.getEvent());
            holder.place.setText(item.getPlace());
            holder.time.setText(item.getTime());
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.events_recycle, parent, false);
            return new ViewHolder(view);
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
            rssItemList = (List<EventsRssItem>) resultData.getSerializable(ITEMS);
            rssItemList = getWednesdayEvent(rssItemList);
            mAdapter.updateChange(rssItemList);
        }
    }

    /**
     *  Background task to start service for Rss
     */
    private class DownloadXmlTask extends AsyncTask<Void, Void, String> {
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