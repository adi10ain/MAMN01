package com.LTH.aprofile;

import com.LTH.aprofile.Classes.Profile;
import com.LTH.aprofile.Classes.WiFiHotspot;
import com.LTH.aprofile.GUI.EditSettings;

import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import android.widget.TextView;

public final class EditProfileActivity extends Activity {

	private Profile profile;
	private EditText profileName;

	// private TextView wifiList;
	public Button btn_addWIFI;
	public Button btn_removeWifi;
	public Spinner spinner2;

	private WifiReceiver wifiReceiver;
	public static WiFiHotspot connectedWiFi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// override activity transition animation to slide in and out from left

		setContentView(R.layout.activity_edit_profile);

		// get profile to be edited
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			int profIndex = extras.getInt("PROFILE_TO_EDIT");
			profile = MainActivity.settings.getProfiles().get(profIndex);
		} else {
			profile = new Profile();
			profile.setName("ERROR");
		}

		wifiReceiver = new WifiReceiver();
		this.registerReceiver(wifiReceiver, new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION));

		profileName = (EditText) findViewById(R.id.profileName);
		profileName.setText("" + profile);
		addItemsOnSpinner2();
		addListenerOnButton();

		btn_addWIFI = (Button) findViewById(R.id.addWIFI);

		// wifiList = (TextView) findViewById(R.id.wifiList);

		profileName.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				profile.setName("" + s);
			}
		});

		LinearLayout screenLayout = (LinearLayout) findViewById(R.id.screenLayout);

		EditSettings settingsPanel = new EditSettings(this, profile);
		screenLayout.addView(settingsPanel.getSettingsPanel());

		// showWiFiList();

	}

	public void btn_addWiFiHotspot(View view) {
		if (connectedWiFi != null
				&& MainActivity.settings.addWifiProfileLink(connectedWiFi,
						profile)) {
			profile.addHotspot(connectedWiFi);
			MainActivity.settings.addWifiProfileLink(connectedWiFi, profile);
			this.addItemsOnSpinner2();
			Toast.makeText(
					EditProfileActivity.this,
					"Network " + connectedWiFi.getName()
							+ " has been added to the profile",
					Toast.LENGTH_SHORT).show();

		}
		// showWiFiList();
	}

	// private void showWiFiList() {
	// String s = "";
	//
	// for (WiFiHotspot w : profile.getHotspots()) {
	// s += " " + w.getName() + " ";
	// }
	// wifiList.setText(s);
	// }

	// remove all field text if profile name is "New profile."
	public void OnClick_profileName(View view) {
		EditText et = (EditText) view;
		if (profile.toString().equals("New profile")) {
			et.setText("");
		}

	}

	public void addItemsOnSpinner2() {

		spinner2 = (Spinner) findViewById(R.id.spinner2);

		// lagg till massa networks har
		ArrayAdapter dataAdapter = new ArrayAdapter(this,
				android.R.layout.simple_spinner_item, profile.getHotSpotNames());
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner2.setAdapter(dataAdapter);

	}

	public void addListenerOnButton() {

		spinner2 = (Spinner) findViewById(R.id.spinner2);
		btn_removeWifi = (Button) findViewById(R.id.removeWiFi);
		btn_removeWifi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (spinner2.getCount() != 0) {
					int index = spinner2.getSelectedItemPosition();
					WiFiHotspot deletedHotspot = profile
							.removeHotspotIndex(index);
					MainActivity.settings.removeWiFiHotspot(deletedHotspot);
					addItemsOnSpinner2();

					Toast.makeText(
							EditProfileActivity.this,
							"Network " + deletedHotspot.getName()
									+ " has been removed from the profile",
							Toast.LENGTH_SHORT).show();
				}
				return;
			}

		});
	}

}

class WifiReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifi.getConnectionInfo();
		if (wifi.isWifiEnabled() == true) {

			EditProfileActivity.connectedWiFi = new WiFiHotspot(
					wifiInfo.getSSID(), wifiInfo.getBSSID());
			((EditProfileActivity) context).btn_addWIFI.setClickable(true);
			Log.d("debug", "connect");
		} else {
			((EditProfileActivity) context).btn_addWIFI.setClickable(false);
			EditProfileActivity.connectedWiFi = null;
			Log.d("debug", "disconnect");
		}

	}

};
