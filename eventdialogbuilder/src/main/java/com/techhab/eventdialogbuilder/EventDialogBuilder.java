package com.techhab.eventdialogbuilder;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by jhchoe on 12/27/14.
 */
public class EventDialogBuilder extends MaterialDialog.Builder {

    private Context context;
    private View layout;

    private MaterialDialog dialog;

    private TextView title;
    private TextView content;
    private ImageView close;

    private ImageButton campus;
    private ImageButton map;
    private ImageButton calendar;
    private ImageButton check;

    private ProgressBar progressbar;

    public EventDialogBuilder(Context context) {
        super(context);
        this.context = context;
    }

    public void onCreate() {
        dialog = build();
    }

    public void setCustomView(View layout) {
        this.layout = layout;
        customView(layout);

        title = (TextView) layout.findViewById(R.id.title);
        content = (TextView) layout.findViewById(R.id.content);
        close = (ImageView) layout.findViewById(R.id.close);

        campus = (ImageButton) layout.findViewById(R.id.campus_button);
        map = (ImageButton) layout.findViewById(R.id.mapit_button);
        calendar = (ImageButton) layout.findViewById(R.id.calendar_button);
        check = (ImageButton) layout.findViewById(R.id.attending_button);
    }

    private void setOnClickListener(String event, String building, int duration, String date) {
        final String e = event;
        final String b = building;
        final int d = duration;

        final String[] dateTime = formatDate(date);

        campus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, b, Toast.LENGTH_SHORT).show();
            }
        });
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, b, Toast.LENGTH_SHORT).show();
            }
        });
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Attending", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     *  Watch out for the element at index 1, "month day" will result together.
     *
     * @param date  "day, month day, year, time" in format of "Saturday, January 10th, 2015, 9:30 pm"
     * @return
     */
    private String[] formatDate(String date) {
        return date.split(", ");
    }

    public void showDialog(String event, String link) {
        if (dialog == null) {
            onCreate();
        }
        if (dialog.getCustomView() == null) {
            customView(R.layout.event_dialog_custom);
            setCustomView(dialog.getCustomView());
        }

        progressbar = (ProgressBar) layout.findViewById(R.id.progress_bar);

        title.setText(event);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

        new MyAsyncTask().execute(link);
    }

    private class MyAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String...link) {
//            int progressInt = 0;
            try {
                StringBuffer buffer = new StringBuffer();
//                while (progressInt < 40) {
//                    //Sleep for up to one second.
//                    try {
//                        Thread.sleep(2);
//                    } catch (InterruptedException ignore) {
//
//                    }
//                    progressInt += 1;
//                    publishProgress(progressInt);
//                }
                Document doc = Jsoup.connect(link[0]).get();
//                progressInt = 60;
//                publishProgress(progressInt);

                Elements elems = doc.select("p");

                String building = "";
                int duration = 1;
                String date = "";
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
//                    if (progressInt < 95) {
//                        progressInt += 10;
//                        publishProgress(progressInt);
//                    }
                }
                setOnClickListener(title.getText().toString(), building, duration, date);
//                publishProgress(100);
                return buffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Error";
        }

        @Override
        protected void onProgressUpdate(Integer...progress) {
//            progressbar.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            content.setText(result);
//            progressbar.setVisibility(ProgressBar.GONE);
        }
    }
}
