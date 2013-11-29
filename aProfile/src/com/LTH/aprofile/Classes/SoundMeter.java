package com.LTH.aprofile.Classes;

import java.io.File;
import java.io.IOException;

import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
 
public class SoundMeter {
    static final private double EMA_FILTER = 0.6;

    private MediaRecorder recorder = null;
    private double mEMA = 0.0;
    private double startValue = 0.0;
    File audiofile = null; 

    public void startRec() {
    	if(recorder == null){
        File dir = Environment.getExternalStorageDirectory();  
        try {  
          audiofile = File.createTempFile("sound", ".3gp", dir);  
        } catch (IOException e) {  
          Log.e("InStart", "external storage access error");  
          return;  
        }  
            //Creating MediaRecorder and specifying audio source, output format, encoder & output format  
        recorder = new MediaRecorder();  
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);  
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);  
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);  
        recorder.setOutputFile(audiofile.getAbsolutePath());
        try {
			recorder.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        recorder.start();  
    	}
    }
   
    public void stopRec() {
            if (recorder != null) {
                recorder.stop();  
                recorder.release();  
                recorder = null;
            }
    }
   
    public double getAmplitude() {
            if (recorder != null){
            	return  (recorder.getMaxAmplitude()/2700.0);            	
            } else {
            	  Log.v("Amplitute", "SoundMeter is null");
                    return 0;
            }

    }

    public double getAmplitudeEMA() {
            double amp = getAmplitude();
            mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
            double dB = 20 * Math.log10(mEMA);
            return mEMA;
    }
    
    public boolean isKnock(double dB){
    	if(dB > 2.2){
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public void calibrate(){
    	startValue = getAmplitudeEMA();
    	startValue = getAmplitudeEMA();
    	   Log.d("dialog", ""+ startValue);
    }
}
