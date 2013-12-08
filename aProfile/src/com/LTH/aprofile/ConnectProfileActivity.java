package com.LTH.aprofile;

import java.util.ArrayList;
import java.util.LinkedList;

import com.LTH.aprofile.Classes.Profile;
import com.LTH.aprofile.Classes.Preferences.Preference;
import com.LTH.aprofile.Classes.Sensors.GestureActivity;
import com.LTH.aprofile.GUI.PiechartOverlayView;
import com.LTH.aprofile.GUI.PiechartTextView;
import com.LTH.aprofile.GUI.PiechartView;

import android.os.Bundle;
import android.content.Intent;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public final class ConnectProfileActivity extends GestureActivity {
	private Profile targetProfile;

	private TextView TV_profileName;

	private PiechartOverlayView chartSelectionOverlay;
	private PiechartTextView piechartText;
	private RelativeLayout pieChartLayout;

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
		pieChartLayout = (RelativeLayout) findViewById(R.id.pieChart);
		pieChartLayout.addView(pieMenu);

		chartSelectionOverlay = new PiechartOverlayView(this, 3, 225);
		pieChartLayout.addView(chartSelectionOverlay);
		chartSelectionOverlay.createAnimation(null);

		piechartText = new PiechartTextView(this, 225);
		pieChartLayout.addView(piechartText);

		addPieIcons();

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

	public void addPieIcons() {
		ImageView icon = new ImageView(this);

		int scale = getDIP(20);

		// approve icon
		icon.setImageResource(R.drawable.pie_approve);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				scale, scale);
		int posX = getDIP(187);
		int posY = getDIP(73);
		layoutParams.leftMargin = posX;
		layoutParams.topMargin = posY;
		icon.setLayoutParams(layoutParams);
		icon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		pieChartLayout.addView(icon);

		// decline icon
		icon = new ImageView(this);
		icon.setImageResource(R.drawable.pie_decline);
		layoutParams = new RelativeLayout.LayoutParams(scale, scale);

		posX = getDIP(18);
		posY = getDIP(73);
		layoutParams.leftMargin = posX;
		layoutParams.topMargin = posY;
		icon.setLayoutParams(layoutParams);
		pieChartLayout.addView(icon);

		// timer icon
		icon = new ImageView(this);
		icon.setImageResource(R.drawable.pie_timer);
		layoutParams = new RelativeLayout.LayoutParams(scale, scale);
		posX = getDIP(102);
		posY = getDIP(193);
		layoutParams.leftMargin = posX;
		layoutParams.topMargin = posY;
		icon.setLayoutParams(layoutParams);
		pieChartLayout.addView(icon);
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

	// returns device independent pixels
	public int getDIP(int size) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				size, getResources().getDisplayMetrics());
	}

}
