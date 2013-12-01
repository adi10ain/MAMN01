package com.LTH.aprofile.Preferences;

import com.LTH.aprofile.R;
import com.LTH.aprofile.Classes.Profile;

import android.app.Activity;
import android.graphics.Color;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;

public class BrightnessPreference extends Preference {

	private WindowManager.LayoutParams lp;

	// Brightness preference, prefValue should be between 0-255
	public BrightnessPreference(int prefValue, Activity callingActivity) {
		super(prefValue, callingActivity);

		colorCode = Color.parseColor("#8000ABA9");

		type = Profile.BRIGHTNESS;
		name = "Brightness";
		iconResId = R.drawable.brightness64;

		// Settings.System.putInt(callingActivity.getContentResolver(),
		// Settings.System.SCREEN_BRIGHTNESS, prefValue);
		lp = callingActivity.getWindow().getAttributes();

	}

	@Override
	public void load() {
		set(prefValue, callingActivity); // 0 < prefValue < 255
		Settings.System.putInt(callingActivity.getContentResolver(),
				Settings.System.SCREEN_BRIGHTNESS, prefValue);
	}

	@Override
	public void set(int targetValue, Activity activity) {
		
		dynamicIcon.setAlpha(125);
		if (targetValue <= 0) {
			targetValue = 0;
			dynamicIcon.setAlpha(50);
		} else if (targetValue >= 100) {
			targetValue = 100;
			dynamicIcon.setAlpha(200);
		}
	
		
		float target = targetValue / 100f;
		lp.screenBrightness = target;
		activity.getWindow().setAttributes(lp);

	}
}
