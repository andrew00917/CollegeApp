package com.techhab.collegeapp;

/**
 * Created by Griffin on 12/26/2014.
 */
/**
 * Provides common API for observable and scrollable widgets.
 */
public interface Scrollable {
    /**
     * Sets a callback listener.
     *
     * @param listener listener to set
     */
    void setScrollViewCallbacks(ObservableScrollViewCallbacks listener);

    /**
     * Scrolls vertically to the absolute Y.
     * Implemented classes are expected to scroll to the exact Y pixels from the top,
     * but it depends on the type of the widget.
     *
     * @param y vertical position to scroll to
     */
    void scrollVerticallyTo(int y);

    /**
     * Returns the current Y of the scrollable view.
     *
     * @return current Y pixel
     */
    int getCurrentScrollY();
}
