package com.techhab.rss;

public class EventsRssItem {

    private final String date;
    private final String event;
    private final String place;
    private final String time;
    private final String link;

    public EventsRssItem(String date, String event, String place
            , String time, String link) {
        this.date = date;
        this.event = event;
        this.place = place;
        this.time = time;
        this.link = link;
    }

    public String getDate() {
        return date;
    }

    public String getEvent() {
        return event;
    }

    public String getPlace() {
        return place;
    }

    public String getTime() {
        return time;
    }

    public String getLink() {
        return link;
    }
}