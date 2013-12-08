package com.LTH.aprofile.Classes;

import java.util.ArrayList;
import java.util.HashMap;

import com.LTH.aprofile.Classes.Preferences.BrightnessPreference;
import com.LTH.aprofile.Classes.Preferences.Preference;
import com.LTH.aprofile.Classes.Preferences.SoundLevelPreference;
import com.LTH.aprofile.Classes.Preferences.VibrationPreference;



import android.app.Activity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Profile {

	private String profileName;

	// CONSTANTS
	public static final int SOUNDLEVEL = 0;
	public static final int BRIGHTNESS = 1;
	public static final int VIBRATION = 2;

	// preferences
	public HashMap<Integer, Preference> preferences;

	ArrayList<WiFiHotspot> hotspots;

	public Profile() {
		this.profileName = "New profile";
		hotspots = new ArrayList<WiFiHotspot>();
		hotspots = new ArrayList<WiFiHotspot>();
		preferences = new HashMap<Integer, Preference>();

	}

	public Boolean addHotspot(WiFiHotspot w) {
		Boolean ret = false;
		if (!hotspots.contains(w)) {
			hotspots.add(w);
			ret = true;
		}
		return ret;
	}
	
	public WiFiHotspot removeHotspotIndex(int index){
		return hotspots.remove(index);
	}

	public ArrayList<String> getHotSpotNames(){
		ArrayList<String> hotspotNames = new ArrayList<String>();
		for(int i=0; i<hotspots.size(); i++){
			hotspotNames.add(hotspots.get(i).getName());
		}
		return hotspotNames;
	}
	
	public ArrayList<WiFiHotspot> getHotspots() {
		return hotspots;
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
	 * compares the argument with the current linked WiFi hotspots to this
	 * profile, Returns true if the hotspot already exists
	 */
	public boolean containsHotspot(WiFiHotspot ap) {
		return (hotspots.contains(ap));
	}

	@Override
	public String toString() {
		return profileName;
	}

	// returns a string representation of a Profile
	public String profileToString() {
		String profileString = profileName;

		for (Preference pref : getPref().values())
			profileString += pref; // adds each pref to the reutrn string

		return profileString;
	}

	// returns a Profile of a Profile string representation
	public static Profile profileFromString(String profileString,
			Activity activity) {

		String[] separatedValues = profileString.split(";");
		Profile profile = new Profile();
		profile.setName(separatedValues[0]); // name

		Preference preference = null;

		for (int i = 1; i < separatedValues.length; i++) {
			String[] pref = separatedValues[i].split(":");
			int type = Integer.parseInt(pref[0]);
			int prefValue = Integer.parseInt(pref[1]);
			switch (type) {
			case Profile.BRIGHTNESS:
				preference = new BrightnessPreference(prefValue, activity);
				break;
			case Profile.SOUNDLEVEL:
				preference = new SoundLevelPreference(prefValue, activity);
				break;
			case Profile.VIBRATION:
				preference = new VibrationPreference(prefValue, activity);
				break;

			}

			profile.addPref(preference);
		}

		return profile;
	}

	public HashMap<Integer, Preference> getPref() {
		return preferences;
	}

}
