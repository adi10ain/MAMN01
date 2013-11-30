package com.LTH.aprofile.Classes.Sensors;

import com.LTH.aprofile.Classes.ProfileExchanger;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class ProximitySensor implements SensorEventListener {
	private SensorManager mSensorManager;
	private Sensor mSensor;

	public static int PROXIMITY_NEAR = 0;
	public static int PROXIMITY_FAR = 1;

	private int proximityValue;

	public ProximitySensor(Activity activity) {
		mSensorManager = (SensorManager) activity
				.getSystemService(activity.SENSOR_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
				SensorManager.SENSOR_DELAY_NORMAL);

		proximityValue = PROXIMITY_NEAR;
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	public Boolean proximityNear() {
		return (proximityValue == PROXIMITY_NEAR);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		float value = event.values[0];

		if (value == 0)
			proximityValue = PROXIMITY_NEAR;
		else
			proximityValue = PROXIMITY_FAR;

	}
}
