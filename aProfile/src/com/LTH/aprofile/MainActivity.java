package com.LTH.aprofile;

import java.util.ArrayList;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.LTH.aprofile.Classes.Profile;
import com.LTH.aprofile.Classes.ProfileExchanger;
import com.LTH.aprofile.Classes.Settings;
import com.LTH.aprofile.Classes.SoundMeter;
import com.LTH.aprofile.Classes.WiFiHotspot;
import com.LTH.aprofile.Classes.Sensors.GestureActivity;
import com.LTH.aprofile.Classes.Sensors.GestureSelector;
import com.LTH.aprofile.GUI.EditSettings;
import com.LTH.aprofile.GUI.EditSettingsConnected;
import com.LTH.aprofile.Preferences.BrightnessPreference;
import com.LTH.aprofile.Preferences.Preference;
import com.LTH.aprofile.Preferences.SoundLevelPreference;
import com.LTH.aprofile.Preferences.VibrationPreference;

public class MainActivity extends GestureActivity {
	// SoundMeter
	private SoundMeter mSensor;
	private Handler mHandler;
	private static final int POLL_INTERVAL = 300;
	private int mTickCount = 0;
	private boolean lock = false;

	private Runnable mPollTask = new Runnable() {
		public void run() {
			double amp = mSensor.getAmplitudeEMA();
		//	Log.d("SoundMeter", ""+ amp);
			if (mSensor.isKnock(amp)) {
				mTickCount++;
			} else {
				mTickCount = 0;
			}
			if (mTickCount >= 3) {
				unlockScreen();
			}
			mHandler.postDelayed(mPollTask, POLL_INTERVAL);
		}

	};

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
	public static Profile currentProfile;

	public static Profile targetProfile;

	private ArrayList<Integer> desiredPref = new ArrayList<Integer>();

	private WifiReceiver wifiReceiver;

	private ProfileExchanger profileExchanger;

	/* Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		profileExchanger = new ProfileExchanger(this);
		setGestureUpdateInterval(1000);
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

		// temporary disabled wifi to check functionality of ProfileExchanger

		// wifiReceiver = new WifiReceiver();
		// this.registerReceiver(wifiReceiver, new IntentFilter(
		// ConnectivityManager.CONNECTIVITY_ACTION));
		showProfiles();
		
		//SoundMeter
		mSensor = new SoundMeter();
		mHandler = new Handler();
		
		//temporary

		WiFiHotspot eduroam = new WiFiHotspot("Eduroam", "00:11:22:A8:66:9B");
		currentProfile = settings.getProfile(eduroam);
		
		LinearLayout screenLayout = (LinearLayout) findViewById(R.id.linearLayout1);
		
		EditSettingsConnected settingsPanel = new EditSettingsConnected(this, currentProfile);
		screenLayout.addView(settingsPanel.getSettingsPanel());
		


	}

	// temporary button, simulates a new connection to WiFi AP
	public void scanButton(View view) {

		WiFiHotspot eduroam = new WiFiHotspot("Eduroam", "00:11:22:A8:66:9B");
		targetProfile = settings.getProfile(eduroam);

		newProfileConnected();

	}

	public void newProfileConnected() {
		VibrationPreference
				.vibrate(VibrationPreference.VIBRATE_PROFILE_CONNECTED);
		Intent myIntent = new Intent(this, ConnectProfileActivity.class);
		this.startActivityForResult(myIntent, REQUEST_CODE_NEW_PROFILE);
	}

	public void confirmButton(View view) {
		Intent myIntent = new Intent(this, SettingsChartActivity.class);
		this.startActivity(myIntent);

	}



	public void settingsButton(View view) {
		// settingsProfile = settings.getProfile("00:11:22:A8:66:9B");
		Intent myIntent = new Intent(this, SettingsActivity.class);
		this.startActivityForResult(myIntent, REQUEST_CODE_SETTINGS);
	}

	public void shareProfileButton(View view) {
		profileExchanger.sendBroadcastRequest();
	}

	// Shows the current profile, its desired preferences and a list of all
	// pre-set profiles
	private void showProfiles() {

		gestSelect.clear();

		// Displays current active profile
		TW_currentProfile.setText("" + currentProfile);

		// Displays current preferences of active profile

		// linearLayout.addView(currentProfile.genPrefButtons(this,
		// desiredPref));
		// LinearLayout preferenceButtons = (LinearLayout) ((LinearLayout)
		// findViewById(R.id.desiredPreferences))
		// .getChildAt(0);

		// if (currentProfile.preferences.size() > 0)
		// gestSelect.addChildrenToRow(preferenceButtons);

		LinearLayout bottomButtons = (LinearLayout) findViewById(R.id.bottomButtons);
		gestSelect.addChildrenToRow(bottomButtons);


	}

	// Method called when returning from called activity
	@Override
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

		Profile p1 = new Profile();
		p1.setName("EDUROAM");

		WiFiHotspot eduroam = new WiFiHotspot("Eduroam", "00:11:22:A8:66:9B");
		p1.addHotspot(eduroam);

		SoundLevelPreference pref2 = new SoundLevelPreference(20, this);
		BrightnessPreference pref3 = new BrightnessPreference(50, this);
		VibrationPreference pref4 = new VibrationPreference(0, this);
		p1.addPref(pref2);
		p1.addPref(pref3);
		p1.addPref(pref4);

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

	protected void onPause() {
		super.onPause();
		start();
		lock = true;

	}

	protected void onResume() {
		super.onResume();
		 stop();
		// mSensor.calibrate();
		lock = false;

	}

	public void start() {
		mSensor.startRec();
		mHandler.postDelayed(mPollTask, POLL_INTERVAL);
		//mSensor.calibrate();
	}

	public void stop() {

		mHandler.removeCallbacks(mPollTask);
		mSensor.stopRec();

	}

	private void unlockScreen() {
		  Log.d("dialog", "trying to unlock");
	      Window wind = this.getWindow();
	      if(lock){
	   // 	  Log.d("SoundMeter", "unlocking screen now");
		    wind.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		    wind.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		    wind.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		   //ful lösning, kommer att fixa till.
		    PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
            WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
            wakeLock.acquire();
            KeyguardManager keyguardManager = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE); 
            KeyguardLock keyguardLock =  keyguardManager.newKeyguardLock("TAG");
            keyguardLock.disableKeyguard();
	      } else {
	    	//  Log.d("SoundMeter", "Lock is off");
	   	    wind.clearFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
	   	    wind.clearFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
	   	    wind.clearFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
         
	      }
	}

}
