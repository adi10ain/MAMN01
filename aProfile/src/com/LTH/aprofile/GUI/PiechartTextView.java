package com.LTH.aprofile.GUI;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.TextView;

import com.LTH.aprofile.R;

public class PiechartTextView extends TextView {
	private String text;

	private Animation fadeOut;
	private Animation fadeIn;

	// size(DP) in diameter
	public PiechartTextView(Context context, int sizeDP) {
		super(context);
		final float scale = getResources().getDisplayMetrics().density;
		sizeDP = (int) (sizeDP * scale);

		// align in center
		this.setLayoutParams(new LayoutParams(sizeDP, sizeDP));
		setGravity(Gravity.CENTER);

		

		fadeOut = new AlphaAnimation(1.0f, 0.3f);
		fadeOut.setDuration(300);

		fadeIn = new AlphaAnimation(0.3f, 1.0f);
		fadeIn.setDuration(1000);
		

		fadeOut.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationEnd(Animation animation) {
				setText(text);
				startAnimation(fadeIn);

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}
		});

		setSelection(0); // show text of first item (0 = Approve)
	}

	// itemNo is the n:th slice counting from top (0) clockwise
	public void setSelection(int itemToSelect) {

		switch (itemToSelect) {
		case 0:
			text = "APPROVE";
			break;
		case 1:
			text = "TIMER";
			break;
		case 2:
			text = "DECLINE";
			break;
		}
		this.startAnimation(fadeOut);

		// animation

	}

}