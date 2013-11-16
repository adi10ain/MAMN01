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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
	//CONSTANTS
	private static final int REQUEST_CODE_NEW_PROFILE = 1;
	
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
	private TextView TW_desiredPref;
	
	private Settings settings;
	
	//experimental
	private Profile currentProfile;
	private int[] desiredPref = new int[2]; 
	
	public static Profile targetProfile;
	

	/* Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//get UI elements
		buttonScan = (Button) findViewById(R.id.button_scan);
		buttonScan.setOnClickListener(this);
		lv = (ListView) findViewById(R.id.listView);
		TW_currentProfile = (TextView) findViewById(R.id.currentProfile);
		TW_desiredPref = (TextView) findViewById(R.id.desiredPreferences);

		//adapter for making list
		this.adapter = new SimpleAdapter(MainActivity.this, arraylist,
		android.R.layout.simple_list_item_1, new String[] { ITEM_KEY },
		new int[] { android.R.id.text1 });
		lv.setAdapter(this.adapter);
		
		//generate fake profiles;
		Profile p1 = new Profile("NETGEAR","00:13:49:A8:77:4F",10,20, this);
		Profile p2 = new Profile("EDUROAM","00:11:22:A8:66:9B",0,0, this);
		Profile p3 = new Profile("Mom use this one","22:31:22:B2:12:46",100,100, this);

		settings = new Settings();
		
		settings.addProfile(p1);
		settings.addProfile(p2);
		settings.addProfile(p3);
		
		currentProfile = new Profile();
		
		showProfiles();
	}

	
	//temporary button, simulates a new connection to WiFi AP
	public void onClick(View view)
	{
		
		targetProfile = settings.getProfile("00:11:22:A8:66:9B");

		Intent myIntent = new Intent(this, NewprofileActivity.class);
		this.startActivityForResult(myIntent, REQUEST_CODE_NEW_PROFILE);
		
	}
	
		
		
	// Shows the current profile, its desired preferences and a list of all pre-set profiles 
	private void showProfiles() {
		
		//Displays current active profile
		TW_currentProfile.setText(""+currentProfile);
		
		//Displays current preferences of active profile
		String prefRow = "";
		for (int i = 0; i < desiredPref.length; i++)
			prefRow += desiredPref[i] + " ";
		
		TW_desiredPref.setText(prefRow);
		
		
		//Clear old list
		arraylist.clear();
		
		
		//Displays all profiles in a list
		Iterator<Profile> profiles = settings.getProfiles();
		while(profiles.hasNext()) {
			Profile profile = profiles.next();
			HashMap<String, String> item = new HashMap<String, String>();
			item.put(ITEM_KEY, ""+profile);
			arraylist.add(item);
			adapter.notifyDataSetChanged();
			
		}
	}
	
	
	//Method when returned from called activity
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	   
	    if (requestCode == REQUEST_CODE_NEW_PROFILE) {
	    	switch (resultCode) {
	    	//if new profile was approved
	    	case APPROVE_NEW_PROFILE: 
	    		currentProfile = targetProfile;
	    		desiredPref = data.getIntArrayExtra("PREFERENCES");
	    		currentProfile.loadPref(desiredPref);
	    		showProfiles();
	    		break;
	    	
	    	}
	    }
	}



}
