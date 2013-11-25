package com.LTH.aprofile;

import java.util.ArrayList;

import com.LTH.aprofile.Classes.GestureSensor;
import com.LTH.aprofile.Classes.KeyPress;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Test_Orientation extends Activity {

	LinearLayout row;

	private int[] gestureSeries;
	private int currentPos;
	private int currentGesture;

	private static int MAX_AMOUNT_GESTURES;

	GestureSensor gestSensor;

	ArrayList<int[]> savedGestures;
	ArrayList<int[]> possibleGestures;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_test__orientation);
		row = (LinearLayout) findViewById(R.id.row);
		MAX_AMOUNT_GESTURES = row.getChildCount();

		gestureSeries = new int[MAX_AMOUNT_GESTURES];
		currentPos = -1;
		currentGesture = -1;
		gestSensor = new GestureSensor(this, this);

		savedGestures = new ArrayList<int[]>();

		int[] gest1 = { 0, 0, 1, 1, 2 }; // up up right right down
		int[] gest2 = { 3, 0, 1, 1, 2 }; // left up right right down
		int[] gest3 = { 3, 0, 1, 2, 1 }; // left up right down right

		savedGestures.add(gest1);
		savedGestures.add(gest2);
		savedGestures.add(gest3);

		possibleGestures = savedGestures;

	}

	// Called whenever a gesture is detected
	public void onGesture(int gesture) {
		if (gesture != GestureSensor.GESTURE_NOT_FOUND
				&& currentPos < MAX_AMOUNT_GESTURES - 1) {
			currentGesture = gesture;
			int nextPos = currentPos + 1;
			TextView tv = (TextView) row.getChildAt(nextPos);
			tv.setText(gestureToString(currentGesture));
			if (checkPossibilty(currentGesture))
				tv.setBackgroundColor(Color.GREEN);
			else
				tv.setBackgroundColor(Color.RED);
			checkPossibilty(currentGesture);
			checkIfLastSolution();

		}
	}

	public boolean checkPossibilty(int nextGesture) {
		boolean ret = false;

		for (int[] gestCombo : possibleGestures) {
			if (gestCombo[currentPos + 1] == nextGesture) {
				ret = true;
				break;
			}
		}




		return ret;
	}

	public boolean checkIfLastSolution() {
		boolean ret = false;
		// only one possible gesture left! do something
		if (possibleGestures.size() == 1) {
			for (int i = currentPos + 1; i < MAX_AMOUNT_GESTURES; i++) {
				TextView tv = (TextView) row.getChildAt(i);
				int gest = possibleGestures.get(0)[i];
				tv.setText(gestureToString(gest));
				tv.setBackgroundColor(Color.BLUE);
				currentPos++;
			}
			ret = true;
		}
		return ret;
	}

	private void addGesture() {
		int nextPos = currentPos + 1;

		ArrayList<int[]> removeList = new ArrayList<int[]>();
		for (int[] gestCombo : possibleGestures) {
			if (gestCombo[nextPos] != currentGesture)
				removeList.add(gestCombo);
		}
		possibleGestures.removeAll(removeList);

		gestureSeries[nextPos] = currentGesture;

		TextView tv = (TextView) row.getChildAt(nextPos);
		tv.setText(gestureToString(currentGesture));
		currentPos++;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN
				&& currentPos < MAX_AMOUNT_GESTURES - 1) {
			addGesture();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public String gestureToString(int gesture) {
		String ret = "error gestureToString";
		switch (gesture) {
		case GestureSensor.GESTURE_UP:
			ret = "^";
			break;
		case GestureSensor.GESTURE_RIGHT:
			ret = ">";
			break;
		case GestureSensor.GESTURE_DOWN:
			ret = "v";
			break;
		case GestureSensor.GESTURE_LEFT:
			ret = "<";
			break;
		case GestureSensor.GESTURE_SHAKE:
			ret = "*";
			break;
		case GestureSensor.GESTURE_NOT_FOUND:
			break;
		}

		return ret;
	}
}
