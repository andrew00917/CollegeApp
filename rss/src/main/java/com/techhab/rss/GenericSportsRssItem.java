package com.techhab.rss;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Griffin on 12/29/2014.
 */
public abstract class GenericSportsRssItem {

    public abstract boolean isUpcoming();

    public abstract String getDateAndTimeUpcoming();

    public abstract String getOpponentOrEvent();

    public abstract String getOpponentOrEventWithPrefix();

    public abstract boolean isAtHome();

    public abstract String getDatePast();

    public abstract String getResult();

    public abstract String getLocation();

    public String getEventDate(String description) {
        Pattern datePattern = Pattern.compile("(\\w{3} \\d{1,2}, \\d{4})");
        Matcher matcher = datePattern.matcher(description);
        if ( matcher.find() ) {
            return matcher.group(1);
        } else {
            return "(No date)";
        }
    }

    public String getEventTime(String description) {
        Pattern timePattern = Pattern.compile("(\\d{1,2}:\\d\\d [AP]M)",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = timePattern.matcher(description);
        if ( matcher.find() ) {
            return matcher.group();
        } else {
            return null;
        }
    }

    public String getFullDateWithTime(String description) {

        String time = getEventTime(description);
        String date = getEventDate(description);
        Date parsedDate = null;

        try {
            parsedDate = new SimpleDateFormat("MMM d, yyyy", Locale.US).parse(date);
        } catch (Exception e) {
        }

        String dayOfTheWeek = new SimpleDateFormat("EE").format(parsedDate);

        if ( time == null ) {
            return dayOfTheWeek + ", " + date.substring(0, date.length() - 6) + " (Time TBA)";
        } else {
            return dayOfTheWeek + ", " + date.substring(0, date.length() - 6) + " at " + time;
        }
    }

    public boolean determineIfUpcoming(String description) {
        String time = getEventTime(description);
        String date = getEventDate(description);
        Date parsedDate = null;

        if (time != null) {
            if (time.length() == 7) {
                time = "0" + time;
            }
            String dateAndTime = date + " " + time;

            try {
                parsedDate = new SimpleDateFormat("MMM d, yyyy hh:mm aa", Locale.US)
                        .parse(dateAndTime);
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

        Date currentDate = new Date();

        return (parsedDate.compareTo(currentDate) > 0) || (parsedDate.compareTo(currentDate) == 0);

    }

    public String parseStandardResult(String description, boolean isAtHome, boolean isUpcoming) {
        if ( !isUpcoming ) {
            int scoreStartIndex = description.indexOf("Final, ") + 7;
            String score = description.substring(scoreStartIndex);
            String[] scores = score.split("-");
            boolean kWin;
            // Second score is bigger than the first, which corresponds to a win if at home and
            // a loss if on the road
            if (Integer.parseInt(scores[0]) < Integer.parseInt(scores[1])) {
                // If won, then it must be at home
                kWin = isAtHome;
            } else {
                kWin = !isAtHome;
            }
            if (kWin) {
                return "W, " + score;
            } else {
                return "L, " + score;
            }
        }
        return "N/A";
    }
}
