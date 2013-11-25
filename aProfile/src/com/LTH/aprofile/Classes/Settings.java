package com.LTH.aprofile.Classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.util.Log;

public class Settings {
	private ArrayList<Profile> profiles;

	private HashMap<WiFiHotspot, Profile> AP_Profile_link;
	private HashMap<int[], Profile> Gesture_Profile_link;

	private Boolean GestureSensorToggleOn;

	public Settings() {
		profiles = new ArrayList<Profile>();
		AP_Profile_link = new HashMap<WiFiHotspot, Profile>();
		Gesture_Profile_link = new HashMap<int[], Profile>();
		GestureSensorToggleOn = false;
	}

	// adds profile
	public void addProfile(Profile profile) {
		profiles.add(profile);
	}

	public Profile checkIfLinkedWifi(WiFiHotspot wifi) {
		return AP_Profile_link.get(wifi);
	}

	public boolean addWifiProfileLink(WiFiHotspot wifi, Profile p) {
		Boolean ret = false;
		if (!AP_Profile_link.containsKey(wifi)) {
			AP_Profile_link.put(wifi, p);
			ret = true;
		}
		return ret;
	}
	
	public Boolean getGestureToggle() {
		return GestureSensorToggleOn;
	}

	public void setGestureToggle(Boolean b) {
		GestureSensorToggleOn = b;
	}
	// deletes profile
	public void delProfile(Profile profile) {
		profiles.remove(profile);
	}

	// returns a Profile with specific BSSID, returns null if not found
	public Profile getProfile(String BSSID) {

		Profile ret = null;
		Iterator<Profile> it = profiles.iterator();

		while (it.hasNext()) {
			Profile p = (Profile) it.next();
			if (p.compareBSSID(BSSID)) {
				ret = p;
				break;
			}
		}

		return ret;
	}

	// returns iterator of all profiles
	public Iterator<Profile> getProfiles() {
		return profiles.iterator();
	}
}
