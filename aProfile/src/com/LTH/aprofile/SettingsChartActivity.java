package com.LTH.aprofile;

import java.util.Collection;


import com.LTH.aprofile.Classes.Profile;
import com.LTH.aprofile.Classes.Preferences.BrightnessPreference;
import com.LTH.aprofile.Classes.Preferences.Preference;
import com.LTH.aprofile.Classes.Preferences.SoundLevelPreference;
import com.LTH.aprofile.Classes.Preferences.VibrationPreference;
import com.LTH.aprofile.Classes.Sensors.GestureActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;



public class SettingsChartActivity extends GestureActivity {

	private PieChartOverlay2 pieChart;
	
	private Profile p;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings_chart);
		this.addListenForGesture(GESTURE_LEFT);
		this.addListenForGesture(GESTURE_RIGHT);
		this.setGestureUpdateInterval(1000);
		this.gestureSensor.initiate();

		
		
		p = new Profile();
		p.setName("EDUROAM");
		SoundLevelPreference pref2 = new SoundLevelPreference(20, this);
		BrightnessPreference pref3 = new BrightnessPreference(50, this);
		VibrationPreference pref4 = new VibrationPreference(0, this);
		p.addPref(pref2);
		p.addPref(pref3);
		p.addPref(pref4);
		
		Collection<Preference> pref =p.getPref().values();
		
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.hello);
		pieChart = new PieChartOverlay2(this, pref, 800);
		linearLayout.addView(pieChart);
		pieChart.createAnimation(null);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings_chart, menu);
		return true;
	}


	@Override
	public void onGesture(int gesture) {
		switch (gesture) {
		case GESTURE_RIGHT:
			pieChart.rotateSteps(1);
			break;

		case GESTURE_LEFT:
			pieChart.rotateSteps(-1);
			break;

		}

	}

	class PieChartOverlay2 extends View {

		private DynamicAnimation2 rotate;
		RectF rectf;
		private int prevSelection;
		protected Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		protected int size;
		protected int amountSlices;
		protected int selectionOffset;
		protected Collection<Preference> prefs;

		// size in diameter
		public PieChartOverlay2(Context context, Collection<Preference> prefs, int size) {
			super(context);
			this.size = (int) (size * 0.5);
			this.amountSlices = prefs.size();
			selectionOffset = 0;
			this.prefs = prefs;

			rectf = new RectF(0, 0, size, size);
			
			prevSelection = 0;

		}

		@Override
		protected void onDraw(Canvas canvas) {
			
			
			int i = 0;
			paint.setColor(Color.BLACK);
			for (Preference pref : prefs) {
				
				//paint.setAlpha(100);
				Log.d("test", "" +i);
				canvas.drawArc(rectf, selectionOffset, (i+1)* 360 / amountSlices,
						true, paint);
				
				selectionOffset += 360 / amountSlices;
				i++;
				paint.setColor(Color.BLUE);
				
				
			}

			paint.setColor(Color.WHITE);
			for (i = 0; i < amountSlices; i++) {
				float angle = selectionOffset + i * (360 / amountSlices);
				angle = (float) ((Math.PI / 180) * angle);
				canvas.drawLine(size, size, size * (float) Math.cos(angle)
						+ size, size * (float) Math.sin(angle) + size, paint);
			}

			// draw white circle in center
			paint.setColor(Color.WHITE);
			canvas.drawCircle(size, size, size * 0.0f, paint);

		}

		// itemNo is the n:th slice counting from top clockwise. should be a
		// value
		// between 0 and amountSlices
		void createAnimation(Canvas canvas) {
			rotate = new DynamicAnimation2(0, 0, size, size);
			//rotate.setRepeatMode(Animation.REVERSE);
			rotate.setDuration(1000);
			rotate.setRepeatCount(Animation.INFINITE);

			startAnimation(rotate);
		}

		// itemNo is the n:th slice counting from top clockwise. should be a
		// value
		// between 0 and amountSlices
		public void rotateSteps(int sliceSteps) {

			rotate.addDegrees(sliceSteps*360/amountSlices);
			rotate.cancel();
			rotate.start();
		}
	}

	class DynamicAnimation2 extends Animation {
		private float mFromDegrees;
		private float mToDegrees;

		private int mPivotXType = ABSOLUTE;
		private int mPivotYType = ABSOLUTE;
		private float mPivotXValue = 0.0f;
		private float mPivotYValue = 0.0f;

		private float mPivotX;
		private float mPivotY;

		public DynamicAnimation2(Context context, AttributeSet attrs) {
			super(context, attrs);

		}

		public DynamicAnimation2(float fromDegrees, float toDegrees,
				float pivotX, float pivotY) {
			mFromDegrees = fromDegrees;
			mToDegrees = toDegrees;

			mPivotXType = ABSOLUTE;
			mPivotYType = ABSOLUTE;
			mPivotXValue = pivotX;
			mPivotYValue = pivotY;
		}

		@Override
		protected void applyTransformation(float interpolatedTime,
				Transformation t) {
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
			mPivotY = resolveSize(mPivotYType, mPivotYValue, height,
					parentHeight);
		}

		public synchronized void addDegrees(float deg) {
			this.mToDegrees += deg;
		}

	}
}
