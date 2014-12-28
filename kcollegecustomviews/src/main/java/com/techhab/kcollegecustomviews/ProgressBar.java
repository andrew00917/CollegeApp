package com.techhab.kcollegecustomviews;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by jhchoe on 12/27/14.
 */
public class ProgressBar extends android.widget.ProgressBar {

    public ProgressBar(Context context) {
        super(context);

        setIndeterminate(false);
        setProgressDrawable(getResources().getDrawable(R.drawable.custom_progress_bar_horizontal));
        setMinimumHeight(10);
    }

    public ProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        setIndeterminate(false);
        setProgressDrawable(getResources().getDrawable(R.drawable.custom_progress_bar_horizontal));
        setMinimumHeight(10);
    }

    public ProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setIndeterminate(false);
        setProgressDrawable(getResources().getDrawable(R.drawable.custom_progress_bar_horizontal));
        setMinimumHeight(10);
    }
}
