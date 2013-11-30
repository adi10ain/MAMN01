package com.LTH.aprofile.GUI;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.LTH.aprofile.R;

public class PiechartOverlayView extends View {
	protected Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

	private DynamicAnimation rotate;
	RectF rectf;
	private int prevSelection;
	protected int size;
	protected int amountSlices;
	protected int selectionOffset;

	// size(DP) in diameter
	public PiechartOverlayView(Context context, int amountSlices, int sizeDP) {

		super(context);

		// Make this view size independent of screen resolution
		final float scale = getResources().getDisplayMetrics().density;
		size = (int) (sizeDP * scale);

		rectf = new RectF(0, 0, size, size);
		this.setLayoutParams(new LayoutParams(size, size));
		size = size / 2;
		this.amountSlices = amountSlices;
		selectionOffset = 270;
		prevSelection = 0;
		


	}

	@Override
	protected void onDraw(Canvas canvas) {

		// draw selector
		int color = R.color.METRO_DARK_BROWN;
		paint.setColor(color);
		canvas.drawArc(rectf, selectionOffset, 360 / amountSlices, true, paint);

		// draw white circle in center
		color = getContext().getResources().getColor(
				R.color.METRO_BACKGROUND_BROWN);
		paint.setColor(color);
		canvas.drawCircle(size, size, size * 0.6f, paint);


	}

	// itemNo is the n:th slice counting from top clockwise. should be a value
	// between 0 and amountSlices
	public void createAnimation(Canvas canvas) {
		rotate = new DynamicAnimation(0, 0, size, size);
		rotate.setFillAfter(true);
		rotate.setDuration(2000);
		rotate.setRepeatCount(Animation.INFINITE);

		startAnimation(rotate);
	}

	// itemNo is the n:th slice counting from top clockwise. should be a value
	// between 0 and amountSlices
	public void setSelection(int itemToSelect) {

		int targetAngle = itemToSelect * 360 / amountSlices;

		prevSelection = itemToSelect;
		rotate.setmToDegrees(targetAngle);
		rotate.cancel();
		rotate.start();
	}
}

class DynamicAnimation extends Animation {
	private float mFromDegrees;
	private float mToDegrees;

	private int mPivotXType = ABSOLUTE;
	private int mPivotYType = ABSOLUTE;
	private float mPivotXValue = 0.0f;
	private float mPivotYValue = 0.0f;

	private float mPivotX;
	private float mPivotY;

	public DynamicAnimation(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public DynamicAnimation(float fromDegrees, float toDegrees, float pivotX,
			float pivotY) {
		mFromDegrees = fromDegrees;
		mToDegrees = toDegrees;

		mPivotXType = ABSOLUTE;
		mPivotYType = ABSOLUTE;
		mPivotXValue = pivotX;
		mPivotYValue = pivotY;
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		float degrees = mFromDegrees
				+ ((mToDegrees - mFromDegrees) * interpolatedTime);

		if (mPivotX == 0.0f && mPivotY == 0.0f) {
			t.getMatrix().setRotate(degrees);
		} else {
			t.getMatrix().setRotate(degrees, mPivotX, mPivotY);
		}

		mFromDegrees = degrees;
	}

	@Override
	public void initialize(int width, int height, int parentWidth,
			int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);
		mPivotX = resolveSize(mPivotXType, mPivotXValue, width, parentWidth);
		mPivotY = resolveSize(mPivotYType, mPivotYValue, height, parentHeight);
	}

	public synchronized void setmToDegrees(float mToDegrees) {
		this.mToDegrees = mToDegrees;
	}

}
