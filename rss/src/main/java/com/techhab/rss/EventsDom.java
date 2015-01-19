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

    private Context context;

    private TextView contentView;
    private View calendar;
    private com.techhab.kcollegecustomviews.ProgressBar progress;

    private String event;
    private String building;
    private int duration = 1;
    private String date;

    public EventsDom(Context context, String event, TextView contentView, View calendar, String link
            , com.techhab.kcollegecustomviews.ProgressBar progress) {
        this.context = context;

        this.event = event;

        this.contentView = contentView;
        this.calendar = calendar;
        this.progress = progress;

        if (contentView.getText().toString().equals("")) {
            new MyAsyncTask().execute(link);
        } else {
            progress.setVisibility(ProgressBar.GONE);
            setOnClickListenerForCalendar();
        }
    }

    public void setOnClickListenerForCalendar() {
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = new Calendar(context);
                cal.insertEvent(event, building, duration, date);
            }
        });
    }

    private class MyAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String...link) {
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
                setOnClickListenerForCalendar();
                return buffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Error";
        }

        @Override
        protected void onPostExecute(String result) {
            contentView.setText(result);
//            HeightAnimation animation = new HeightAnimation(progress, progress.getHeight(), false);
//            animation.setDuration(300);
//            progress.startAnimation(animation);
            progress.setVisibility(ProgressBar.GONE);
        }
    }
}
