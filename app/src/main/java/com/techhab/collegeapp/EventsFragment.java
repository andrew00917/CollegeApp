package com.techhab.collegeapp;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.techhab.kcollegecustomviews.ProgressBar;
import com.techhab.rss.Calendar;
import com.techhab.rss.EventsDom;
import com.techhab.rss.EventsRssItem;
import com.techhab.rss.EventsRssService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.gmariotti.cardslib.library.cards.topcolored.TopColoredCard;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardExpand;
import it.gmariotti.cardslib.library.internal.ViewToClickToExpand;
import it.gmariotti.cardslib.library.recyclerview.internal.CardArrayRecyclerViewAdapter;
import it.gmariotti.cardslib.library.recyclerview.view.CardRecyclerView;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventsFragment extends BaseObservableRecyclerFragment {

    public static final String ARG_POSITION = "position";
    private static final String ITEMS = "rssItemList";
    private static final String RECEIVER = "receiver";

    private Intent mServiceIntent;
    private MyResultReceiver receiver;
    private List<EventsRssItem> rssItemList = new ArrayList<>();
    private CustomCardArrayRecyclerViewAdapter mAdapter;

    public EventsFragment() {
        // Required Empty Constructor
    }

    public static Fragment createNewInstance(Integer position) {
        EventsFragment fragment = new EventsFragment();
        Bundle arg = new Bundle();
        arg.putInt(ARG_POSITION, position);
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mAdapter = new CustomCardArrayRecyclerViewAdapter(getActivity(), cards);
        mCardRecyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    protected List<Card> setupRecyclerCards() {
        //just return empty list for full fill later
        return new ArrayList<>();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_events;
    }

    private void setupRecycleView() {
        for (int i = 0; i < rssItemList.size(); i++) {
            cards.add(initializeCardView(rssItemList.get(i)));
        }
    }

    private CustomCard initializeCardView(final EventsRssItem eventsRssItem) {
        CustomCard card = new CustomCard(getActivity(), eventsRssItem);


        ViewToClickToExpand viewToClickToExpand = ViewToClickToExpand.builder().enableForExpandAction();
        card.setViewToClickToExpand(viewToClickToExpand);

        card.setOnClickListener(new Card.OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                //setup card expand
                CustomCardExpand cardExpand = new CustomCardExpand(getActivity(), eventsRssItem);
                card.addCardExpand(cardExpand);
                card.doToogleExpand();
            }
        });

        return card;
    }


    class CustomCardArrayRecyclerViewAdapter extends CardArrayRecyclerViewAdapter {

        public CustomCardArrayRecyclerViewAdapter(Context context, List<Card> cards) {
            super(context, cards);
        }

        public void updateChange() {
            //recreate new cards list
            cards.clear();
            setupRecycleView();
            this.notifyDataSetChanged();
        }
    }

    public List<EventsRssItem> getEtcEvent(List<EventsRssItem> list) {
        List<EventsRssItem> ret = new ArrayList<>();
        EventsRssItem item;
        String event;
        for (int i = 0; i < list.size(); i++) {
            item = list.get(i);
            event = item.getEvent().toLowerCase();
            if (!(event.contains("stress free") || event.contains("tuesdays with") || event.contains("wind down wednesday")
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

    class CustomCard extends Card {
        private EventsRssItem item;

        public CustomCard(Context context, EventsRssItem item) {
            this(context);
            this.item = item;
        }

        public CustomCard(Context context) {
            super(context, R.layout.events_recycle);
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View view) {
            FrameLayout image = (FrameLayout) view.findViewById(R.id.image);
            TextView date = (TextView) view.findViewById(R.id.date);
            TextView tvEvent = (TextView) view.findViewById(R.id.event);
            TextView time = (TextView) view.findViewById(R.id.time);

            String[] event = item.getEvent().split(" ");

            if (event[0].toLowerCase().equals("stress")) {
                image.setBackground(getResources().getDrawable(R.drawable.stree_free_zone));
            } else if (event[0].toLowerCase().equals("tuesdays")) {
                image.setBackground(getResources().getDrawable(R.drawable.tuesdays_with));
            } else if (event[0].toLowerCase().equals("wind")) {
                image.setBackground(getResources().getDrawable(R.drawable.wind_down_wed));
            } else if (event[0].toLowerCase().equals("trivia")) {
                image.setBackground(getResources().getDrawable(R.drawable.trivia_night));
            } else if (event[1].toLowerCase().equals("flicks")) {
                image.setBackground(getResources().getDrawable(R.drawable.zoo_flicks));
            } else if (event[1].toLowerCase().equals("after")) {
                image.setBackground(getResources().getDrawable(R.drawable.zoo_after_dark));
            } else {
                image.setBackground(getResources().getDrawable(R.drawable.banner));
            }
            date.setText(item.getDate());
            tvEvent.setText(item.getEvent());
            time.setText(item.getTime());
        }
    }

    class CustomCardExpand extends CardExpand {

        private final Context context;
        private EventsRssItem event;
        private boolean isContentLoaded = false;

        public CustomCardExpand(Context context, EventsRssItem event) {
            this(context);
            this.event = event;
        }

        public CustomCardExpand(Context context) {
            super(context, R.layout.events_recycle_expand);
            this.context = context;
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View view) {
            final ProgressBar progress = (ProgressBar) view.findViewById(R.id.progress_bar);
            final LinearLayout buttonSection = (LinearLayout) view.findViewById(R.id.button_section);
            final LinearLayout layoutProgressBar = (LinearLayout) view.findViewById(R.id.layout_progress_bar);
            final TextView tvDescription = (TextView) view.findViewById(R.id.place);

            //ImageView favoriteButton = (ImageView) view.findViewById(R.id.favorite_button);
            //ImageView buildingButton = (ImageView) view.findViewById(R.id.building_button);
            final ImageView calendarButton = (ImageView) view.findViewById(R.id.calendar_button);
            //ImageView attendButton = (ImageView) view.findViewById(R.id.attending_button);
            String description = tvDescription.getText().toString();
            if (!isContentLoaded) {
                /*
                 * Generate jsoup DOM to set description of the event's cards and set calendar
                 * button on click listener
                 */
                new EventsDom(context, event, new EventsDom.OnContentLoaded() {
                    @Override
                    public void onContentLoaded(String content, final String building, final String date, final int duration) {
                        layoutProgressBar.setVisibility(View.GONE);
                        tvDescription.setVisibility(View.VISIBLE);
                        tvDescription.setText(content);

                        buttonSection.setVisibility(View.VISIBLE);
                        calendarButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Calendar cal = new Calendar(context);
                                cal.insertEvent(event.getEvent(), building, duration, date);
                            }
                        });
                        isContentLoaded = true;
                    }
                });
            } else {
                layoutProgressBar.setVisibility(View.GONE);
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
            switch (tabPosition) {
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
            mAdapter.updateChange();
        }
    }

    /**
     * Background task to start service for Rss
     */
    private class DownloadXmlTask extends AsyncTask<Void, Integer, String> {

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