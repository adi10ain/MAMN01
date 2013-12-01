package com.LTH.aprofile.Classes.Sensors;

import java.util.ArrayList;


import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;

public abstract class GestureActivity extends Activity {

	// CONSTANTS
	public static final int GESTURE_NOT_FOUND = -1;
	public static final int GESTURE_HOLD = 0;
	public static final int GESTURE_UP = 1;
	public static final int GESTURE_RIGHT = 2;
	public static final int GESTURE_DOWN = 3;
	public static final int GESTURE_LEFT = 4;
	public static final int GESTURE_SHAKE = 5;

	// minimum time between a new gesture is reported (0 for no limit)
	private int minUpdateInterval;

	// list of gestures to listen for
	private ArrayList<Integer> listenForTheseGestures;

	protected GestureSensor2 gestureSensor;

	// determines if same gestures could be sent in a row
	private Boolean repeatSameGestures;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//s
		listenForTheseGestures = new ArrayList<Integer>();
		minUpdateInterval = 0;
		repeatSameGestures = true;
		gestureSensor = new GestureSensor2(this);
		

	}

	// minimum time between a new gesture is reported (0 for no limit - default)
	protected void setGestureUpdateInterval(int interval) {
		minUpdateInterval = interval;
	}

	// adds a gesture to listen for
	protected void addListenForGesture(int gesture) {
		listenForTheseGestures.add(gesture);
	}

	// called when a gesture is recognized
	public abstract void onGesture(int gesture);

	public int getUpdateInterval() {

		return minUpdateInterval;
	}

	public ArrayList<Integer> getGestureList() {
		return listenForTheseGestures;
	}

	public void setRepeatSameGestures(Boolean repeatSameGestures) {
		this.repeatSameGestures = repeatSameGestures;
	}

	public Boolean getRepeatSameGestures() {
		return repeatSameGestures;
	}
}
