package com.techhab.rss;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Griffin on 12/19/2014.
 */
public class CrossCountryRssItem {

    private final String link;
    private final boolean isUpcoming;
    private String event;
    private String location;
    private String dateAndTimeUpcoming;
    private String datePast;
    private String result;

    public CrossCountryRssItem(String titleAndScore, String link, String description) {
        this.isUpcoming = true;

        this.link = link;
        this.event = parseEvent(titleAndScore);
        this.dateAndTimeUpcoming = parseDateAndTimeUpcoming(description);
        this.datePast = parseDatePast(this.dateAndTimeUpcoming);
        this.result = parseResult(titleAndScore);
        this.location = parseLocation(description);
    }

    private String parseResult(String titleAndScore) {
        if ( !isUpcoming ) {
            return titleAndScore.substring(titleAndScore.indexOf(", ") + 2);
        }
        return "N/A";
    }

    private String parseLocation(String description) {
        int locationStartIndex = description.indexOf(", (") + 3;
        int locationEndIndex = description.indexOf("),", locationStartIndex);

        return description.substring(locationStartIndex, locationEndIndex);
    }

    private String parseEvent(String titleAndScore) {
        int eventEndIndex = titleAndScore.indexOf(", ");
        return titleAndScore.substring(0, eventEndIndex);
    }

    private String parseDateAndTimeUpcoming(String description) {
        /*int dateStartIndex = description.indexOf("on ") + 3;
        int dateEndIndex = description.indexOf(": at");
        String date = description.substring(dateStartIndex, dateEndIndex);

        Date parsedDate = null;
        if ( date.length() == 12) {
            try {
                parsedDate = new SimpleDateFormat("MMM dd, yyyy", Locale.US).parse(date);
            } catch (Exception e) {
                Log.d("parsedDate exception", "Couldn't parse the date!");
            }
        } else {
            try {
                parsedDate = new SimpleDateFormat("MMM d, yyyy", Locale.US).parse(date);
            } catch (Exception e) {
                Log.d("parsedDate exception", "Couldn't parse the date!");
            }
        }

        String dayOfTheWeek = new SimpleDateFormat("EE").format(parsedDate);

        int timeStartIndex = description.indexOf("), ") + 3;
        int timeEndIndex = description.indexOf("m,");
        String time = description.substring(timeStartIndex, timeEndIndex);

        String finalDateAndTime = dayOfTheWeek + ", " + date.substring(0, date.length() - 6) + " at " + time;*/

//        return finalDateAndTime;
        return "Today!";
    }

    private String parseDatePast(String parsedDate) {
//        return parsedDate.substring(5, 11);
        return "Yesterday!";
    }


    public String getResult() {
        return result;
    }

    public String getDatePast() {
        return datePast;
    }

    public String getDateAndTimeUpcoming() {
        return dateAndTimeUpcoming;
    }

    public boolean isUpcoming() {
        return isUpcoming;
    }

    public String getEvent() {
        return event;
    }

    public String getLink() {
        return link;
    }

}
