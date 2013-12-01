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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import android.widget.TextView;

public final class EditProfileActivity extends Activity {

	private Profile profile;
	private EditText profileName;

	private TextView wifiList;
	public Button btn_addWIFI;

	private WifiReceiver wifiReceiver;
	public static WiFiHotspot connectedWiFi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_profile);

		profile = SettingsActivity.selectedProfile;
		wifiReceiver = new WifiReceiver();
		this.registerReceiver(wifiReceiver, new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION));

		profileName = (EditText) findViewById(R.id.profileName);
		profileName.setText("" + profile);

		btn_addWIFI = (Button) findViewById(R.id.addWIFI);

		wifiList = (TextView) findViewById(R.id.wifiList);

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

		showWiFiList();

	}

	public void btn_addWiFiHotspot(View view) {
		if (connectedWiFi != null
				&& MainActivity.settings.addWifiProfileLink(connectedWiFi,
						profile))
			profile.addHotspot(connectedWiFi);
		showWiFiList();
	}

	private void showWiFiList() {
		String s = "";

		for (WiFiHotspot w : profile.getHotspots()) {
			s += " " + w.getName() + " ";
		}
		wifiList.setText(s);
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
