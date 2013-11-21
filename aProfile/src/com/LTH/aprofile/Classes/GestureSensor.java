package com.LTH.aprofile.Classes;

import com.LTH.aprofile.R;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class GestureSensor implements SensorEventListener {
	private float mLastX, mLastY, mLastZ;
	private SensorManager mgr;
	private Sensor acc, gyro, orient;
	private boolean mInitialized;
	private final float NOISE = 0.5f;
	private Activity activity;
	private GestureSelector gestSelect;


	// CONSTANTS
	public static final int GESTURE_UP = 0;
	public static final int GESTURE_RIGHT = 1;
	public static final int GESTURE_DOWN = 2;
	public static final int GESTURE_LEFT = 3;
	public float yViewp = 10;
	public float yViewn = -10;
	public float zViewp = 10;
	public float zViewn = -10;
	private long prevTime = 0;

	public GestureSensor(Activity activity, GestureSelector gestSelect) {
		super();
		this.gestSelect = gestSelect;
		mInitialized = false;
		mgr = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
		intiSensors();
		this.activity = activity;
		// GUI stuff



	}

	// Register all sensors
	private void intiSensors() {
		// Accelerometer sensor
		acc = mgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mgr.registerListener(this, acc, SensorManager.SENSOR_DELAY_NORMAL);
		// Gyro sensor
		gyro = mgr.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		mgr.registerListener(this, gyro, SensorManager.SENSOR_DELAY_NORMAL);
		// Orientation Sensor
		orient = mgr.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		mgr.registerListener(this, orient, SensorManager.SENSOR_DELAY_NORMAL);

	}

	// Implementeras vid senare tillfälle
	protected void onResume() {
		// mgr.registerListener(this, acc, SensorManager.SENSOR_DELAY_NORMAL);
		// ((NewprofileActivity)activity).onResume();
		intiSensors();
	}

	protected void onPause() {
		// ((NewprofileActivity)activity).onPause();
		mgr.unregisterListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("deprecation")
	@Override
	public void onSensorChanged(SensorEvent event) {

		switch (event.sensor.getType()) {
		case Sensor.TYPE_ORIENTATION:
			calcOrientation(event);
			break;

		}

	}

	private void calcOrientation(SensorEvent event) {

		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];

		if (!mInitialized) {
			mLastX = x;
			mLastY = y;
			mLastZ = z;
			mInitialized = true;

		} else {
			float deltaX = (mLastX - x);
			float deltaY = (mLastY - y);
			float deltaZ = (mLastZ - z);
			if (deltaY > (yViewp)) {
				long tmp = (long) ((event.timestamp - prevTime) / 1e9);
				if (tmp > 0.5) {
					gestSelect.onGesture(GESTURE_UP);
					prevTime = event.timestamp;
				}

			} else if (deltaY < (yViewn)) {
				long tmp = (long) ((event.timestamp - prevTime) / 1e9);
				if (tmp > 0.5) {
					gestSelect.onGesture(GESTURE_DOWN);
					prevTime = event.timestamp;
				}

			} else if (deltaZ > (zViewp)) {
				long tmp = (long) ((event.timestamp - prevTime) / 1e9);
				if (tmp > 0.5) {
					gestSelect.onGesture(GESTURE_RIGHT);
					prevTime = event.timestamp;
				}

			} else if (deltaZ < (zViewn)) {
				long tmp = (long) ((event.timestamp - prevTime) / 1e9);
				if (tmp > 0.5) {
					gestSelect.onGesture(GESTURE_LEFT);
					prevTime = event.timestamp;
				}

				// filterZn = 10;
				// filterZp = 0;
				// filterYp = 0;
				// filterYn = 0;

			}
			mLastX = x;
			mLastY = y;
			mLastZ = z;

		}
	}

}
