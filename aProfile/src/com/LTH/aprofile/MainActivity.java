package com.LTH.aprofile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	WifiManager wifi;
	ListView lv;
	TextView textStatus;
	Button buttonScan;
	int size = 0;
	List<ScanResult> results;

	String ITEM_KEY = "key";
	ArrayList<HashMap<String, String>> arraylist = new ArrayList<HashMap<String, String>>();
	SimpleAdapter adapter;

	SoundMeter soundMeter;
	
	//profiles
	private LinkedList<Profile> profiles = new LinkedList<Profile>();
	private Profile currentProfile;
	private int[] desiredPref = new int[2]; 
	private TextView TW_currentProfile;
	private TextView TW_desiredPref;

	/* Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		textStatus = (TextView) findViewById(R.id.text_status);
		buttonScan = (Button) findViewById(R.id.button_scan);
		buttonScan.setOnClickListener(this);
		lv = (ListView) findViewById(R.id.listView);
		TW_currentProfile = (TextView) findViewById(R.id.currentProfile);
		TW_desiredPref = (TextView) findViewById(R.id.desiredPreferences);

		
		this.adapter = new SimpleAdapter(MainActivity.this, arraylist,
		android.R.layout.simple_list_item_1, new String[] { ITEM_KEY },
		new int[] { android.R.id.text1 });
		lv.setAdapter(this.adapter);
		
		//generate fake profiles;
		Profile p1 = new Profile("NETGEAR","00:13:49:A8:77:4F",10,20);
		Profile p2 = new Profile("EDUROAM","00:11:22:A8:66:9B",0,0);
		Profile p3 = new Profile("Mom use this one","22:31:22:B2:12:46",100,100);
		profiles.add(p1);
		profiles.add(p2);
		profiles.add(p3);
		
		currentProfile = p2;
		desiredPref[0] = 1;
		desiredPref[1] = 0;
		
		
		
		
		showProfiles();

		/* sound Meter
		soundMeter = new SoundMeter((TextView)
		findViewById(R.id.text_status), (ListView)
		findViewById(R.id.listView), this);
		soundMeter.start(); */
	}

	public void onClick(View view)
	// this is a test message
	{
		arraylist.clear();
		wifi.startScan();

		Toast.makeText(this, "Scanning...." + size, Toast.LENGTH_SHORT).show();

		try {
			size = size - 1;
			while (size >= 0) {
				// Toast.makeText(this, "found " + results.get(size).SSID,
				// Toast.LENGTH_SHORT).show();
				HashMap<String, String> item = new HashMap<String, String>();
				item.put(ITEM_KEY,
						results.get(size).SSID + "  " + results.get(size).BSSID);

				arraylist.add(item);
				size--;
				adapter.notifyDataSetChanged();
			}
		} catch (Exception e) {
		}
	}
	
	// Shows the current profile, its desired preferences and a list of all pre-set profiles 
	private void showProfiles() {
		
		if (profiles.size() > 0) {
			//show current profile
			if (currentProfile != null) {
				TW_currentProfile.setText(""+currentProfile);
				
				// iterate through all desired preferences
				String prefRow = "";
				for (int i = 0; i < desiredPref.length; i++)
					prefRow += desiredPref[i] + " ";
				
				TW_desiredPref.setText(prefRow);
			}
			
			//show a list of all profiles
			for (int i = 0; i < profiles.size(); i++) {
				HashMap<String, String> item = new HashMap<String, String>();
				item.put(ITEM_KEY, ""+profiles.get(i));
				arraylist.add(item);
				adapter.notifyDataSetChanged();
			}
			
		//user has no profiles
		} else {
			HashMap<String, String> item = new HashMap<String, String>();
			item.put(ITEM_KEY, "Click here to add new profiles");
			arraylist.add(item);
			adapter.notifyDataSetChanged();
			
		}
	}
	
	private void initiateWIFI() {
		wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		if (wifi.isWifiEnabled() == false) {
			Toast.makeText(getApplicationContext(),
					"wifi is disabled..making it enabled", Toast.LENGTH_LONG)
					.show();
			wifi.setWifiEnabled(true);
		}

	

		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context c, Intent intent) {
				results = wifi.getScanResults();
				size = results.size();
			}
		}, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		
	}

	private void generateFakeWIFIlist() {

		HashMap<String, String> item = new HashMap<String, String>();
		item.put(ITEM_KEY, "NETGEAR;00:13:49:A8:77:4F");
		arraylist.add(item);
		adapter.notifyDataSetChanged();

		item = new HashMap<String, String>();
		item.put(ITEM_KEY, "EDUROAM;00:11:22:A8:66:9B");
		arraylist.add(item);
		adapter.notifyDataSetChanged();
		
		item = new HashMap<String, String>();
		item.put(ITEM_KEY, "Mom use this one;22:31:22:B2:12:46");
		arraylist.add(item);

		adapter.notifyDataSetChanged();

	}
}
