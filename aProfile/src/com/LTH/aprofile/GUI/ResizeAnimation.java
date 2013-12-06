package com.LTH.aprofile.GUI;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.view.animation.Animation.AnimationListener;
import android.widget.LinearLayout;
import android.widget.TextView;

//Animations the actual height of a view
public class ResizeAnimation extends Animation {
	TextView view;
	int startH;
	int endH;
	int diff;

	public ResizeAnimation(TextView v, int oldHeight, int newHeight) {
		view = v;
		startH = oldHeight;
		endH = newHeight;
		diff = endH - startH;
		setFillEnabled(false);
		setFillAfter(false);
		
		this.setInterpolator(new DecelerateInterpolator());
		this.setDuration(1000);
		


	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		int newHeight = (int) (startH + (endH - startH) * interpolatedTime);
		view.setHeight(newHeight);
		view.requestLayout();
	}

	@Override
	public void initialize(int width, int height, int parentWidth,
			int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);
	}

	@Override
	public boolean willChangeBounds() {
		return false;
	}

}