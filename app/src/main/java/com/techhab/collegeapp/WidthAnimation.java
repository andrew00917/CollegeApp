package com.techhab.collegeapp;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by jhchoe on 12/28/14.
 */
public class WidthAnimation extends Animation {
    private final int widthToAddOrSubtract;
    private final View view;
    private final boolean grow;
    private final int width;

    public WidthAnimation(View view, int widthToAddOrSubtract, boolean grow) {
        this.view = view;
        this.widthToAddOrSubtract = widthToAddOrSubtract;
        this.grow = grow;
        this.width = view.getWidth();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        int newWidth;
        if (grow) {
            newWidth = (int) (width + (widthToAddOrSubtract * interpolatedTime));
        } else {
            newWidth = (int) (width + (-widthToAddOrSubtract * interpolatedTime));
        }
        view.getLayoutParams().width = newWidth;

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