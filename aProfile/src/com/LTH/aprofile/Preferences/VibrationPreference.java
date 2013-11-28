package com.LTH.aprofile.Preferences;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Vibrator;

import com.LTH.aprofile.R;
import com.LTH.aprofile.Classes.Profile;

public class VibrationPreference extends Preference {
	// vibration patterns
	public final static int VIBRATE_SINGLE = 0;
	public final static int VIBRATE_PROFILE_CONNECTED = 1;
	public final static int VIBRATE_SHARE_PROFILE_RECEIVE = 2;
	public final static int VIBRATE_SHARE_PROFILE_SEND = 3;

	// vibrator settings
	public final static int VIBRATOR_UNCHANGED = -1;
	public final static int VIBRATOR_OFF = 0;
	public final static int VIBRATOR_ON = 1;

	private static Vibrator vibrator;
	private AudioManager audioManager;

	/*
	 * prefValue should be either <50 (off) or >= 50 (on)
	 */
	public VibrationPreference(int prefValue, Activity callingActivity) {
		super(prefValue, callingActivity);
		type = Profile.VIBRATION;
		name = "Vibration";
		iconResId = R.drawable.vibration_icon;

		vibrator = (Vibrator) callingActivity
				.getSystemService(Context.VIBRATOR_SERVICE);
		audioManager = (AudioManager) callingActivity
				.getSystemService(Context.AUDIO_SERVICE);

	}

	@Override
	public void load() {
		set(prefValue, callingActivity);

	}

	@Override
	public void set(int targetValue, Activity activity) {

		int oldSetting = (prefValue >= 50) ? VIBRATOR_ON : VIBRATOR_OFF;
		int newSetting = (targetValue >= 50) ? VIBRATOR_ON : VIBRATOR_OFF;

		// only affect settings if there is a difference between previous
		// settings and current settings
		if (oldSetting == newSetting)
			newSetting = VIBRATOR_UNCHANGED;

		switch (newSetting) {
		case VIBRATOR_OFF:
			audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
					AudioManager.VIBRATE_SETTING_OFF);
			audioManager.setVibrateSetting(
					AudioManager.VIBRATE_TYPE_NOTIFICATION,
					AudioManager.VIBRATE_SETTING_OFF);
			break;
		case VIBRATOR_ON:
			audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
					AudioManager.VIBRATE_SETTING_ON);
			audioManager.setVibrateSetting(
					AudioManager.VIBRATE_TYPE_NOTIFICATION,
					AudioManager.VIBRATE_SETTING_ON);
			vibrate(VIBRATE_SINGLE);
			break;
		}

	}

	
	
	public static void vibrate(int vibrationPattern) {
		long[] pattern = { 0, 0 };
		switch (vibrationPattern) {
		// The cases is actions that the user performs. One action may
		// require different
		// vibration "tones" than others, like if something is accepted may
		// differ from a tone
		// that says "you've pressed a button

		case VIBRATE_SINGLE:

			pattern = new long[] { 0, 150 };

			break;
		case VIBRATE_PROFILE_CONNECTED:
			// Vibrates for 150 ms with no delay. -1 defines that only one
			// sequence will be done.
			pattern = new long[] { 0, 200, 75, 200, 75 };

		case VIBRATE_SHARE_PROFILE_RECEIVE:
			pattern = new long[] { 0, 500, 50, 100 };
			break;
		case VIBRATE_SHARE_PROFILE_SEND:
			pattern = new long[] { 0, 100, 50, 500 };
			break;

		}
		vibrator.vibrate(pattern, -1);

	}

}
