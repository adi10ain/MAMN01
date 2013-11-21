package com.LTH.aprofile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.LTH.aprofile.Classes.GestureSelector;
import com.LTH.aprofile.Classes.Profile;
import com.LTH.aprofile.Classes.Settings;
import com.LTH.aprofile.Preferences.BrightnessPreference;
import com.LTH.aprofile.Preferences.SoundLevelPreference;

public class MainActivity extends Activity {
	// CONSTANTS
	public static final int REQUEST_CODE_NEW_PROFILE = 1;
	public static final int REQUEST_CODE_SETTINGS = 2;

	public static final int APPROVE_NEW_PROFILE = 1;
	public static final int DECLINE_NEW_PROFILE = 2;

	private GestureSelector gestSelect;

	Button buttonScan;

	private TextView TW_currentProfile;

	public static Settings settings;

	// experimental
	private Profile currentProfile;

	public static Profile targetProfile;

	private ArrayList<Integer> desiredPref = new ArrayList<Integer>();

	/* Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// get UI elements
		TW_currentProfile = (TextView) findViewById(R.id.currentProfile);

		settings = new Settings();
		loadSettings();

		currentProfile = new Profile();

		gestSelect = new GestureSelector(this);
		gestSelect.initGestureSensor();
		showProfiles();

	}

	// temporary button, simulates a new connection to WiFi AP
	public void scanButton(View view) {

		targetProfile = settings.getProfile("00:11:22:A8:66:9B");

		// set Brightness activity experiment
		Intent myIntent = new Intent(this, NewprofileActivity.class);

		// Intent myIntent = new Intent(this, NewprofileActivity.class);
		this.startActivityForResult(myIntent, REQUEST_CODE_NEW_PROFILE);

	}

	public void confirmButton(View view) {
		Intent myIntent = new Intent(this, Test_Orientation.class);
		this.startActivity(myIntent);

	}

	public void settingsButton(View view) {
		// settingsProfile = settings.getProfile("00:11:22:A8:66:9B");
		Intent myIntent = new Intent(this, SettingsActivity.class);
		this.startActivityForResult(myIntent, REQUEST_CODE_SETTINGS);
	}

	// Shows the current profile, its desired preferences and a list of all
	// pre-set profiles
	private void showProfiles() {

		gestSelect.clear();

		// Displays current active profile
		TW_currentProfile.setText("" + currentProfile);

		// Displays current preferences of active profile
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.desiredPreferences);
		linearLayout.removeAllViews();
		linearLayout.addView(currentProfile.genPrefButtons(this, desiredPref));
		LinearLayout preferenceButtons = (LinearLayout) ((LinearLayout) findViewById(R.id.desiredPreferences))
				.getChildAt(0);

		if (currentProfile.preferences.size() > 0)
			gestSelect.addChildrenToRow(preferenceButtons);

	
		LinearLayout bottomButtons = (LinearLayout) findViewById(R.id.bottomButtons);
		gestSelect.addChildrenToRow(bottomButtons);

	}

	// Method called when returning from called activity
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == REQUEST_CODE_NEW_PROFILE) {
			switch (resultCode) {
			// if new profile was approved
			case APPROVE_NEW_PROFILE:
				currentProfile = targetProfile;
				desiredPref = data.getIntegerArrayListExtra("PREFERENCES");
				currentProfile.loadPref(desiredPref);
				showProfiles();
				break;

			}
		}
		showProfiles();
	}

	private void loadSettings() {

		// generate fake profiles;
		Profile p1 = new Profile("NETGEAR", "00:13:49:A8:77:4F");
		p1.setName("NETGEAR");
		SoundLevelPreference pref1 = new SoundLevelPreference(0, this);
		p1.addPref(pref1);

		Profile p2 = new Profile("EDUROAM", "00:11:22:A8:66:9B");
		p2.setName("EDUROAM");
		SoundLevelPreference pref2 = new SoundLevelPreference(20, this);
		BrightnessPreference pref3 = new BrightnessPreference(50, this);
		p2.addPref(pref2);
		p2.addPref(pref3);

		Profile p3 = new Profile("WiFi hotspot", "22:31:22:B2:12:46");
		SoundLevelPreference pref4 = new SoundLevelPreference(100, this);
		BrightnessPreference pref5 = new BrightnessPreference(50, this);
		p3.addPref(pref4);
		p3.addPref(pref5);
		p3.setName("WiFi hotspot");

		settings.addProfile(p1);
		settings.addProfile(p2);
		settings.addProfile(p3);

	}

	public Settings getSettings() {
		return settings;
	}

}
