package com.techhab.collegeapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.techhab.kcollegecustomviews.ProgressBar;

/**
 * Created by jhchoe on 12/29/14.
 */
public class EventsViewHolder extends RecyclerView.ViewHolder {

    public View v;
    public FrameLayout image;
    public TextView date, event, description, time;
    public ProgressBar progress;
    public View divider;
    public LinearLayout buttonSection;
    public ImageButton infoButton;
    public ImageView favoriteButton, buildingButton, calendarButton, attendButton;
    public boolean cardExpanded;
    public boolean buttonExpanded;

    public EventsViewHolder(View itemView) {
        super(itemView);
        v = itemView;

        image = (FrameLayout) v.findViewById(R.id.image);
        date = (TextView) v.findViewById(R.id.date);
        event = (TextView) v.findViewById(R.id.event);
        description = (TextView) v.findViewById(R.id.place);
        time = (TextView) v.findViewById(R.id.time);

        progress = (ProgressBar) v.findViewById(R.id.progress_bar);

        divider = v.findViewById(R.id.divider);

        buttonSection = (LinearLayout) v.findViewById(R.id.button_section);

        infoButton = (ImageButton) v.findViewById(R.id.info_button);
        favoriteButton = (ImageView) v.findViewById(R.id.favorite_button);
        buildingButton = (ImageView) v.findViewById(R.id.building_button);
        calendarButton = (ImageView) v.findViewById(R.id.calendar_button);
        attendButton = (ImageView) v.findViewById(R.id.attending_button);

        cardExpanded = false;
        buttonExpanded = false;
    }

    public boolean isCardExpanded() {
        return cardExpanded;
    }

    public boolean isButtonExpanded() {
        return buttonExpanded;
    }
}