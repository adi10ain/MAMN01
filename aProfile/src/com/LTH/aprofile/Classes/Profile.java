package com.LTH.aprofile.Classes;

import java.util.ArrayList;
import java.util.HashMap;

import com.LTH.aprofile.Preferences.Preference;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Profile {
	
	private String profileName;
	// WiFi identifiers
	private String ESSID; // ex. "NETGEAR"
	private String BSSID; // ex. "00:11:22:33:44:55"

	// CONSTANTS
	public static final int SOUNDLEVEL = 0;
	public static final int BRIGHTNESS = 1;

	// preferences
	public HashMap<Integer, Preference> preferences = new HashMap<Integer, Preference>();

	public Profile() {
		this.profileName = "New profile";
		this.BSSID = "Not set";
		this.ESSID = "Not set";
		
	}

	public Profile(String ESSID, String BSSID) {
		this.profileName = "New profile";
		this.ESSID = ESSID;
		this.BSSID = BSSID;

	}
	
	public void setName(String name) {
		this.profileName = name;
	}

	public void addPref(Preference pref) {
		preferences.put(pref.getType(), pref);
	}

	/*
	 * loads desired preferences only desiredPref is an ArrayList of the
	 * integers, IDs, of the preferences which we want to activate
	 */
	public void loadPref(ArrayList<Integer> desiredPref) {
		for (int pref : desiredPref) {
			Preference p = preferences.get(pref);
			if (p != null)
				p.load();
		}

	}

	// returns a LinearLayout with icons of the profile's preferences
	public LinearLayout genPrefButtons(Activity activity) {
		return genPrefButtons(activity, null);
	}

	// returns a LinearLayout with icons of the profile's preferences with
	// (on/off) visualization
	public LinearLayout genPrefButtons(final Activity activity,
			ArrayList<Integer> desiredPref) {
		LinearLayout ll = new LinearLayout(activity);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		for (Preference p : getPref().values()) {

			Boolean toggle = (desiredPref == null || desiredPref.contains(p
					.getType()));
			ImageView imageView = p.getIconButton(toggle);

			
			imageView.setId(p.getType());
			ll.addView(imageView);

		}

		return ll;
	}

	/*
	 * compares the argument with the BSSID of the profile Returns true if they
	 * are equal
	 */
	public boolean compareBSSID(String BSSID) {
		return (BSSID.equals(this.BSSID));
	}

	public String toString() {
		return profileName;
	}

	public HashMap<Integer, Preference> getPref() {
		return preferences;
	}

}
