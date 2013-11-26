package com.LTH.aprofile.Preferences;

import com.LTH.aprofile.R;
import com.LTH.aprofile.Classes.Profile;

import android.app.Activity;
import android.graphics.Color;
import android.media.AudioManager;
import android.util.Log;

public class SoundLevelPreference extends Preference {
	private AudioManager audioManager;
	private int ringMaxVolume;
	private int systemMaxVolume;
	private int musicMaxVolume;

	public SoundLevelPreference(int targetValue, Activity callingActivity) {
		super(targetValue, callingActivity);
		name = "Sound Level";
		colorCode = Color.parseColor("#80A05000");
		iconResId = R.drawable.volume64;
		
		type = Profile.SOUNDLEVEL;
		audioManager = (AudioManager)callingActivity.getSystemService(Activity.AUDIO_SERVICE);
		
		ringMaxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
		systemMaxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
		musicMaxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	}

	@Override
	public void load() {
		set(prefValue, callingActivity);
	}

	@Override
	public void set(int targetValue, Activity activity) {
		int ringVolume = (int) ((targetValue/100.0)*ringMaxVolume);
		int systemVolume = (int) ((targetValue/100.0)*systemMaxVolume);
		int musicVolume = (int) ((targetValue/100.0)*musicMaxVolume);
		
		audioManager.setStreamVolume(AudioManager.STREAM_RING, ringVolume, AudioManager.FLAG_ALLOW_RINGER_MODES|AudioManager.FLAG_PLAY_SOUND);
		audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, systemVolume, AudioManager.FLAG_ALLOW_RINGER_MODES|AudioManager.FLAG_PLAY_SOUND);
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, musicVolume, AudioManager.FLAG_ALLOW_RINGER_MODES|AudioManager.FLAG_PLAY_SOUND);
		
	}



}
