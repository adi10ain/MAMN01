package com.LTH.aprofile;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class GestureSensor implements SensorEventListener  {
	private float mLastX, mLastY, mLastZ;
	private SensorManager mgr;
	private Sensor acc;
	private boolean mInitialized;
	private final float NOISE = (float) 2.0;
	private Activity activity;
	private GestureSelector gestSelect;
	
	//CONSTANTS
	public static final int GESTURE_UP = 0;
	public static final int GESTURE_RIGHT = 1;
	public static final int GESTURE_DOWN = 2;
	public static final int GESTURE_LEFT = 3;
	
	public GestureSensor(Activity activity,GestureSelector gestSelect) {	
		super();
		this.gestSelect = gestSelect;
		mInitialized = false;
		mgr = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
		acc = mgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		this.activity = activity;
		mgr.registerListener(this, acc, SensorManager.SENSOR_DELAY_NORMAL);
	
	}
	
	
	protected void onResume() {
		//mgr.registerListener(this, acc, SensorManager.SENSOR_DELAY_NORMAL);
		//((NewprofileActivity)activity).onResume();
	}

	
	protected void onPause() {
		//((NewprofileActivity)activity).onPause();
		//mgr.unregisterListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		
		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];

		// set values to 0. No movements
		if (!mInitialized) {
			mLastX = x;
			mLastY = y;
			mLastZ = z;
			mInitialized = true;
		} else {

			float deltaX = (mLastX - x);
			float deltaY = (mLastY - y);
			float deltaZ = (mLastZ - z);

			// Filter out Noise
			if (Math.abs(deltaX) < NOISE)
				deltaX = (float) 0.0;
			if (Math.abs(deltaY) < NOISE)
				deltaY = (float) 0.0;
			if (Math.abs(deltaZ) < NOISE)
				deltaZ = (float) 0.0;

			// Re-set the values
			mLastX = x;
			mLastY = y;
			mLastZ = z;

			//
			if (Math.abs(deltaX) > Math.abs(deltaY)) {
				if (deltaX > 0) {
					gestSelect.onGesture(GESTURE_RIGHT);
				} else {
					gestSelect.onGesture(GESTURE_LEFT);
				}

			} else if (Math.abs(deltaY) > Math.abs(deltaX)) {
				if (deltaY > 0) {
					gestSelect.onGesture(GESTURE_UP);
				} else {
					gestSelect.onGesture(GESTURE_DOWN);
				}
				
			} else {

			}

		}
		
	}
	


}
