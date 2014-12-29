package com.techhab.collegeapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Generic sports fragment that holds functions used by most or all sports fragments.
 *
 * Created by Griffin on 12/29/2014.
 */
public class GenericSportsFragment extends Fragment {

    public GenericSportsFragment() {

    }

    public void addViewToRow(String text, TableRow tableRow, float rowWeight) {
        TextView newTextView = new TextView(getActivity());
        newTextView.setText(text);
        newTextView.setTextColor(getResources().getColor(
                R.color.primary_text_default_material_light));
        newTextView.setTextSize(16);
        newTextView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT, rowWeight));
        tableRow.addView(newTextView);
    }

    public void setButtonOnClickListener(View button) {

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (button.getText().equals("VIEW MORE")) {
                    // Sometimes the height to add variable doesn't get set, so check to make
                    // sure it has a value.
                    if (upcomingGamesHeightToAdd == 0) {
                        initializeUpcomingGamesCount();
                    }
                    HeightAnimation animation = new HeightAnimation(upcomingGamesListView,
                            upcomingGamesHeightToAdd, true);
                    animation.setDuration(300);
                    upcomingGamesCardView.startAnimation(animation);
                    if (upcomingGamesCount == upcomingGamesList.size()) {
                        upcomingGamesViewMoreButton.setText(R.string.collapse_list);
                    }
                    upcomingGamesHeightAdded += upcomingGamesHeightToAdd;
                    updateUpcomingGamesCount();
                } else {
                    HeightAnimation animation = new HeightAnimation(upcomingGamesListView,
                            upcomingGamesHeightAdded, false);
                    animation.setDuration(300);
                    upcomingGamesCardView.startAnimation(animation);
                    upcomingGamesHeightAdded = 0;
                    initializeUpcomingGamesCount();
                    upcomingGamesViewMoreButton.setText(R.string.view_more);
                }*/
                Toast.makeText(getActivity(), "Clicked!", Toast.LENGTH_LONG).show();
                // TODO: fix auto-scrolling
            }
        });
    }



}
