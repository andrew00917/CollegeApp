package com.techhab.rss;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.CalendarContract;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

/**
 * Created by jhchoe on 12/29/14.
 */
public class Calendar {

    private Context context;

    public Calendar(Context context) {
        this.context = context;
    }

    public void insertEvent(String event, String building, int duration, String date) {
        final String e = event;
        final String b = building;
        final int d = duration;
        final String[] dateTime = formatDate(date);

        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();
        // 1 is default calendar id - e.g. "My Calendar"
        values.put(CalendarContract.Events.CALENDAR_ID, 1);
        values.put(CalendarContract.Events.TITLE, e);
        values.put(CalendarContract.Events.EVENT_LOCATION, b);

        java.util.Calendar cal = java.util.Calendar.getInstance();
        try {
            int year = Integer.parseInt(dateTime[2]);

            cal.setTime(new SimpleDateFormat("MMMM").parse(dateTime[1].split(" ")[0]));
            int month = cal.get(java.util.Calendar.MONTH);
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

            GregorianCalendar calDateStart = new GregorianCalendar(year, month, day, hour, minute);
            GregorianCalendar calDateEnd = new GregorianCalendar(year, month, day, hour + d, minute);
            values.put(CalendarContract.Events.DTSTART, calDateStart.getTimeInMillis());
            values.put(CalendarContract.Events.DTEND, calDateEnd.getTimeInMillis());
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

    public void deleteEvent() {

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
}
