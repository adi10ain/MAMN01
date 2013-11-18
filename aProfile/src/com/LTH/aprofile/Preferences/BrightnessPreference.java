package com.LTH.aprofile.Preferences;

import com.LTH.aprofile.Profile;
import com.LTH.aprofile.R;

import android.app.Activity;
import android.provider.Settings;
import android.view.WindowManager;

public class BrightnessPreference extends Preference {

	// Brightness preference, prefValue should be between 0-255
	public BrightnessPreference(int prefValue, Activity callingActivity) {
		super(prefValue, callingActivity);

		type = Profile.BRIGHTNESS;
		name = "Brightness";
		iconResId = R.drawable.brightness64;

	}

	@Override
	public void load() {

		Settings.System.putInt(callingActivity.getContentResolver(),
				Settings.System.SCREEN_BRIGHTNESS, prefValue);
		WindowManager.LayoutParams lp = callingActivity.getWindow().getAttributes();
		lp.screenBrightness = prefValue/255.0f;// 100 / 100.0f;
		callingActivity.getWindow().setAttributes(lp);

	}

}
