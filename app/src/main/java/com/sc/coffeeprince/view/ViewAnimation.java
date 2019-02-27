package com.sc.coffeeprince.view;

import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

/**
 * Created by deskh on 2017-02-13.
 */

public class ViewAnimation extends Animation {
    int centerX, centerY, endY;

    @Override
    public void initialize(int width, int height, int parentWidth,
                           int parentHeight) {
        //System.out.println(width + ", " + height + ", " + parentWidth + ", " + parentHeight);
        super.initialize(width, height, parentWidth, parentHeight);
        setDuration(1000);
        setFillAfter(true);

        setInterpolator(new LinearInterpolator());
        centerX = width / 2;
        centerY = height / 2;
        endY = height;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        final Matrix matrix = t.getMatrix();
        matrix.setScale(interpolatedTime, interpolatedTime, 0, endY); //기준점. y의 마지막.
    }
}