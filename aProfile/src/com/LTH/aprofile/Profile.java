package com.LTH.aprofile;

import java.util.ArrayList;
import java.util.HashMap;

import com.LTH.aprofile.Preferences.Preference;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Profile {
	// WiFi identifiers
	private String ESSID; // ex. "NETGEAR"
	private String BSSID; // ex. "00:11:22:33:44:55"

	// CONSTANTS
	public static final int SOUNDLEVEL = 0;
	public static final int BRIGHTNESS = 1;
	
	public static final int ALPHA_BTN = 80; // Opacity for toggled off button (0-255)
	public static final int ALPHA_MAX = 255;

	// preferences
	HashMap<Integer, Preference> preferences = new HashMap<Integer, Preference>();

	public Profile() {
		this.BSSID = "Not set";
		this.ESSID = "Profile not set";
	}

	public Profile(String ESSID, String BSSID) {
		this.ESSID = ESSID;
		this.BSSID = BSSID;

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

	// returns a LinearLayout with icons of the profile's preferences with (on/off) visualization
	public LinearLayout genPrefButtons(Activity activity,
			ArrayList<Integer> desiredPref) {
		LinearLayout A = new LinearLayout(activity);
		A.setOrientation(LinearLayout.HORIZONTAL);
		for (Preference p : getPref().values()) {
			final ImageView imageView = new ImageView(activity);
			imageView.setImageResource(p.getIconResId());

			int alpha = ALPHA_BTN;
			if (desiredPref == null || desiredPref.contains(p.getType()))
				alpha = ALPHA_MAX;

			imageView.setAlpha(alpha);
			
			imageView.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View view) {
	               	int alpha = (imageView.getImageAlpha() == ALPHA_MAX) ? ALPHA_BTN : ALPHA_MAX;
	               	imageView.setAlpha(alpha);
	            }
	        });
			imageView.setId(p.getType());
			A.addView(imageView);

		}

		return A;
	}

	/*
	 * compares the argument with the BSSID of the profile Returns true if they
	 * are equal
	 */
	public boolean compareBSSID(String BSSID) {
		return (BSSID.equals(this.BSSID));
	}

	public String toString() {
		return ESSID;
	}

	public HashMap<Integer, Preference> getPref() {
		return preferences;
	}

}
