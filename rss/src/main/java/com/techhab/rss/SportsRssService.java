package com.techhab.rss;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Griffin on 12/19/2014.
 */
public class SportsRssService extends IntentService {

    public static final String TAG = "RssApp";

    /*private static final String RSS_LINK =
            "http://hornets.kzoo.edu/sports/mbkb/2014-15/schedule?print=rss";*/
//    public static final String ITEMS = "sportsRssItemList";
//    public static final String RECEIVER = "sportsReceiver";

    public String RSS_LINK = "gender_preference";

    public String SPORT = "sport";
    public String ITEMS;
    public String RECEIVER;

    public SportsRssService() {
        super("RssService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Service started");
        RSS_LINK = intent.getStringExtra("gender_preference");
        SPORT = intent.getStringExtra("sport");
        if (SPORT.equals("basketball")) {
            ITEMS = "basketballRssItemList";
            RECEIVER = "basketballReceiver";
            List<BasketballRssItem> rssItems = null;
            try {
                BasketballParser parser = new BasketballParser();
                rssItems = parser.parse(getInputStream(RSS_LINK));
            } catch (Exception e) {
                Log.w(e.getMessage(), e);
            }
            Bundle bundle = new Bundle();
            bundle.putSerializable(ITEMS, (Serializable) rssItems);
            ResultReceiver receiver = intent.getParcelableExtra(RECEIVER);
            receiver.send(0, bundle);
        } else if (SPORT.equals("cross_country")) {
            ITEMS = "xcRssItemList";
            RECEIVER = "xcReceiver";
            List<CrossCountryRssItem> rssItems = null;
            try {
                CrossCountryParser parser = new CrossCountryParser();
                rssItems = parser.parse(getInputStream(RSS_LINK));
            } catch (Exception e) {
                Log.w(e.getMessage(), e);
            }
            Bundle bundle = new Bundle();
            bundle.putSerializable(ITEMS, (Serializable) rssItems);
            ResultReceiver receiver = intent.getParcelableExtra(RECEIVER);
            receiver.send(0, bundle);
        }
        Log.d(TAG, "Service ended");
    }

    public InputStream getInputStream(String link) {
        try {
            System.setProperty(link, "false");
            URL url = new URL(link);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setUseCaches(false);
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            return conn.getInputStream();
        } catch (IOException e) {
            Log.w(TAG, "Exception while retrieving the input stream", e);
            return null;
        }
    }
}

