package com.techhab.rss;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jhchoe on 12/10/14.
 */
public class EventsParser {

    // We don't use namespaces
    private final String ns = null;

    public List<EventsRssItem> parse(InputStream inputStream) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    private List<EventsRssItem> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "rss");
        String title = null;
        String description = null;
        String link = null;
        List<EventsRssItem> items = new ArrayList<EventsRssItem>();
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
//            Log.d("readFeed name", "name: " + name);
            if (name.equals("channel")) {
                continue;
            }
            if ( ! name.equals("item")) {
                skip(parser);
            }
            else {
                String n = "";
                while ( ! n.equals("item") && parser.next() != XmlPullParser.END_TAG) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    n = parser.getName();

                    if (n.equals("title")) {
                        title = readTitle(parser);
                    } else if (n.equals("description")) {
                        description = readDescription(parser);
                    } else if (n.equals("link")) {
                        link = readLink(parser);
                    }
                }
            }
            if (title != null && description != null && link != null) {
                String[] dateAndEvent = title.split(" - ");
                String[] placeAndTime = description.split(", ");
                String event = dateAndEvent.length == 3 ? dateAndEvent[2] + " - " + dateAndEvent[1] : dateAndEvent[1];
                if (dateAndEvent[1].contains("amp;")) {
                    event = dateAndEvent[1].replace("amp;", "");
                }
                EventsRssItem item = new EventsRssItem(dateAndEvent[0], event
                        , placeAndTime[0], placeAndTime[placeAndTime.length - 1], link);
                items.add(item);
                title = null;
                description = null;
                link = null;
            }
        }
        return items;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    private String readLink(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "link");
        String link = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "link");
        return link;
    }

    private String readDescription(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "description");
        String description = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "description");
        return description;
    }

    private String readTitle(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "title");
        return title;
    }

    // For the tags title and link, extract their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
}
