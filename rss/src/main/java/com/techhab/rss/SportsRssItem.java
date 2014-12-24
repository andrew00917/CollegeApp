package com.techhab.rss;

import android.util.Log;

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
    private String dateAndTimePretty;

    public SportsRssItem(String dateAndTime, String titleAndScore, String link) {
        if (titleAndScore.contains("Final")) {
            this.isUpcoming = false;
        } else {
            this.isUpcoming = true;
        }


        this.dateAndTime = dateAndTime;
        this.titleAndScore = titleAndScore;
        this.link = link;
        this.opponent = parseOpponent(titleAndScore);
        this.dateAndTimePretty = parseDateAndTime(dateAndTime);
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

    private String parseDateAndTime(String dateAndTime) {
        String date = dateAndTime.substring(0, 11);
        Log.d("parseDateAndTime", "date = " + date);
        String GMTHour = dateAndTime.substring(17, 19);
        Log.d("parseDateAndTime", "time = " + GMTHour);
        int GMTHourInt = Integer.parseInt(GMTHour);
        int ESTHourInt = GMTHourInt - 5;
        Log.d("parseDateAndTime", "ESTHourInt = " + ESTHourInt);
        if ( ESTHourInt <= 0 ) {
            ESTHourInt = ESTHourInt + 24;
        }
        if (ESTHourInt > 12 ) {
            int ESTTwelveHourInt = (ESTHourInt - 12);
            Log.d("parseDateAndTime", "ESTTwelveHourInt = " + ESTTwelveHourInt);
            return Integer.toString(ESTTwelveHourInt);
        } else {
            return Integer.toString(ESTHourInt);
        }
    }

    public boolean getIsUpcoming() {
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
