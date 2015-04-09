package com.techhab.rss;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by jhchoe on 12/29/14.
 */
public class EventsDom {

    private final OnContentLoaded listener;
    private Context context;

    private EventsRssItem event;
    private String building;
    private int duration = 1;
    private String date;

    public EventsDom(Context context, EventsRssItem event, OnContentLoaded listener) {
        this.context = context;
        this.event = event;
        this.listener = listener;

        new MyAsyncTask().execute(event.getLink());
    }

    private class MyAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... link) {
            try {
                StringBuffer buffer = new StringBuffer();

                Document doc = Jsoup.connect(link[0]).get();

                Elements elems = doc.select("p");

                for (Element elem : elems) {
                    String className = elem.className();
                    if (className.equals("date")) {
                        date = elem.text().split(": ")[1];
                        buffer.append("\n" + elem.text() + "\n");
                    } else if (className.equals("time")) {
                        date += ", " + elem.text().split(": ")[1];
                        buffer.append("\n" + elem.text() + "\n");
                    } else if (className.equals("duration")) {
                        duration = Integer.parseInt(elem.text().split(" ")[1]);
                        buffer.append("\n" + elem.text() + "\n");
                    } else if (className.equals("sponsor")) {
                        buffer.append("\n" + elem.text() + "\n");
                    } else if (className.equals("location")) {
                        building = elem.text().split(": ")[1];
                        buffer.append("\n" + elem.text() + "\n");
                    }
                }
                return buffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Error";
        }

        @Override
        protected void onPostExecute(String result) {
            if (EventsDom.this.listener != null) {
                EventsDom.this.listener.onContentLoaded(result, building, date, duration);
            }
        }
    }

    public interface OnContentLoaded {
        public void onContentLoaded(String content, String building, String date, int duration);
    }
}
