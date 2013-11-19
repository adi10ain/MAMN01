package com.LTH.aprofile.Classes;

import java.util.ArrayList;
import java.util.Iterator;

public class Settings {
	private ArrayList<Profile> profiles;

	public Settings() {
		profiles = new ArrayList<Profile>();
	}

	// adds profile
	public void addProfile(Profile profile) {
		profiles.add(profile);
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
	
	//returns iterator of all profiles
	public Iterator<Profile> getProfiles() {
		return profiles.iterator();
	}
}
