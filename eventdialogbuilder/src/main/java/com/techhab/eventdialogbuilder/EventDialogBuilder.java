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
import java.util.Calendar;
import java.util.GregorianCalendar;

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

    private void setOnClickListener(String event, String building, String date) {
        final String e = event;
        final String b = building;

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
                ContentResolver cr = context.getContentResolver();
                ContentValues values = new ContentValues();
                // 1 is default calendar id - e.g. "My Calendar"
                values.put(CalendarContract.Events.CALENDAR_ID, 1);
                values.put(CalendarContract.Events.TITLE, e);
                values.put(CalendarContract.Events.EVENT_LOCATION, b);

                Calendar cal = Calendar.getInstance();
                try {
                    int year = Integer.parseInt(dateTime[2]);

                    cal.setTime(new SimpleDateFormat("MMMM").parse(dateTime[1].split(" ")[0]));
                    int month = cal.get(Calendar.MONTH);
                    int day = 0;

                    String dayString = dateTime[1].split(" ")[1];
                    if (dayString.length() < 4) {
                        day = Integer.parseInt("" + dayString.charAt(0));
                    } else {
                        day = Integer.parseInt("" + dayString.charAt(0) + dayString.charAt(1));
                    }

                    int hour = Integer.parseInt(dateTime[3].split(":")[0]);
                    if ( ! dateTime[3].split(":")[1].split(" ")[1].equals("am")) {
                        hour += 12;
                    }
                    int minute = Integer.parseInt(dateTime[3].split(":")[1].split(" ")[0]);

                    GregorianCalendar calDate = new GregorianCalendar(year, month, day, hour, minute);
                    values.put(CalendarContract.Events.DTSTART, calDate.getTimeInMillis());
                    values.put(CalendarContract.Events.DTEND, calDate.getTimeInMillis());
                    values.put(CalendarContract.Events.EVENT_TIMEZONE, "America/Detroit");
                    values.put(CalendarContract.Events.HAS_ALARM, true);

                    values.put(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_PRIVATE);
                    values.put(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);

                    Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

                    long eventID = Long.parseLong(uri.getLastPathSegment());

                    /* Add Reminder to CalendarContract.Reminders */
                    values = new ContentValues();
                    values.put(CalendarContract.Reminders.EVENT_ID, eventID);
                    values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
                    values.put(CalendarContract.Reminders.MINUTES, 15);

                    // updating uri with reminder values
                    uri = cr.insert(CalendarContract.Reminders.CONTENT_URI, values);

                    // notify the user that the event is now added to their private calendar.
                    Toast.makeText(context, "Event added to the calendar successfully", Toast.LENGTH_SHORT).show();
                } catch (ParseException p) {
                    p.printStackTrace();
                    Toast.makeText(context, "ParseException: formating error.", Toast.LENGTH_SHORT).show();
                }
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
        progressbar.setMax(100);
        progressbar.setProgress(0);

        title.setText(event);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        new MyAsyncTask().execute(link);
        dialog.show();
    }

    private class MyAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String...link) {
            publishProgress(10);
            try {
                StringBuffer buffer = new StringBuffer();
                Document doc = Jsoup.connect(link[0]).get();
                int progressInt = 50;
                publishProgress(progressInt);
                Elements elems = doc.select("p");

                String building = "";
                String date = "";
                for (Element elem : elems) {
                    String className = elem.className();
                    if (className.equals("date")) {
                        date = elem.text().split(": ")[1];
                        buffer.append("\n" + elem.text() + "\n");
                    } else if (className.equals("time")) {
                        date += ", " + elem.text().split(": ")[1];
                        buffer.append("\n" + elem.text() + "\n");
                    } else if (className.equals("duration") || className.equals("sponsor")) {
                        buffer.append("\n" + elem.text() + "\n");
                    } else if (className.equals("location")) {
                        building = elem.text().split(": ")[1];
                        buffer.append("\n" + elem.text() + "\n");
                    }
                    if (progressInt < 95) {
                        progressInt += 10;
                        publishProgress(progressInt);
                    }
                }
                Log.e("AsyncTask Date format: ", date);
                setOnClickListener(title.getText().toString(), building, date);
                publishProgress(98);
                return buffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Error";
        }

        @Override
        protected void onProgressUpdate(Integer...progress) {
            progressbar.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            content.setText(result);
            progressbar.setVisibility(ProgressBar.GONE);
        }
    }
}
