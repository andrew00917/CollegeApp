package com.techhab.collegeapp;

/**
 * Created by Griffin on 12/26/2014.
 */
public interface ObservableScrollViewCallbacks {
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging);

    public void onDownMotionEvent();

    public void onUpOrCancelMotionEvent(ScrollState scrollState);
}
