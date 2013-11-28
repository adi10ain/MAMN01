package com.LTH.aprofile;

import java.util.HashMap;

import com.LTH.aprofile.Classes.Profile;
import com.LTH.aprofile.Classes.WiFiHotspot;
import com.LTH.aprofile.Preferences.Preference;

import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;

public class EditProfileActivity extends Activity {

	HashMap<View, TextView> barStatusMap;

	private Profile profile;
	private EditText profileName;

	private LinearLayout statusPanel;
	private LinearLayout touchPanel;
	private TextView wifiList;
	public Button btn_addWIFI;

	private WifiReceiver wifiReceiver;
	public static WiFiHotspot connectedWiFi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_profile);

		barStatusMap = new HashMap<View, TextView>();

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

		statusPanel = (LinearLayout) findViewById(R.id.statusPanel);
		touchPanel = (LinearLayout) findViewById(R.id.touchPanel);

		generateBars();
		showWiFiList();
		
		setTouchListener(touchPanel, this);

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

	private void generateBars() {
		View lastView = new View(this);
		for (Preference p : profile.getPref().values()) {
			// add status bar
			LinearLayout col = new LinearLayout(this);
			col.setOrientation(LinearLayout.VERTICAL);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.MATCH_PARENT, 1f);
			col.setLayoutParams(layoutParams);
			col.setId(p.getType());

			layoutParams = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);

			TextView tv_status = new TextView(this);
			tv_status.setLayoutParams(layoutParams);

			layoutParams = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);

			TextView tv_colorBar = new TextView(this);
			tv_colorBar.setLayoutParams(layoutParams);
			tv_colorBar.setBackgroundColor(p.getColorCode());

			col.addView(tv_status);
			col.addView(tv_colorBar);

			statusPanel.addView(col);

			// add icon
			ImageView icon = new ImageView(this);
			icon.setImageResource(p.getIconResId());
			layoutParams = new LinearLayout.LayoutParams(0,
					LayoutParams.MATCH_PARENT, 1f);
			icon.setLayoutParams(layoutParams);
			icon.setBackgroundColor(p.getColorCode());
			icon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			int padding = 50;
			icon.setPadding(padding, padding, padding, padding);
			icon.getBackground().setAlpha(150);

			touchPanel.addView(icon);

			barStatusMap.put(col, tv_status);
			lastView = col;
			
			

		}

		// show user preferences when the UI have loaded
		lastView.post(new Runnable() {

			@Override
			public void run() {

				for (View col : barStatusMap.keySet()) {
					Preference p = profile.getPref().get(col.getId());
					preferenceChanged(col, p.getPrefValue());
				}

			}

		});

	}

	// used separate method to make main method cleaner, only supposed to be
	// used with touchPanel
	private void setTouchListener(final LinearLayout linearLayout,
			final Activity activity) {
		linearLayout.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent me) {
				for (View col : barStatusMap.keySet()) {

					Rect r = new Rect(col.getLeft(), col.getTop(), col
							.getRight(), col.getBottom());

					// only consider x-axis to determine which button was
					// touched
					if (r.contains((int) me.getX(), 0)) {

						int touchPanelHeight = linearLayout.getHeight();

						float targetValue = (int) me.getY();
						if (targetValue < 0)
							targetValue = 0;
						if (targetValue > touchPanelHeight)
							targetValue = touchPanelHeight;
						Log.d("test", "touchpanel height " + touchPanelHeight
								+ " - " + targetValue);
						targetValue = 100 - (targetValue / touchPanelHeight) * 100;
						Log.d("test", "touchpane2l height " + touchPanelHeight
								+ " - " + targetValue);
						preferenceChanged(col, targetValue);

						break;
					}

				}

				return true;
			}
		});

	}

	// targetValue should be between 0 and 100
	private void preferenceChanged(View col, float targetValue) {

		TextView statusChanger = barStatusMap.get(col);
		targetValue = (targetValue <= 0) ? 1 : targetValue;

		int height = ((int) (((100 - targetValue) / 100) * col.getHeight()));

		Log.d("height ", "height :" + col.getHeight() + "targetValue"
				+ targetValue);

		statusChanger.setHeight(height);

		int prefId = col.getId();
		Preference pref = profile.getPref().get(prefId);
		pref.set((int) targetValue, this);
		pref.setPrefValue((int) targetValue);

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
