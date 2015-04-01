package com.techhab.collegeapp;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.widget.Toast;

import java.util.Map;

/**
 * Created by akhavantafti on 3/20/2015.
 */
public class SearchableCreator {

    public static void makeSearchable(final Activity activity, Menu menu) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            SearchManager searchService = (SearchManager) activity.getSystemService(Context.SEARCH_SERVICE);
            // SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
            SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
            searchView.setSearchableInfo(searchService.getSearchableInfo(activity.getComponentName()));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Map<String, Class> mapSearchableActivities = Resources.getMapSearchableActivities();
                    if (mapSearchableActivities.containsKey(query)) {
                        Class activityClass = mapSearchableActivities.get(query);
                        Intent i = new Intent(activity, activityClass);
                        activity.startActivity(i);
                        activity.finish();
                    } else {
                        Toast.makeText(activity, "No Activity found", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    return true;
                }
            });
        }
    }
}
