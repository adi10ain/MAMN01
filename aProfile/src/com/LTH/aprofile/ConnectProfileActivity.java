package com.LTH.aprofile;

import java.util.ArrayList;
import java.util.LinkedList;

import com.LTH.aprofile.Classes.Profile;
import com.LTH.aprofile.Classes.Sensors.GestureActivity;
import com.LTH.aprofile.GUI.PiechartOverlayView;
import com.LTH.aprofile.GUI.PiechartTextView;
import com.LTH.aprofile.GUI.PiechartView;
import com.LTH.aprofile.Preferences.Preference;
import com.LTH.aprofile.Preferences.VibrationPreference;

import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public final class ConnectProfileActivity extends GestureActivity {
	private Profile targetProfile;

	private TextView TV_profileName;

	PiechartOverlayView chartSelectionOverlay;
	PiechartTextView piechartText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addListenForGesture(GESTURE_DOWN);
		addListenForGesture(GESTURE_RIGHT);
		addListenForGesture(GESTURE_LEFT);
		setGestureUpdateInterval(30);
		setRepeatSameGestures(false);
		gestureSensor.initiate();

		setContentView(R.layout.activity_newprofile);

		targetProfile = MainActivity.targetProfile;

		TV_profileName = (TextView) findViewById(R.id.ProfileName);
		TV_profileName.setText("" + targetProfile);

		// first row of buttons
		ArrayList<View> rowButtons1 = new ArrayList<View>();
		rowButtons1.add(findViewById(R.id.declineProfile));
		rowButtons1.add(findViewById(R.id.acceptProfile));

		PiechartView pieMenu = new PiechartView(this, 3, 225);
		RelativeLayout pieChartLayout = (RelativeLayout) findViewById(R.id.pieChart);
		pieChartLayout.addView(pieMenu);

		chartSelectionOverlay = new PiechartOverlayView(this, 3, 225);
		pieChartLayout.addView(chartSelectionOverlay);
		chartSelectionOverlay.createAnimation(null);
		
		piechartText = new PiechartTextView(this, 225);
		pieChartLayout.addView(piechartText);
		

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
			piechartText.setSelection(0);
			break;
		case GESTURE_DOWN:
			chartSelectionOverlay.setSelection(1);
			piechartText.setSelection(1);
			break;
		case GESTURE_LEFT:
			chartSelectionOverlay.setSelection(2);
			piechartText.setSelection(2);

		}

	}

}

