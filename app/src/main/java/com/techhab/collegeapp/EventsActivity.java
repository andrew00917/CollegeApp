package com.techhab.collegeapp;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
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
