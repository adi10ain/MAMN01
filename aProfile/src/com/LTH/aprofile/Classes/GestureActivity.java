package com.LTH.aprofile.Classes;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;

public abstract class GestureActivity extends Activity {

	// CONSTANTS
	public static final int GESTURE_NOT_FOUND = -1;
	public static final int GESTURE_UP = 0;
	public static final int GESTURE_RIGHT = 1;
	public static final int GESTURE_DOWN = 2;
	public static final int GESTURE_LEFT = 3;
	public static final int GESTURE_SHAKE = 4;

	// minimum time between a new gesture is reported (0 for no limit)
	private int[] minUpdateInterval;

	// list of gestures to listen for
	private ArrayList<Integer> listenForTheseGestures;

	private GestureSensor gestureSensor;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		listenForTheseGestures = new ArrayList<Integer>();
		minUpdateInterval = new int[] { 0};
		gestureSensor = new GestureSensor(this);

	}

	// minimum time between a new gesture is reported (0 for no limit - default)
	protected void setGestureUpdateInterval(Integer interval) {
		minUpdateInterval[0] = interval;
	}

	// adds a gesture to listen for
	protected void addListenForGesture(int gesture) {
		listenForTheseGestures.add(gesture);
	}

	// called when a gesture is recognized
	public abstract void onGesture(int gesture);

	public int[] getUpdateInterval() {

		return minUpdateInterval;
	}

	public ArrayList<Integer> getGestureList() {
		return listenForTheseGestures;
	}
}
