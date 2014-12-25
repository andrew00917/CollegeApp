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
public class SportsRssItem {

    private final String dateAndTime;
    private final String titleAndScore;
    private final String link;
    private final boolean isUpcoming;
    private String opponent;
    private String dateAndTimeUpcoming;

    public SportsRssItem(String dateAndTime, String titleAndScore, String link, String description) {
        if (titleAndScore.contains("Final")) {
            this.isUpcoming = false;
        } else {
            this.isUpcoming = true;
        }

        this.dateAndTime = dateAndTime;
        this.titleAndScore = titleAndScore;
        this.link = link;
        this.opponent = parseOpponent(titleAndScore);
        this.dateAndTimeUpcoming = parseDateAndTime(description);
    }

    private String parseOpponent(String titleAndScore) {
        Pattern numbersPattern = Pattern.compile("(\\d+)");
        int opponentStartIndex;
        int opponentEndIndex;
        if ( !isUpcoming ) {
            if (titleAndScore.startsWith("Kalamazoo")) {
                opponentStartIndex = titleAndScore.indexOf(", ") + 2;
                // Regex matcher to find the beginning of the opponent's score
                Matcher matcher = numbersPattern.matcher(titleAndScore);
                if ( matcher.find(opponentStartIndex) ) {
                    int startOfOpponentScore = matcher.start();
                    return titleAndScore.substring(opponentStartIndex, (startOfOpponentScore - 1));
                }
            } else {
                Matcher matcher = numbersPattern.matcher(titleAndScore);
                if (matcher.find()) {
                    return titleAndScore.substring(0, (matcher.start() - 1));
                }
            }
        } else {
            if ( titleAndScore.startsWith("Kalamazoo") ) {
                opponentStartIndex = titleAndScore.indexOf("vs. ") + 4;
                return titleAndScore.substring(opponentStartIndex);
            } else {
                opponentEndIndex = titleAndScore.indexOf(" vs.");
                return titleAndScore.substring(0, opponentEndIndex);
            }
        }

        return null;
    }

    private String parseDateAndTime(String description) {
        int dateEndIndex = description.indexOf(" at");
        String date = description.substring(20, dateEndIndex);

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

        int timeStartIndex = description.indexOf("at ") + 3;
        int timeEndIndex = description.indexOf(": ");
        String time = description.substring(timeStartIndex, timeEndIndex);

        String finalDateAndTime = dayOfTheWeek + ", " + date.substring(0, date.length() - 6) + " at " + time;

        return finalDateAndTime;
    }

    public String getDateAndTimeUpcoming() {
        return dateAndTimeUpcoming;
    }

    public boolean isUpcoming() {
        return isUpcoming;
    }

    public String getOpponent() {
        return opponent;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public String getTitleAndScore() {
        return titleAndScore;
    }

    public String getLink() {
        return link;
    }

}
