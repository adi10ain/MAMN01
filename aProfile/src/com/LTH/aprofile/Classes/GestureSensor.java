package com.LTH.aprofile.Classes;

import com.LTH.aprofile.R;
import com.LTH.aprofile.Test_Orientation;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class GestureSensor implements SensorEventListener {
	private static final float SHAKE_THRESHOLD = 2000;
	float last_x;
	float last_y;
	float last_z;
	SensorManager sensorManager;

	private GestureSelector gestSelect;
	// change class name later
	private Test_Orientation gestureProfileSelector;

	long lastUpdate;

	public static final int GESTURE_NOT_FOUND = -1;
	public static final int GESTURE_UP = 0;
	public static final int GESTURE_RIGHT = 1;
	public static final int GESTURE_DOWN = 2;
	public static final int GESTURE_LEFT = 3;
	public static final int GESTURE_SHAKE = 4;
	

	public GestureSensor(Activity activity, GestureSelector gestSelect) {
		super();
		this.gestSelect = gestSelect;
		sensorManager = (SensorManager) activity
				.getSystemService(activity.SENSOR_SERVICE);
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
		lastUpdate = System.currentTimeMillis();

	}

	public GestureSensor(Activity activity,
			Test_Orientation gestureProfileSelector) {
		super();
		this.gestureProfileSelector = gestureProfileSelector;
		sensorManager = (SensorManager) activity
				.getSystemService(activity.SENSOR_SERVICE);
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
		lastUpdate = System.currentTimeMillis();

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
			int gesture = GESTURE_NOT_FOUND;
			long diffTime = (actualTime - lastUpdate);
			

			float shakeSpeed = Math.abs(x + y + z - last_x - last_y - last_z)
					/ diffTime * 10000;

			if (shakeSpeed > SHAKE_THRESHOLD)
				gesture = GESTURE_SHAKE;
			else if (x > 3.0000)
				gesture = GESTURE_LEFT;
			else if (x < -3.0000)
				gesture = GESTURE_RIGHT;
			else if (z > 8  && y > 1)
				gesture = GESTURE_DOWN;
			else if (z > 8  && y < -1)
				gesture = GESTURE_UP;

			if (gestSelect != null && (actualTime - lastUpdate) > 100) {

				gestSelect.onGesture(gesture);

				last_x = x;
				last_y = y;
				last_z = z;
				lastUpdate = actualTime;
			} else if (gestureProfileSelector != null) {
				gestureProfileSelector.onGesture(gesture);
			}
		}

	}
}
