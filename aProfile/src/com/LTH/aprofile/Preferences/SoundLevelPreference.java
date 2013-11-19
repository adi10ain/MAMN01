package com.LTH.aprofile.Preferences;

import com.LTH.aprofile.R;
import com.LTH.aprofile.Classes.Profile;

import android.app.Activity;
import android.media.AudioManager;

public class SoundLevelPreference extends Preference {
	private AudioManager audioManager;
	

	public SoundLevelPreference(int prefValue, Activity callingActivity) {
		super(prefValue, callingActivity);
		name = "Sound Level";
		iconResId = R.drawable.volume64;
		
		type = Profile.SOUNDLEVEL;
		audioManager = (AudioManager)callingActivity.getSystemService(Activity.AUDIO_SERVICE);
	}

	@Override
	public void load() {
		audioManager.setStreamVolume(AudioManager.STREAM_RING, prefValue, AudioManager.FLAG_ALLOW_RINGER_MODES|AudioManager.FLAG_PLAY_SOUND);
		audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, prefValue, AudioManager.FLAG_ALLOW_RINGER_MODES|AudioManager.FLAG_PLAY_SOUND);
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, prefValue, AudioManager.FLAG_ALLOW_RINGER_MODES|AudioManager.FLAG_PLAY_SOUND);
	}



}
