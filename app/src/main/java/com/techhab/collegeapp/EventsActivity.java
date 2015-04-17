package com.techhab.collegeapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewTreeObserver;

import com.techhab.kcollegecustomviews.ProgressBar;


public class EventsActivity extends BaseObservableRecyclerActivity {
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_events;
    }

    @Override
    protected String getToolbarTitle() {
        return getResources().getString(R.string.main_menu_11);
    }

    @Override
    protected String[] getTabTitles() {
        return new String[]{"Etc", "Upcoming Events", "Stress Free Zone"
                , "Tuesdays With...", "Wind Down Wednesday", "Trivia Night", "Zoo Flicks"
                , "Zoo After Dark"};
    }

    @Override
    protected Class<? extends Fragment> getFragmentClass() {
        return EventsFragment.class;
    }

    public void dismissProgressBar() {
        progressBar.setVisibility(View.GONE);
        //reupdate  current height of view Pager
        ViewTreeObserver viewTreeObserver = mViewPager.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mViewPager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    currentHeight = mViewPager.getHeight();
                }
            });
        }
    }
}
