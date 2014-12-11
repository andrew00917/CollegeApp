package com.techhab.collegeapp;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.techhab.collegeapp.rss.RssItem;
import com.techhab.collegeapp.rss.RssService;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventsFragment extends Fragment {

    public static final String ARG_OBJECT = "object";

    public static final String ITEMS = "rssItemList";
    public static final String RECEIVER = "receiver";

    public static Intent mServiceIntent;
    public static MyResultReceiver receiver;

    ViewGroup p;
    View v;

    private static List<RssItem> rssItemList;
    private static RecyclerView mRecyclerView;
    private static RssAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public EventsFragment() {
        // Required Empty Constructor
    }

    public static Fragment createNewInstance() {
        EventsFragment fragment = new EventsFragment();
        Bundle arg = new Bundle();
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        p = parent;
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_events, parent, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
//        mRecyclerView.setHasFixedSize(false);

        String dataUrl = "https://reason.kzoo.edu/studentactivities/feeds/events";
        /*
         * Creates a new Intent to start the RSSPullService
         * IntentService. Passes a URI in the
         * Intent's "data" field.
         */
        mServiceIntent = new Intent(getActivity(), RssService.class);
        receiver = new MyResultReceiver(new Handler());
        rssItemList = (List<RssItem>) new ArrayList<RssItem>();
        new DownloadXmlTask().execute(dataUrl);

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


    public class RssAdapter extends RecyclerView.Adapter<RssAdapter.ViewHolder> {

        private List<RssItem> items;
        private final Context context;

        public RssAdapter(Context context, List<RssItem> items) {
            this.items = new ArrayList<RssItem>(items);
            this.context = context;
        }

        public void updateChange(List<RssItem> list) {
            items.clear();
            items.addAll(list);
            this.notifyDataSetChanged();
            // Data should reset above.
            // below code should not be needed
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
                                buttonPressed(v);
                                break;
                            case MotionEvent.ACTION_CANCEL:
                            case MotionEvent.ACTION_OUTSIDE:
                                buttonReleased(v);
                                break;
                            case MotionEvent.ACTION_UP:
                                buttonReleased(v);
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
            String[] text = items.get(position).getTitle().split(" - ");
            holder.date.setText(text[0]);
            holder.event.setText(text[1]);

            text = items.get(position).getDescription().split(", ");
            holder.place.setText(text[0]);
            holder.time.setText(text[1]);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.events_recycle, parent, false);
            return new ViewHolder(view);
        }

        /**
         * Button pressed method
         *
         * @param view button
         */
        public void buttonPressed(View view) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.button_pressed);
            view.startAnimation(animation);
        }

        /**
         * Button released method
         *
         * @param view button
         */
        public void buttonReleased(View view) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.button_released);
            view.startAnimation(animation);
        }
    }

    /*
     * Receiver
     */
    public class MyResultReceiver extends ResultReceiver {

        public MyResultReceiver(Handler handler) {
            super(handler);
            // TODO Auto-generated constructor stub
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultData != null) {
                rssItemList.clear();
                rssItemList.addAll((List<RssItem>) resultData.getSerializable(ITEMS));
                mRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        ((RssAdapter) mAdapter).updateChange(rssItemList);
                    }
                });
            }
        }
    }

    private class DownloadXmlTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                mServiceIntent.setData(Uri.parse(urls[0]));
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