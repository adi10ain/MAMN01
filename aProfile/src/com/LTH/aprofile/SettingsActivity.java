package com.LTH.aprofile;

import java.util.Iterator;

import com.LTH.aprofile.Classes.GestureSelector;
import com.LTH.aprofile.Classes.Profile;
import com.LTH.aprofile.Classes.Settings;
import com.LTH.aprofile.Preferences.BrightnessPreference;
import com.LTH.aprofile.Preferences.SoundLevelPreference;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SettingsActivity extends Activity {

	LinearLayout list_Profiles;
	private static Settings settings;
	private GestureSelector gestSelect;
	public static Profile selectedProfile;

	private static Activity activity;

	public static final int REQUEST_CODE_CHANGE_PROFILE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);

		gestSelect = new GestureSelector(this);
		gestSelect.initGestureSensor();
		settings = MainActivity.settings;

		list_Profiles = (LinearLayout) findViewById(R.id.listProfiles);

		// set default profile as a new one
		selectedProfile = new Profile();
		selectedProfile.setName("Add new profile");
		SoundLevelPreference pref2 = new SoundLevelPreference(50, this);
		BrightnessPreference pref3 = new BrightnessPreference(50, this);
		selectedProfile.addPref(pref2);
		selectedProfile.addPref(pref3);

		activity = this;

		listAllProfiles();
	}

	private void listAllProfiles() {
		// Clear old wifi-profiles list
		list_Profiles.removeAllViews();
		// Displays all profiles in the wifi-profiles list
		Iterator<Profile> profiles = settings.getProfiles();
		while (profiles.hasNext()) {
			final Profile profile = profiles.next();

			TextView tv = new TextView(this);
			tv.setText("" + profile);
			tv.setTextSize(20.0f);
			tv.setPadding(10, 20, 20, 20);

			tv.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					selectedProfile = profile;
					Intent myIntent = new Intent(activity,
							EditProfileActivity.class);
					activity.startActivityForResult(myIntent,
							REQUEST_CODE_CHANGE_PROFILE);
				}
			});

			list_Profiles.addView(tv);
		}

		// Add new profile item
		final Profile newProfile = new Profile();
		SoundLevelPreference pref4 = new SoundLevelPreference(100, this);
		BrightnessPreference pref5 = new BrightnessPreference(100, this);
		newProfile.addPref(pref4);
		newProfile.addPref(pref5);
		TextView tv = new TextView(this);
		tv.setText("" + newProfile);
		tv.setTextSize(20.0f);
		tv.setPadding(10, 20, 20, 20);

		tv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				MainActivity.settings.addProfile(newProfile);
				selectedProfile = newProfile;
				Intent myIntent = new Intent(activity,
						EditProfileActivity.class);
				activity.startActivityForResult(myIntent,
						REQUEST_CODE_CHANGE_PROFILE);
			}
		});

		list_Profiles.addView(tv);

		gestSelect.addChildrenToRows(list_Profiles);

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		listAllProfiles();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

}
