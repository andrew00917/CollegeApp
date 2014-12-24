package com.techhab.rss;

/**
 * Created by Griffin on 12/19/2014.
 */
public class SportsRssItem {

    private final String dateAndTime;
    private final String titleAndScore;
    private final String link;

    public SportsRssItem(String dateAndTime, String titleAndScore, String link) {
        this.dateAndTime = dateAndTime;
        this.titleAndScore = titleAndScore;
        this.link = link;
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
