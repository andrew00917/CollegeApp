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

    private String link;
    private boolean isUpcoming;
    private String opponentOrEvent;
    private String dateAndTimeUpcoming;
    private String datePast;
    private boolean isAtHome;
    private String result;
    private boolean isTeamSport;
    private boolean isInProgress;
    private String titleAndScore;
    private String inProgressTimeRemaining;

    public SportsRssItem(String titleAndScore, String link, String description) {
        this.isUpcoming = determineIfUpcoming(description);
        this.isTeamSport = determineIfTeamSport(description);
        this.link = link;
        this.isInProgress = isInProgress(description);
        this.titleAndScore = titleAndScore;
        if ( isInProgress ) {
            inProgressTimeRemaining = parseStandardInProgressTimeRemaining(titleAndScore);
        }

        if ( isTeamSport ) {
            this.result = parseStandardTeamResult(titleAndScore, isUpcoming);
        } else if ( description.contains("Tennis") ) {
            this.result = "N/A";
        } else if ( description.contains("Golf") ) {
            this.result = parseGolfResult(description);
        } else if ( description.contains("Swimming") ) {
            this.result = parseSwimmingResult(titleAndScore);
        }

        if ( description.contains("Golf") ) {
            this.opponentOrEvent = parseGolfOpponentOrEvent(titleAndScore);
        } else {
            this.opponentOrEvent = parseOpponentOrEvent(titleAndScore);
        }

        this.dateAndTimeUpcoming = getFullDateWithTime(description);
        this.datePast = parseDatePast(this.dateAndTimeUpcoming);
    }

    private String parseSwimmingResult(String titleAndScore) {
        if ( ! isUpcoming ) {
            // Check to see if the title contains "Unscored"
            String titleAndScoreLowerCase = titleAndScore.toLowerCase();
            if ( titleAndScoreLowerCase.contains("unscored") ) {
                return "Unscored";
            }

            Pattern resultPattern = Pattern.compile("(, [WLT])");
            Matcher m = resultPattern.matcher(titleAndScore);

            String resultString = "";
            if ( m.find() ) {
                resultString = titleAndScore.substring(m.start() + 2);
            } else {
                // Code for place finish
                Pattern placeFinishPattern = Pattern.compile("(, \\d[tsrn][htd])",
                        Pattern.CASE_INSENSITIVE);
                Matcher placeMatcher = placeFinishPattern.matcher(titleAndScore);

                if ( placeMatcher.find() ) {
                    return titleAndScore.substring(placeMatcher.start() + 2);
                }
                return "N/A";
            }

            if ( resultString.contains("W") ) {
                return "W, " + resultString.substring(1);
            } else if ( resultString.contains("L") ) {
                return "L, " + resultString.substring(1);
            } else {
                return resultString;
            }
        }
        return "N/A";
    }

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

    public int parseScore(String titleAndScore, boolean returnKzooScore) {
        Pattern firstScorePattern = Pattern.compile("(\\d+,)");
        Pattern secondScorePattern = Pattern.compile("(\\d+)");
        Matcher matcher = firstScorePattern.matcher(titleAndScore);

        if ( ! matcher.find() ) {
            return 0;
        }

        String firstScore = titleAndScore.substring(matcher.start(), matcher.end() - 1);

        String secondNumberSearch = titleAndScore.substring(
                titleAndScore.indexOf(firstScore) + firstScore.length());
        Matcher secondScoreMatcher = secondScorePattern.matcher(secondNumberSearch);

        String secondScore = "";

        if ( secondScoreMatcher.find() ) {
            secondScore = secondNumberSearch.substring(secondScoreMatcher.start(), secondScoreMatcher.end());
        }

        int kzooScore;
        int opponentScore;

        if ( titleAndScore.startsWith("Kalamazoo") ) {
            kzooScore = Integer.parseInt(firstScore);
            opponentScore = Integer.parseInt(secondScore);
        } else {
            kzooScore = Integer.parseInt(secondScore);
            opponentScore = Integer.parseInt(firstScore);
        }

        return returnKzooScore ? kzooScore : opponentScore;
    }

    public String parseStandardTeamResult(String titleAndScore, boolean isUpcoming) {
        if ( ! isUpcoming ) {

            Pattern firstScorePattern = Pattern.compile("(\\d+,)");
            Pattern secondScorePattern = Pattern.compile("(\\d+)");
            Matcher matcher = firstScorePattern.matcher(titleAndScore);

            if ( ! matcher.find() ) {
                return "N/A";
            }

            String firstScore = titleAndScore.substring(matcher.start(), matcher.end() - 1);

            String secondNumberSearch = titleAndScore.substring(
                    titleAndScore.indexOf(firstScore) + firstScore.length());

            Matcher secondScoreMatcher = secondScorePattern.matcher(secondNumberSearch);

            String secondScore = "";
            if ( secondScoreMatcher.find() ) {
                secondScore = secondNumberSearch.substring(secondScoreMatcher.start(), secondScoreMatcher.end());
            }

            int kzooScore;
            int opponentScore;

            if ( titleAndScore.startsWith("Kalamazoo") ) {
                kzooScore = Integer.parseInt(firstScore);
                opponentScore = Integer.parseInt(secondScore);
            } else {
                kzooScore = Integer.parseInt(secondScore);
                opponentScore = Integer.parseInt(firstScore);
            }

            /*int kzooScore = parseScore(titleAndScore, true);
            int opponentScore = parseScore(titleAndScore, false);*/

            if ( kzooScore > opponentScore ) {
                return "W, " + kzooScore + "-" + opponentScore;
            } else if ( kzooScore == opponentScore ) {
                return "T, " + kzooScore + "-" + opponentScore;
            } else {
                return "L, " + kzooScore + "-" + opponentScore;
            }
        }
        return "N/A";
    }

    public String parseStandardInProgressTimeRemaining(String titleAndScore) {
        Pattern timeRemaining = Pattern.compile("(\\d\\w{2} - \\d{1,2}:\\d{2})");
        Matcher matcher = timeRemaining.matcher(titleAndScore);

        if ( matcher.find() ) {
            return matcher.group(1);
        }
        return "null";
    }

    public String parseStandardResultUsingFinal(String description,
                                                boolean isAtHome, boolean isUpcoming) {
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

    private boolean determineIfTeamSport(String description) {
        description = description.toLowerCase();
        String[] teamSports = new String[] {
                "baseball",
                "softball",
                "basketball",
                "football",
                "soccer",
                "volleyball",
                "lacrosse"
        };
        for (int i = 0; i < teamSports.length; i ++) {
            if (description.contains(teamSports[i]) ) {
                return true;
            }
        }
        return false;
    }

    private boolean isInProgress(String description) {
        return false;
    }

    private String parseSoccerResult(String description) {
        if ( ! description.contains("Final -") ) {
            return parseStandardResultUsingFinal(description, isAtHome, isUpcoming);
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
        if ( ! isUpcoming ) {
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

    public boolean isInProgress() {
        return isInProgress;
    }

    public int getScore(boolean kzooScore) {
        if (kzooScore) {
            return parseScore(titleAndScore, true);
        } else {
            return parseScore(titleAndScore, false);
        }
    }

    public String getScoreAsString(boolean kzooScore) {
        return Integer.toString(getScore(kzooScore));
    }

    public String getInProgressTimeRemaining() {
        return inProgressTimeRemaining;
    }

    public boolean isTeamSport() {
        return isTeamSport;
    }


}
