package com.techhab.kcollegecustomviews;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by jhchoe on 12/27/14.
 */
public class ProgressBar extends android.widget.ProgressBar {

    public ProgressBar(Context context) {
        super(context);

        setIndeterminate(true);
        setIndeterminateDrawable(getResources().getDrawable(R.drawable.custom_progress_bar));
//        setMinimumHeight(10);
//        setMax(100);
//        setProgress(0);
    }

    public ProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        setIndeterminate(true);
        setIndeterminateDrawable(getResources().getDrawable(R.drawable.custom_progress_bar));
//        setMinimumHeight(10);
//        setMax(100);
//        setProgress(0);
    }

    public ProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setIndeterminate(true);
        setIndeterminateDrawable(getResources().getDrawable(R.drawable.custom_progress_bar));
//        setMinimumHeight(10);
//        setMax(100);
//        setProgress(0);
    }
}
