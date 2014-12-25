package com.techhab.collegeapp;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by Griffin on 12/25/2014.
 */
public class HeightAnimation extends Animation {
    private final int targetHeight;
    private final View view;
    private final boolean grow;
    private final int height;

    public HeightAnimation(View view, int targetHeight, boolean grow) {
        this.view = view;
        this.targetHeight = targetHeight;
        this.grow = grow;
        height = view.getHeight();
        Log.d("HeightAnimation", "height: " + height);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        int newHeight;
        if (grow) {
            newHeight = (int) (height + (targetHeight * interpolatedTime));
            Log.d("HeightAnimation", "newHeight: " + newHeight);
        } else {
            newHeight = (int) (height + (targetHeight - height) * interpolatedTime);
        }
        view.getLayoutParams().height = newHeight;

        view.requestLayout();
    }

    @Override
    public void initialize(int width, int height, int parentWidth,
                           int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}
