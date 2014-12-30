package com.techhab.rss;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Griffin on 12/19/2014.
 */
public class StandardSportRssItem extends GenericSportsRssItem {

    private final String link;
    private final boolean isUpcoming;
    private String opponentOrEvent;
    private String dateAndTimeUpcoming;
    private String datePast;
    private boolean isAtHome;
    private String result;

    public StandardSportRssItem(String titleAndScore, String link, String description) {
        this.isUpcoming = determineIfUpcoming(description);
        this.link = link;
        if ( description.contains("Soccer")) {
            this.result = parseSoccerResult(description);
        } else if ( description.contains("Tennis") ) {
            this.result = "N/A";
        } else if ( description.contains("Golf") ) {
            this.result = parseGolfResult(description);
        } else {
            this.result = parseStandardResult(description, isAtHome, isUpcoming);
        }
        if ( description.contains("Golf") ) {
            this.opponentOrEvent = parseGolfOpponentOrEvent(titleAndScore);
        } else {
            this.opponentOrEvent = parseOpponentOrEvent(titleAndScore);
        }
        this.dateAndTimeUpcoming = getFullDateWithTime(description);
        this.datePast = parseDatePast(this.dateAndTimeUpcoming);
    }

    private String parseSoccerResult(String description) {
        if ( ! description.contains("Final -") ) {
            return parseStandardResult(description, isAtHome, isUpcoming);
        } else {
            if ( !isUpcoming ) {
                int scoreStartIndex = description.indexOf(", ", description.indexOf("Final")) + 2;
                String score = description.substring(scoreStartIndex);
                String[] scores = score.split("-");
                boolean kWin;
                boolean tie = false;
                // Second score is bigger than the first, which corresponds to a win if at home and
                // a loss if on the road
                if (Integer.parseInt(scores[0]) < Integer.parseInt(scores[1])) {
                    // If won, then it must be at home
                    kWin = isAtHome;
                } else if (Integer.parseInt(scores[0]) == Integer.parseInt(scores[1])) {
                    tie = true;
                    kWin = false;
                } else {
                    kWin = !isAtHome;
                }
                if (tie) {
                    return "T, " + score;
                } else {
                    if (kWin) {
                        return "W, " + score;
                    } else {
                        return "L, " + score;
                    }
                }
            }
            return "N/A";
        }
    }

    private String parseGolfResult(String description) {
        if ( ! isUpcoming ) {
            String time = getEventTime(description);
            int resultStartIndex = description.indexOf(time) + time.length() + 2;
            return description.substring(resultStartIndex);
        } else {
            return "N/A";
        }
    }

    private String parseGolfOpponentOrEvent(String titleAndScore) {
        int eventEndIndex;
        if ( titleAndScore.contains("(") ) {
            eventEndIndex = titleAndScore.indexOf(" (");
        } else {
            if ( ! isUpcoming ) {
                eventEndIndex = titleAndScore.indexOf(", ");
            } else {
                eventEndIndex = titleAndScore.length();
            }
        }
        return titleAndScore.substring(0, eventEndIndex);
    }

    private String parseOpponentOrEvent(String titleAndScore) {
        Pattern numbersPattern = Pattern.compile("(\\d+)");
        int opponentStartIndex;
        int opponentEndIndex;
        if ( !isUpcoming ) {
            if (titleAndScore.startsWith("Kalamazoo")) {
                isAtHome = false;
                opponentStartIndex = titleAndScore.indexOf(", ") + 2;
                // Regex matcher to find the beginning of the opponentOrEvent's score
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

    public String getOpponentOrEvent() {
        if (opponentOrEvent.equals("TBA")) {
            return "Opponent " + opponentOrEvent;
        }
        return opponentOrEvent;
    }

    public String getOpponentOrEventWithPrefix() {
        if ( isAtHome ) {
            return "vs. " + opponentOrEvent;
        } else {
            return "at " + opponentOrEvent;
        }
    }

    public String getLocation() {
        return null;
    }

    public String getLink() {
        return link;
    }

    public String getEvent() { return null; }

}
