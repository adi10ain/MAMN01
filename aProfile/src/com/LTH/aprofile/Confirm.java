package com.LTH.aprofile;

import com.LTH.aprofile.R;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Confirm extends Activity implements SensorEventListener {

	private float mLastX, mLastY, mLastZ;
	private SensorManager mgr;
	private Sensor acc;
	private boolean mInitialized;
	private final float NOISE = (float) 1.0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirm);
		mInitialized = false;
		mgr = (SensorManager) this.getSystemService(SENSOR_SERVICE);
		acc = mgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	}

	@Override
	protected void onResume() {
		mgr.registerListener(this, acc, SensorManager.SENSOR_DELAY_NORMAL);
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mgr.unregisterListener(this);
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	public void onSensorChanged(SensorEvent event) {
		TextView tvX = (TextView) findViewById(R.id.textview1);
		TextView tvY = (TextView) findViewById(R.id.textview2);
		TextView tvZ = (TextView) findViewById(R.id.textview3);
		ImageView iv = (ImageView) findViewById(R.id.image);
		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];

		// set values to 0. No movements
		if (!mInitialized) {
			mLastX = x;
			mLastY = y;
			mLastZ = z;
			tvX.setText("0.0");
			tvY.setText("0.0");
			tvZ.setText("0.0");
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

			tvX.setText(Float.toString(deltaX));
			tvY.setText(Float.toString(deltaY));
			tvZ.setText(Float.toString(deltaZ));

			//
			if (Math.abs(deltaX) > Math.abs(deltaY)) {
				if (deltaX > 0) {
					iv.setImageResource(R.drawable.arrow_right);
					iv.setVisibility(View.VISIBLE);
				} else {
					iv.setImageResource(R.drawable.arrow_left);
					iv.setVisibility(View.VISIBLE);
				}

			} else if (Math.abs(deltaY) > Math.abs(deltaX)) {
				if (deltaY > 0) {
					iv.setImageResource(R.drawable.arrow_up);
				} else {
					iv.setImageResource(R.drawable.arrow_down);
				}
				iv.setVisibility(View.VISIBLE);
			} else {

				iv.setVisibility(View.INVISIBLE);

			}

		}

	}
}
