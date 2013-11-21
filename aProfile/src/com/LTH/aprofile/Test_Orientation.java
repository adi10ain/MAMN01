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
	private final float NOISE = (float) 0.0;
	 private SensorManager mSensorManager;
	  private Sensor mOrientation;
	//  private float[] mValuesMagnet, mValuesAccel, mValuesOrientation, mRotationMatrix;
     private final float[] mValuesMagnet      = new float[3];
     private final float[] mValuesAccel       = new float[3];
     private final float[] mValuesOrientation = new float[3];
     private final float[] mRotationMatrix    = new float[9];
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_test__orientation);

        final float[] mValuesMagnet      = new float[3];
        final float[] mValuesAccel       = new float[3];
        final float[] mValuesOrientation = new float[3];
        final float[] mRotationMatrix    = new float[9];
        
	    mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	    //mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
	    mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 
                SensorManager.SENSOR_DELAY_NORMAL);
	    mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), 
                SensorManager.SENSOR_DELAY_NORMAL);
	  }

	  @Override
	  public void onAccuracyChanged(Sensor sensor, int accuracy) {
	    // Do something here if sensor accuracy changes.
	    // You must implement this callback in your code.
	  }

	  @Override
	  protected void onResume() {
	    super.onResume();
	    mSensorManager.registerListener(this, mOrientation, SensorManager.SENSOR_DELAY_NORMAL);
	  }

	  @Override
	  protected void onPause() {
	    super.onPause();
	    mSensorManager.unregisterListener(this);
	  }

	  @Override
	  public void onSensorChanged(SensorEvent event) {
			TextView tvX = (TextView) findViewById(R.id.textview1);
			TextView tvY = (TextView) findViewById(R.id.textview2);
			TextView tvZ = (TextView) findViewById(R.id.textview3);
			ImageView iv = (ImageView) findViewById(R.id.image);
			
		    switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                System.arraycopy(event.values, 0, mValuesAccel, 0, 3);
                break;

            case Sensor.TYPE_MAGNETIC_FIELD:
                System.arraycopy(event.values, 0, mValuesMagnet, 0, 3);
                break;
        }
		    

            SensorManager.getRotationMatrix(mRotationMatrix, null, mValuesAccel, mValuesMagnet);
            SensorManager.getOrientation(mRotationMatrix, mValuesOrientation);
            tvX.setText(""+mValuesOrientation[0]);
            tvY.setText(""+mValuesOrientation[1]);
            tvZ.setText(""+mValuesOrientation[2]);
          
	  }
	}
 


