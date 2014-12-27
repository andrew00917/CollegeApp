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
public class BasketballRssItem {

    private final String link;
    private final boolean isUpcoming;
    private String opponent;
    private String dateAndTimeUpcoming;
    private String datePast;
    private boolean isAtHome;
    private String result;

    public BasketballRssItem(String titleAndScore, String link, String description) {
        if (titleAndScore.contains("Final")) {
            this.isUpcoming = false;
        } else {
            this.isUpcoming = true;
        }

        this.link = link;
        this.opponent = parseOpponent(titleAndScore);
        this.dateAndTimeUpcoming = parseDateAndTimeUpcoming(description);
        this.datePast = parseDatePast(this.dateAndTimeUpcoming);
        this.result = parseResult(description, isAtHome);
    }

    private String parseResult(String description, boolean isAtHome) {
        if ( !isUpcoming ) {
            String score = description.substring(description.length() - 5);
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

    private String parseOpponent(String titleAndScore) {
        Pattern numbersPattern = Pattern.compile("(\\d+)");
        int opponentStartIndex;
        int opponentEndIndex;
        if ( !isUpcoming ) {
            if (titleAndScore.startsWith("Kalamazoo")) {
                isAtHome = false;
                opponentStartIndex = titleAndScore.indexOf(", ") + 2;
                // Regex matcher to find the beginning of the opponent's score
                Matcher matcher = numbersPattern.matcher(titleAndScore);
                if ( matcher.find(opponentStartIndex) ) {
                    int startOfOpponentScore = matcher.start();
                    return titleAndScore.substring(opponentStartIndex, (startOfOpponentScore - 1));
                }
            } else {
                isAtHome = true;
                Matcher matcher = numbersPattern.matcher(titleAndScore);
                if (matcher.find()) {
                    return titleAndScore.substring(0, (matcher.start() - 1));
                }
            }
        } else {
            if ( titleAndScore.startsWith("Kalamazoo") ) {
                isAtHome = false;
                opponentStartIndex = titleAndScore.indexOf("vs. ") + 4;
                return titleAndScore.substring(opponentStartIndex);
            } else {
                isAtHome = true;
                opponentEndIndex = titleAndScore.indexOf(" vs.");
                return titleAndScore.substring(0, opponentEndIndex);
            }
        }

        return null;
    }

    private String parseDateAndTimeUpcoming(String description) {
        int dateStartIndex = description.indexOf("on ") + 3;
        int dateEndIndex = description.indexOf(" at");
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

        int timeStartIndex = description.indexOf("at ") + 3;
        int timeEndIndex = description.indexOf(": ");
        String time = description.substring(timeStartIndex, timeEndIndex);

        String finalDateAndTime = dayOfTheWeek + ", " + date.substring(0, date.length() - 6) + " at " + time;

        return finalDateAndTime;
    }

    private String parseDatePast(String parsedDate) {
        return parsedDate.substring(5, 11);
    }

    public String getResult() {
        return result;
    }

    public boolean isAtHome() {
        return isAtHome;
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

    public String getOpponent() {
        return opponent;
    }

    public String getLink() {
        return link;
    }

}
