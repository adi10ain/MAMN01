package com.LTH.aprofile.Classes.Sensors;

import java.util.ArrayList;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class GestureSensor implements SensorEventListener {
	private static final float SHAKE_THRESHOLD = 2000;
	float last_x;
	float last_y;
	float last_z;
	SensorManager sensorManager;

	private GestureActivity gestureListener;

	// ugly solution, array to pass reference to int rather than the value
	private int minUpdateInterval;

	private ArrayList<Integer> listenForTheseGestures;

	long lastUpdate;

	private int prevGesture;

	// determines if same gestures could be sent in a row
	private Boolean repeatSameGestures;

	public GestureSensor(GestureActivity gestureListener) {
		super();
		this.gestureListener = gestureListener;

		sensorManager = (SensorManager) gestureListener
				.getSystemService(Context.SENSOR_SERVICE);
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);

	}

	public void initiate() {
		minUpdateInterval = gestureListener.getUpdateInterval();

		listenForTheseGestures = gestureListener.getGestureList();
		repeatSameGestures = gestureListener.getRepeatSameGestures();

		lastUpdate = System.currentTimeMillis();

		if (listenForTheseGestures == null)
			listenForTheseGestures = new ArrayList<Integer>();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// Do something here if sensor accuracy changes.
		// You must implement this callback in your code.
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

			float[] values = event.values;

			// Movement
			float x = values[0];
			float y = values[1];
			float z = values[2];

			/*
			 * float accelationSquareRoot = (x * x + y * y + z * z) /
			 * (SensorManager.AXIS_X * SensorManager.AXIS_X);
			 */

			long actualTime = System.currentTimeMillis();
			int gesture = GestureActivity.GESTURE_NOT_FOUND;
			long diffTime = (actualTime - lastUpdate);

			float shakeSpeed = Math.abs(x + y + z - last_x - last_y - last_z)
					/ diffTime * 10000;

			if (listenForTheseGestures.contains(GestureActivity.GESTURE_SHAKE)
					&& shakeSpeed > SHAKE_THRESHOLD)
				gesture = GestureActivity.GESTURE_SHAKE;
			else if (listenForTheseGestures
					.contains(GestureActivity.GESTURE_LEFT) && x > 3.0000)
				gesture = GestureActivity.GESTURE_LEFT;
			else if (listenForTheseGestures
					.contains(GestureActivity.GESTURE_RIGHT) && x < -3.0000)
				gesture = GestureActivity.GESTURE_RIGHT;
			else if (listenForTheseGestures
					.contains(GestureActivity.GESTURE_DOWN) && z > 8 && y > 1)
				gesture = GestureActivity.GESTURE_DOWN;
			else if (listenForTheseGestures
					.contains(GestureActivity.GESTURE_UP) && z > 8 && y < -1)
				gesture = GestureActivity.GESTURE_UP;

			if ((actualTime - lastUpdate) > minUpdateInterval) {
				
				if (repeatSameGestures || prevGesture != gesture)
					gestureListener.onGesture(gesture);

				last_x = x;
				last_y = y;
				last_z = z;
				lastUpdate = actualTime;
				prevGesture = gesture;
			}

		}

	}
}
