package com.LTH.aprofile;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class Test_Orientation extends Activity implements SensorEventListener {
	private float mLastX, mLastY, mLastZ;
	private boolean mInitialized;
	private final float NOISE = (float) 1;
	private SensorManager mSensorManager;
	private Sensor mOrientation;
	// private float[] mValuesMagnet, mValuesAccel, mValuesOrientation,
	// mRotationMatrix;
	private final float[] mValuesMagnet = new float[3];
	private final float[] mValuesAccel = new float[3];
	private final float[] mValuesOrientation = new float[3];
	private final float[] mRotationMatrix = new float[9];
	private long prevTime = 0;
	public float yViewp = 10;
	public float yViewn = -10;
	public float zViewp = 10;
	public float zViewn = -10;
	public float filterYp = 0;
	public float filterYn = 0;
	public float filterZp = 0;
	public float filterZn = 0;
	

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test__orientation);

		final float[] mValuesMagnet = new float[3];
		final float[] mValuesAccel = new float[3];
		final float[] mValuesOrientation = new float[3];
		final float[] mRotationMatrix = new float[9];

		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		// mOrientation =
		// mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_NORMAL);
		// mSensorManager.registerListener(this,
		// mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
		// SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// Do something here if sensor accuracy changes.
		// You must implement this callback in your code.
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, mOrientation,
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
	}

	public void onSensorChanged(SensorEvent event) {
		TextView tvX = (TextView) findViewById(R.id.textview1);
		TextView tvY = (TextView) findViewById(R.id.textview2);
		TextView tvZ = (TextView) findViewById(R.id.textview3);
		TextView testView = (TextView) findViewById(R.id.testView);
		
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
			if (deltaY > (yViewp + filterYn)) {
				long tmp = (long) ((event.timestamp - prevTime)/1e9);
				if(tmp > 0.5){					
					testView.setText("Direction is: UP");
					prevTime = event.timestamp;
				} 
					
				
//				filterYp = -10;
//				filterYn = 0;
//				filterZp = 0;
//				filterZn = 0;
			} else if (deltaY < (yViewn + filterYp)) {
				long tmp = (long) ((event.timestamp - prevTime)/1e9);
				if(tmp > 0.5){					
					testView.setText("Direction is: DOWN");
					prevTime = event.timestamp;
				}
					
				
//				filterYn = 10;
//				filterYp = 0;
//				filterZp = 0;
//				filterZn = 0;
			} else if (deltaZ > (zViewp + filterZn)) {
				long tmp = (long) ((event.timestamp - prevTime)/1e9);
				if(tmp > 0.5){	
				testView.setText("Direction is: RIGHT");
				prevTime = event.timestamp;
				} 
				
//				filterZp = -10;
//				filterZn = 0;
//				filterYp = 0;
//				filterYn = 0;
			} else if (deltaZ < (zViewn + filterZp)) {
				long tmp = (long) ((event.timestamp - prevTime)/1e9);
				if(tmp > 0.5){
				testView.setText("Direction is: LEFT");
				prevTime = event.timestamp;
				} 
					
				
//					filterZn = 10;
//					filterZp = 0;
//					filterYp = 0;
//					filterYn = 0;

			} else {
				testView.setText("");
			}

			mLastX = x;
			mLastY = y;
			mLastZ = z;
			tvX.setText(" X: " + deltaX);
			tvY.setText(" Y: " + deltaY);
			tvZ.setText(" Z: " + deltaZ);

		}
	}

	// TextView tvX = (TextView) findViewById(R.id.textview1);
	// TextView tvY = (TextView) findViewById(R.id.textview2);
	// TextView tvZ = (TextView) findViewById(R.id.textview3);
	// TextView testView = (TextView) findViewById(R.id.testView);
	// ImageView iv = (ImageView) findViewById(R.id.image);
	//
	// switch (event.sensor.getType()) {
	// case Sensor.TYPE_ACCELEROMETER:
	// System.arraycopy(event.values, 0, mValuesAccel, 0, 3);
	// break;
	//
	// case Sensor.TYPE_MAGNETIC_FIELD:
	// System.arraycopy(event.values, 0, mValuesMagnet, 0, 3);
	// break;
	// }
	//
	//
	// SensorManager.getRotationMatrix(mRotationMatrix, null, mValuesAccel,
	// mValuesMagnet);
	// SensorManager.getOrientation(mRotationMatrix, mValuesOrientation);
	//
	// float x = mValuesOrientation[0];
	// float y = mValuesOrientation[1];
	// float z = mValuesOrientation[2];
	//
	// //setting the first values.
	// if (!mInitialized) {
	// mLastX = x;
	// mLastY = y;
	// mLastZ = z;
	// mInitialized = true;
	//
	// } else {
	//
	// float deltaX = (mLastX - x);
	// float deltaY = (mLastY - y);
	// float deltaZ = (mLastZ - z);
	// tvX.setText(deltaX+"");
	// tvY.setText(deltaY+"");
	// tvZ.setText(deltaZ+"");
	// // Re-set the values
	//
	//
	// // Filter out Noise
	// if (Math.abs(deltaX) < NOISE){
	// deltaX = (float) 0.0;
	// tvX.setText(deltaX+"");
	// }
	// if (Math.abs(deltaY) < NOISE){
	// deltaY = (float) 0.0;
	// tvY.setText(deltaX+"");
	// }
	// if (Math.abs(deltaZ) < NOISE){
	// deltaZ = (float) 0.0;
	// tvZ.setText(deltaX+"");
	// }
	//
	//
	//
	// if (Math.abs(deltaX) > Math.abs(deltaY)) {
	// if (deltaX > 0) {
	// testView.setText("Vänster");
	// } else {
	// testView.setText("Höger");
	// }
	//
	// } else if (Math.abs(deltaY) > Math.abs(deltaX)) {
	// if (deltaY > 0) {
	// testView.setText("NER");
	// } else {
	// testView.setText("UP");
	// }
	//
	// } else {
	// testView.setText(" ");
	// }
	// }
	// mLastX = x;
	// mLastY = y;
	// mLastZ = z;
	// }
	 
}
