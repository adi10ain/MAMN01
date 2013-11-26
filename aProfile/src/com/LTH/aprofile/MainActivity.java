package com.LTH.aprofile;

import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.LTH.aprofile.Classes.GestureActivity;
import com.LTH.aprofile.Classes.GestureSelector;
import com.LTH.aprofile.Classes.Profile;
import com.LTH.aprofile.Classes.Settings;
import com.LTH.aprofile.Classes.WiFiHotspot;
import com.LTH.aprofile.Preferences.BrightnessPreference;
import com.LTH.aprofile.Preferences.Preference;
import com.LTH.aprofile.Preferences.SoundLevelPreference;

public class MainActivity extends GestureActivity {
	
	
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

	private WifiReceiver wifiReceiver;

	/* Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setGestureUpdateInterval(100);
		addListenForGesture(GESTURE_UP);
		addListenForGesture(GESTURE_DOWN);
		addListenForGesture(GESTURE_RIGHT);
		addListenForGesture(GESTURE_LEFT);
		gestureSensor.initiate();
		
		setContentView(R.layout.activity_main);

		// get UI elements
		TW_currentProfile = (TextView) findViewById(R.id.currentProfile);

		settings = new Settings();
		loadSettings();

		currentProfile = new Profile();

		gestSelect = new GestureSelector(this);

		wifiReceiver = new WifiReceiver();
		this.registerReceiver(wifiReceiver, new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION));
		showProfiles();

	}

	// temporary button, simulates a new connection to WiFi AP
	public void scanButton(View view) {

		targetProfile = settings.getProfile("00:11:22:A8:66:9B");

		newProfileConnected();

	}

	public void newProfileConnected() {

		Intent myIntent = new Intent(this, NewprofileActivity.class);
		this.startActivityForResult(myIntent, REQUEST_CODE_NEW_PROFILE);
	}

	public void confirmButton(View view) {
		Intent myIntent = new Intent(this, Test_Orientation.class);
		this.startActivity(myIntent);

	}

	private void showSettingsButtons() {
		for (Preference p : currentProfile.getPref().values()) {
			LinearLayout settingsBar = (LinearLayout) findViewById(R.id.desiredPreferences);
			ImageView icon = new ImageView(this);
			icon.setImageResource(p.getIconResId());
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					10, LinearLayout.LayoutParams.MATCH_PARENT, 1f);
			icon.setLayoutParams(layoutParams);
			// icon.setBackgroundColor(p.getColorCode());
			icon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			int padding = 25;
			icon.setPadding(padding, padding, padding, padding);
			// icon.getBackground().setAlpha(150);
			settingsBar.addView(icon);

		}
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
		// linearLayout.addView(currentProfile.genPrefButtons(this,
		// desiredPref));
		// LinearLayout preferenceButtons = (LinearLayout) ((LinearLayout)
		// findViewById(R.id.desiredPreferences))
		// .getChildAt(0);

		// if (currentProfile.preferences.size() > 0)
		// gestSelect.addChildrenToRow(preferenceButtons);

		LinearLayout bottomButtons = (LinearLayout) findViewById(R.id.bottomButtons);
		gestSelect.addChildrenToRow(bottomButtons);
		showSettingsButtons();

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

		Profile p1 = new Profile("EDUROAM", "00:11:22:A8:66:9B");
		p1.setName("EDUROAM");
		SoundLevelPreference pref2 = new SoundLevelPreference(20, this);
		BrightnessPreference pref3 = new BrightnessPreference(50, this);
		p1.addPref(pref2);
		p1.addPref(pref3);



		settings.addProfile(p1);
	

	}

	public Settings getSettings() {
		return settings;
	}

	class WifiReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			WifiManager wifi = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			WifiInfo wifiInfo = wifi.getConnectionInfo();
			if (wifi.isWifiEnabled() == true) {
				WiFiHotspot hotspot = new WiFiHotspot(wifiInfo.getSSID(),
						wifiInfo.getBSSID());

				// check if the new wifi connection is associated with a profile
				Profile p = settings.checkIfLinkedWifi(hotspot);
				if (p != null) {
					targetProfile = p;
					newProfileConnected();
				}
				Log.d("debug", "wifi connected");
			} else {
				Log.d("debug", "wifi disconnected");
			}

		}
	}

	@Override
	public void onGesture(int gesture) {
		gestSelect.onGesture(gesture);
		
	};

}
