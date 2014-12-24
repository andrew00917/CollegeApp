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
 * Created by Griffin on 12/19/2014.
 */
public class SportsParser {
    // We don't use namespaces
    private final String ns = null;

    public List<SportsRssItem> parse(InputStream inputStream) throws XmlPullParserException,
            IOException {
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

    private List<SportsRssItem> readFeed(XmlPullParser parser) throws XmlPullParserException,
            IOException {
        parser.require(XmlPullParser.START_TAG, null, "rss");
        String titleAndScore = null;
        String pubDate = null;
        String link = null;
        List<SportsRssItem> items = new ArrayList<SportsRssItem>();
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
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
                        titleAndScore = readTitle(parser);
                    } else if (n.equals("pubDate")) {
                        pubDate = readPubDate(parser);
                    } else if (n.equals("link")) {
                        link = readLink(parser);
                    } else if (n.equals("description")) {
                        parser.next();
                        parser.nextTag();
                    } else if (n.equals("category")) {
                        parser.next();
                        parser.nextTag();
                    }
                }
            }
            if (titleAndScore != null && pubDate != null && link != null) {
                SportsRssItem item = new SportsRssItem(pubDate, titleAndScore, link);
                items.add(item);
                pubDate = null;
                titleAndScore = null;
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

    private String readLink(XmlPullParser parser) throws XmlPullParserException,
            IOException {
        parser.require(XmlPullParser.START_TAG, ns, "link");
        String link = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "link");
        return link;
    }

    private String readDescription(XmlPullParser parser) throws XmlPullParserException,
            IOException {
        parser.require(XmlPullParser.START_TAG, ns, "description");
        String description = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "description");
        return description;
    }

    private String readTitle(XmlPullParser parser) throws XmlPullParserException,
            IOException {
        parser.require(XmlPullParser.START_TAG, ns, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "title");
        return title;
    }

    private String readPubDate(XmlPullParser parser) throws XmlPullParserException,
            IOException {
        parser.require(XmlPullParser.START_TAG, ns, "pubDate");
        String pubDate = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "pubDate");
        return pubDate;
    }

    // For the tags title and link, extract their text values.
    private String readText(XmlPullParser parser) throws IOException,
            XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
}
