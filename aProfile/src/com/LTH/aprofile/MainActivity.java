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
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.LTH.aprofile.Preferences.BrightnessPreference;
import com.LTH.aprofile.Preferences.SoundLevelPreference;

public class MainActivity extends Activity implements OnClickListener {
	// CONSTANTS
	public static final int REQUEST_CODE_NEW_PROFILE = 1;

	public static final int APPROVE_NEW_PROFILE = 1;
	public static final int DECLINE_NEW_PROFILE = 2;

	WifiManager wifi;
	ListView lv;
	Button buttonScan;
	int size = 0;
	List<ScanResult> results;

	String ITEM_KEY = "key";
	ArrayList<HashMap<String, String>> arraylist = new ArrayList<HashMap<String, String>>();
	SimpleAdapter adapter;

	private TextView TW_currentProfile;

	private Settings settings;

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
		buttonScan = (Button) findViewById(R.id.button_scan);
		buttonScan.setOnClickListener(this);
		lv = (ListView) findViewById(R.id.listView);
		TW_currentProfile = (TextView) findViewById(R.id.currentProfile);

		// adapter for making the WiFi list
		this.adapter = new SimpleAdapter(MainActivity.this, arraylist,
				android.R.layout.simple_list_item_1, new String[] { ITEM_KEY },
				new int[] { android.R.id.text1 });
		lv.setAdapter(this.adapter);
		
		settings = new Settings();
		loadSettings();

		currentProfile = new Profile();

		showProfiles();
	}

	// temporary button, simulates a new connection to WiFi AP
	public void onClick(View view) {

		targetProfile = settings.getProfile("00:11:22:A8:66:9B");

		//set Brightness activity experiment
		Intent myIntent = new Intent(this, NewprofileActivity.class);
		
		
		//Intent myIntent = new Intent(this, NewprofileActivity.class);
		this.startActivityForResult(myIntent, REQUEST_CODE_NEW_PROFILE);

	}

	// Shows the current profile, its desired preferences and a list of all
	// pre-set profiles
	private void showProfiles() {

		// Displays current active profile
		TW_currentProfile.setText("" + currentProfile);

		// Displays current preferences of active profile
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.desiredPreferences);
		linearLayout.removeAllViews();
		linearLayout.addView(currentProfile.genPrefButtons(this, desiredPref));

		// Clear old wifi-profiles list
		arraylist.clear();

		// Displays all profiles in the wifi-profiles list
		Iterator<Profile> profiles = settings.getProfiles();
		while (profiles.hasNext()) {
			Profile profile = profiles.next();
			HashMap<String, String> item = new HashMap<String, String>();
			item.put(ITEM_KEY, "" + profile);
			arraylist.add(item);
			adapter.notifyDataSetChanged();

		}
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
	}

	private void loadSettings() {
		
		
		// generate fake profiles;
		Profile p1 = new Profile("NETGEAR", "00:13:49:A8:77:4F");
		SoundLevelPreference pref1 = new SoundLevelPreference(0, this);
		p1.addPref(pref1);

		Profile p2 = new Profile("EDUROAM", "00:11:22:A8:66:9B");
		SoundLevelPreference pref2 = new SoundLevelPreference(20, this);
		BrightnessPreference pref3 = new BrightnessPreference(120, this);
		p2.addPref(pref2);
		p2.addPref(pref3);

		Profile p3 = new Profile("Mom use this one", "22:31:22:B2:12:46");
		
		//kommentar
		///hej hej hej
		//sdfsdfsd

		settings.addProfile(p1);
		settings.addProfile(p2);
		settings.addProfile(p3);
		//Eriks test
	}

}
