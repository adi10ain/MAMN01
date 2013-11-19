package com.LTH.aprofile.Classes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.MediaRecorder;
import android.os.SystemClock;
import android.widget.ListView;
import android.widget.TextView;

public class SoundMeter extends Thread {

    private MediaRecorder mRecorder = null;
    private TextView textToUpdate;
    private ListView listToUpdate;
    
    //calling activity
    private Activity callingActivity;
    
    
    public SoundMeter(TextView textToUpdate, ListView listToUpdate, Activity callingActivity) {
    	this.textToUpdate = textToUpdate;
    	this.listToUpdate = listToUpdate;
    	this.callingActivity = callingActivity;
    	
    	startRec();
    }
    
    // ful lösning
    public void run() {
    	

            	while(true) {
            		callingActivity.runOnUiThread(new Runnable() {
            			@SuppressLint("NewApi")
						public void run() {
            				Double amplitude = getAmplitude();
            				if (amplitude > 0)
            					textToUpdate.setText("Amplitude: "+amplitude);
            				
            				listToUpdate.scrollListBy((int)(amplitude/500));
            			}
            		 });
            		SystemClock.sleep(10);
            		
            	}
            
       
    
    }

    public void startRec() {
            if (mRecorder == null) {
            	try {
                    mRecorder = new MediaRecorder();
                    mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    mRecorder.setOutputFile("/dev/null"); 
                    mRecorder.prepare();
                    mRecorder.start();
            	} catch(Exception e) {}
            }
    }

    public void stopRec() {
            if (mRecorder != null) {
                    mRecorder.stop();       
                    mRecorder.release();
                    mRecorder = null;
            }
    }

    public double getAmplitude() {
            if (mRecorder != null)
                    return  mRecorder.getMaxAmplitude();
            else
                    return -1;

    }
}