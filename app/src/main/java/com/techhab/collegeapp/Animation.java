package com.techhab.collegeapp;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Transformation;

/**
 * Created by jhchoe on 08/01/15.
 */
public class Animation extends android.view.animation.Animation {

    private View view;
    private int width, height;
    private boolean grow;

    public Animation(View view, int width, int height, boolean grow) {
        this.view = view;
        this.width = width;
        this.height = height;
        this.grow = grow;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
//        int newHeight;
//        if (grow) {
//            newHeight = (int) (height + (heightToAddOrSubtract * interpolatedTime));
//        } else {
//            newHeight = (int) (height + (-heightToAddOrSubtract * interpolatedTime));
//        }
        if (grow) {
            view.getLayoutParams().width = (int) (view.getLayoutParams().width
                    + (width - view.getLayoutParams().width) * interpolatedTime);
            view.getLayoutParams().height = (int) (view.getLayoutParams().height
                    + (height - view.getLayoutParams().height) * interpolatedTime);
        } else {
            view.getLayoutParams().width = (int) (view.getLayoutParams().width
                    - (view.getLayoutParams().width - width) * interpolatedTime);
            view.getLayoutParams().height = (int) (view.getLayoutParams().height
                    - (view.getLayoutParams().height - height) * interpolatedTime);
        }

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
