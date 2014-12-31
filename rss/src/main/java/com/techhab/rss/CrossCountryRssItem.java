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

        this.link = link;
        this.dateAndTimeUpcoming = parseDateAndTimeUpcoming(description);
        this.isUpcoming = parseIsUpcoming(description);
        this.event = parseEvent(titleAndScore);
        this.location = parseLocation(description);
        this.datePast = parseDatePast(this.dateAndTimeUpcoming);
        this.result = parseResult(titleAndScore);
    }

    private boolean parseIsUpcoming(String description) {
        int timeEndIndex = description.indexOf(dateAndTimeUpcoming) + dateAndTimeUpcoming.length();

        return !(description.length() > timeEndIndex);
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
        int timeStartIndex = description.indexOf("), ") + 3;
        int timeEndIndex = description.indexOf(", ", timeStartIndex);

        String time = description.substring(timeStartIndex, timeEndIndex);

        int dateStartIndex = description.indexOf("on ") + 3;
        int dateEndIndex = description.indexOf(": ", dateStartIndex);

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

        String finalDateAndTime = dayOfTheWeek + ", " +
                date.substring(0, date.length() - 6) + " at " + time;

        return finalDateAndTime;
    }

    private String parseDatePast(String parsedDate) {
        return parsedDate.substring(5, 11);
    }

    public String getOpponentOrEvent() {
        return event;
    }

    public String getOpponentOrEventWithPrefix() {
        return event;
    }

    public boolean isAtHome() {
        return true;
    }

    public String getLocation() {
        return location;
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

    public String getLink() {
        return link;
    }

    public String getScoreAsString(boolean kzooScore) {
        return "N/A";
    }

    public boolean isInProgress() {
        return false;
    }

    public String getInProgressTimeRemaining() {
        return "N/A";
    }

}
