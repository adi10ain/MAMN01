package com.LTH.aprofile.Classes;

import com.LTH.aprofile.R;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


public class TiltSensor implements SensorEventListener {
	private static final float SHAKE_THRESHOLD = 2000;
	float last_x;
	float last_y;
	float last_z;
	SensorManager sensorManager;

	GestureSelector gestSelect;

	long lastUpdate;

	public static final int GESTURE_UP = 0;
	public static final int GESTURE_RIGHT = 1;
	public static final int GESTURE_DOWN = 2;
	public static final int GESTURE_LEFT = 3;
	public static final int GESTURE_SHAKE = 4;
	
	public static final int X_AXIS = 0;
	public static final int Y_AXIS = 1;
	public static final int Z_AXIS = 2;
	
	

	public TiltSensor(Activity activity, GestureSelector gestSelect) {
		super();
		this.gestSelect = gestSelect;
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

			
		}

	}
}