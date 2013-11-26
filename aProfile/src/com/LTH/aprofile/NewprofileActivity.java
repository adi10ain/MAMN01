package com.LTH.aprofile;

import java.util.ArrayList;
import java.util.LinkedList;

import com.LTH.aprofile.Classes.GestureActivity;
import com.LTH.aprofile.Classes.GestureSelector;
import com.LTH.aprofile.Classes.Profile;
import com.LTH.aprofile.Preferences.Preference;

import android.R.interpolator;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NewprofileActivity extends GestureActivity {
	private Profile targetProfile;

	private TextView TV_profileName;

	PieChartOverlay chartSelectionOverlay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addListenForGesture(GESTURE_DOWN);
		addListenForGesture(GESTURE_RIGHT);
		addListenForGesture(GESTURE_LEFT);
		setGestureUpdateInterval(300);
		setRepeatSameGestures(false);
		gestureSensor.initiate();

		setContentView(R.layout.activity_newprofile);

		targetProfile = MainActivity.targetProfile;

		TV_profileName = (TextView) findViewById(R.id.ProfileName);
		TV_profileName.setText("" + targetProfile);

		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.desiredPreferences);

		// first row of buttons
		ArrayList<View> rowButtons1 = new ArrayList<View>();
		rowButtons1.add(findViewById(R.id.declineProfile));
		rowButtons1.add(findViewById(R.id.acceptProfile));

		PieChartView pieMenu = new PieChartView(this, 3, 400);
		RelativeLayout pieChartLayout = (RelativeLayout) findViewById(R.id.pieChart);
		pieChartLayout.addView(pieMenu);

		chartSelectionOverlay = new PieChartOverlay(this, 3, 400);
		pieChartLayout.addView(chartSelectionOverlay);
		chartSelectionOverlay.createAnimation(null);


		// preference buttons, temporary disabled
		// linearLayout.addView(targetProfile.genPrefButtons(this));
		// LinearLayout preferenceButtons = (LinearLayout) linearLayout
		// .getChildAt(0);
		//
		// gestSelect.addRow(rowButtons1);
		// gestSelect.addChildrenToRow(preferenceButtons);

	}

	// When pressed accept button
	public void btnApprove(View view) {

		LinkedList<Integer> pref = new LinkedList<Integer>();

		// Find out what preferences we want to affect
		for (Preference p : targetProfile.getPref().values()) {
			// temporary disabled
			// ImageView btn = (ImageView) findViewById(p.getType());
			// if (btn.isSelected())
			// pref.add(p.getType());
			//
			pref.add(p.getType());
		}

		// return to calling activity and send chosen preferences
		Intent result = new Intent(this, MainActivity.class);
		result.putExtra("PREFERENCES", pref);
		setResult(MainActivity.APPROVE_NEW_PROFILE, result);

		finish();
	

	}

	// when pressed decline button
	public void btnDecline(View view) {
		 setResult(MainActivity.DECLINE_NEW_PROFILE);
		 finish();



	}

	@Override
	public void onGesture(int gesture) {
		switch (gesture) {

		case GESTURE_RIGHT:
			chartSelectionOverlay.setSelection(0);
			break;
		case GESTURE_DOWN:
			chartSelectionOverlay.setSelection(1);
			break;
		case GESTURE_LEFT:
			chartSelectionOverlay.setSelection(2);

		}

	}

}

class PieChartView extends View {
	protected Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	protected int size;
	protected int amountSlices;
	protected int selectionOffset;

	// size in diameter
	public PieChartView(Context context, int amountSlices, int size) {
		super(context);
		this.size = (int) (size * 0.5);
		this.amountSlices = amountSlices;
		selectionOffset = 270;

		this.setLayoutParams(new LayoutParams(size, size));

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		paint.setColor(Color.GRAY);

		canvas.drawCircle(size, size, size, paint);

		paint.setColor(Color.WHITE);
		// draw white lines, slice separators
		for (int i = 0; i < amountSlices; i++) {
			float angle = selectionOffset + i * (360 / amountSlices);
			angle = (float) ((Math.PI / 180) * angle);
			canvas.drawLine(size, size, size * (float) Math.cos(angle) + size,
					size * (float) Math.sin(angle) + size, paint);
		}

	}

}

class PieChartOverlay extends PieChartView {

	private DynamicAnimation rotate;
	RectF rectf;
	private int prevSelection;

	// size in diameter
	public PieChartOverlay(Context context, int amountSlices, int size) {

		super(context, amountSlices, size);
		rectf = new RectF(0, 0, size, size);
		prevSelection = 0;

	}

	@Override
	protected void onDraw(Canvas canvas) {

		// draw selector
		int color = R.color.METRO_DARK_BROWN;
		paint.setColor(color);
		canvas.drawArc(rectf, selectionOffset, 360 / amountSlices, true, paint);

		// draw white circle in center
		paint.setColor(Color.WHITE);
		canvas.drawCircle(size, size, size * 0.6f, paint);

	}

	// itemNo is the n:th slice counting from top clockwise. should be a value
	// between 0 and amountSlices
	void createAnimation(Canvas canvas) {
		rotate = new DynamicAnimation(0, 0, size, size);
		rotate.setFillAfter(true);
		rotate.setDuration(2000);
		rotate.setRepeatCount(1000);

		startAnimation(rotate);
	}

	// itemNo is the n:th slice counting from top clockwise. should be a value
	// between 0 and amountSlices
	public void setSelection(int itemToSelect) {
		
		
		int targetAngle = itemToSelect* 360 / amountSlices;
		
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
