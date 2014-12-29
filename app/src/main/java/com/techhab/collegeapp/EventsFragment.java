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
import com.techhab.rss.EventsRssItem;
import com.techhab.rss.EventsRssService;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventsFragment extends Fragment {

    public static final String ARG_OBJECT = "object";

    private static final String ITEMS = "rssItemList";
    private static final String RECEIVER = "receiver";

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


    /**
     * Adapter for recycler view
     */
    public class RssAdapter extends RecyclerView.Adapter<RssAdapter.ViewHolder> {

        private Context context;
        private List<EventsRssItem> items;

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

        // Not use static
        public class ViewHolder extends RecyclerView.ViewHolder {

            public View v;
            public FrameLayout image;
            public TextView date, event, place, time;
            public View divider;
            public LinearLayout buttonSection;
            public ImageButton infoButton;
            public ImageView favoriteButton, buildingButton, calendarButton, attendButton;
            public boolean cardExpanded = false;
            public boolean buttonExpanded = false;

            public ViewHolder(View itemView) {
                super(itemView);
                v = itemView;

                image = (FrameLayout) v.findViewById(R.id.image);
                date = (TextView) v.findViewById(R.id.date);
                event = (TextView) v.findViewById(R.id.event);
                place = (TextView) v.findViewById(R.id.place);
                time = (TextView) v.findViewById(R.id.time);

                divider = v.findViewById(R.id.divider);

                buttonSection = (LinearLayout) v.findViewById(R.id.button_section);

                infoButton = (ImageButton) v.findViewById(R.id.info_button);
                favoriteButton = (ImageView) v.findViewById(R.id.favorite_button);
                buildingButton = (ImageView) v.findViewById(R.id.building_button);
                calendarButton = (ImageView) v.findViewById(R.id.calendar_button);
                attendButton = (ImageView) v.findViewById(R.id.attending_button);
            }

        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        @Override
        public void onBindViewHolder(ViewHolder h, int position) {
            final ViewHolder holder = h;
            final EventsRssItem item = items.get(position);
            String event = item.getEvent();
            if (event.contains("Stress Free Zone")) {
                holder.image.setBackground(getResources().getDrawable(R.drawable.stree_free_zone));
            } else if (event.contains("Tuesdays With")) {
                holder.image.setBackground(getResources().getDrawable(R.drawable.tuesdays_with));
            } else if (event.contains("Wind Down Wednesday")) {
                holder.image.setBackground(getResources().getDrawable(R.drawable.wind_down_wed));
            } else if (event.contains("Trivia Night")) {
                holder.image.setBackground(getResources().getDrawable(R.drawable.trivia_night));
            } else if (event.contains("Zoo Flicks")) {
                holder.image.setBackground(getResources().getDrawable(R.drawable.zoo_flicks));
            } else if (event.contains("Zoo After Dark")) {
                holder.image.setBackground(getResources().getDrawable(R.drawable.zoo_after_dark));
            } else {
                holder.image.setBackground(getResources().getDrawable(R.drawable.banner));
            }
            holder.date.setText(item.getDate());
            holder.event.setText(item.getEvent());
            holder.place.setText(item.getPlace());
            holder.time.setText(item.getTime());

            holder.image.setOnTouchListener(new View.OnTouchListener() {

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
                            // TODO: fix auto-scrolling
                            HeightAnimation animation;
                            int height = holder.buttonSection.getHeight() + 15;
                            if (holder.cardExpanded) {
                                animation = new HeightAnimation(holder.place, height, false);
                                holder.cardExpanded = false;
                            } else {
                                animation = new HeightAnimation(holder.place, height, true);
                                holder.cardExpanded = true;
                            }
                            animation.setDuration(300);
                            holder.place.startAnimation(animation);

                            WidthAnimation widthAnimation;
                            int width = holder.buttonSection.getHeight();
                            if (holder.buttonExpanded) {
                                widthAnimation = new WidthAnimation(holder.favoriteButton, width, false);
                                widthAnimation.setDuration(300);
                                holder.favoriteButton.startAnimation(widthAnimation);
                                widthAnimation = new WidthAnimation(holder.buildingButton, width, false);
                                widthAnimation.setDuration(300);
                                holder.buildingButton.startAnimation(widthAnimation);
                                widthAnimation = new WidthAnimation(holder.calendarButton, width, false);
                                widthAnimation.setDuration(300);
                                holder.calendarButton.startAnimation(widthAnimation);
                                widthAnimation = new WidthAnimation(holder.attendButton, width, false);
                                widthAnimation.setDuration(300);
                                holder.attendButton.startAnimation(widthAnimation);
                                holder.buttonExpanded = false;
                            } else {
                                widthAnimation = new WidthAnimation(holder.favoriteButton, width, true);
                                widthAnimation.setDuration(300);
                                holder.favoriteButton.startAnimation(widthAnimation);
                                widthAnimation = new WidthAnimation(holder.buildingButton, width, true);
                                widthAnimation.setDuration(300);
                                holder.buildingButton.startAnimation(widthAnimation);
                                widthAnimation = new WidthAnimation(holder.calendarButton, width, true);
                                widthAnimation.setDuration(300);
                                holder.calendarButton.startAnimation(widthAnimation);
                                widthAnimation = new WidthAnimation(holder.attendButton, width, true);
                                widthAnimation.setDuration(300);
                                holder.attendButton.startAnimation(widthAnimation);
                                holder.buttonExpanded = true;
                            }
                            break;
                    }
                    return true;
                }
            });
            holder.place.setOnTouchListener(new View.OnTouchListener() {

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
                            if (holder.buttonSection.getVisibility() == LinearLayout.GONE) {
                                holder.buttonSection.setVisibility(LinearLayout.VISIBLE);
                            } else {
                                holder.buttonSection.setVisibility(LinearLayout.GONE);
                            }
                            break;
                    }
                    return true;
                }
            });

            holder.infoButton.setOnTouchListener(new View.OnTouchListener() {

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
                            ((EventsActivity) getActivity()).showInfoDialog(item.getEvent(), item.getLink());
                            break;
                    }
                    return true;
                }
            });
            holder.favoriteButton.setOnTouchListener(new View.OnTouchListener(){

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
            mAdapter.updateChange(rssItemList);
            ((EventsActivity) getActivity()).dismissProgressBar();
        }
    }

    /**
     *  Background task to start service for Rss
     */
    private class DownloadXmlTask extends AsyncTask<Void, Integer, String> {

        int p = 0;

        @Override
        protected String doInBackground(Void...voids) {
            publishProgress(p);
            try {
                mServiceIntent.putExtra(RECEIVER, receiver);
                // Starts the IntentService
                getActivity().startService(mServiceIntent);
                while (p < 90) {
                    //Sleep for up to one second.
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException ignore) {

                    }
                    p += 1;
                    publishProgress(p);
                }
                return "Intent Service Started";
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "Error";
        }

        @Override
        protected void onProgressUpdate(Integer...progress) {
            super.onProgressUpdate(progress);
            ((EventsActivity) getActivity()).updateProgressBar(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }

}