package com.techhab.collegeapp;

import android.app.Activity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by akhavantafti on 3/19/2015.
 */
public class Resources {

    //TODO: need to find better way to store acitivties map
    private  Map<String, Class> mapSearchableActivities = new HashMap<String, Class>();
    private static Resources instance;

    private  Resources(){
        mapSearchableActivities.put("help", HelpAndFeedBackActivity.class);
        mapSearchableActivities.put("profile", ProfileActivity.class);
        mapSearchableActivities.put("academic", AcademicActivity.class);
        mapSearchableActivities.put("setting", SettingsActivity.class);
        mapSearchableActivities.put("pinsetup", PINSetupActivity.class);
        mapSearchableActivities.put("maps", MapsActivity.class);
        mapSearchableActivities.put("campus", CampusActivity.class);
        mapSearchableActivities.put("home", HomeActivity.class);
        mapSearchableActivities.put("about", AboutActivity.class);
        mapSearchableActivities.put("athletic", AthleticActivity.class);
        mapSearchableActivities.put("feedback", FeedBackActivity.class);
        mapSearchableActivities.put("dinning", DiningActivity.class);
        mapSearchableActivities.put("event", EventsActivity.class);

    }

    private static Resources getInstance() {
        if(instance == null){
            instance = new Resources();
        }
        return instance;
    }

    public static   Map<String, Class> getMapSearchableActivities() {
        return getInstance().mapSearchableActivities;
    }
}
