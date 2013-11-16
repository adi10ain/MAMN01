package com.LTH.aprofile;

import android.content.Context;
import android.media.AudioManager;

public class Profile {
	// WiFi identifiers
	private String ESSID; //ex. "NETGEAR"
	private String BSSID; //ex. "00:11:22:33:44:55"
	
	//CONSTANTS
	public static final int SOUNDLEVEL = 0;
	public static final int BRIGHTNESS = 1;
		
	// preferences
	private int preferences[] = new int[2];
	
	//managers
	private Context callingContext;
	private AudioManager audioManager;
	
	public Profile() {
		this.BSSID = "Not set";
		this.ESSID = "Profile not set";
	}
	
	public Profile(String ESSID, String BSSID, int soundLevel, int brightness, Context callingContext) {
		this.ESSID = ESSID;
		this.BSSID = BSSID;
		preferences[SOUNDLEVEL] = soundLevel;
		preferences[BRIGHTNESS] = brightness;
		
		this.callingContext = callingContext;		
		audioManager = (AudioManager)callingContext.getSystemService(Context.AUDIO_SERVICE);

		
		
	}
	
	//loads all preferences
	public void loadPref() {
		int[] array = new int[preferences.length];
		
		for (int i = 0; i < preferences.length; i++)
			array[i] = 1;
		
		loadPref(array);
		
	}
	
	/*loads desired preferences only
	 * desiredPref, preferences which we want to activate should be represented by value 1
	 * "preferences[1] = 1" means we want to activate the preferences for BRIGHTNESS
	 * */
	public void loadPref(int[] desiredPref) {
		for (int i = 0; i < desiredPref.length; i++) {
			if (desiredPref[i] == 1)
				loadSpecPref(i);
		}
		
	}
	
	//loads a specific preference
	private void loadSpecPref(int identifier) {
		switch (identifier) {
			case SOUNDLEVEL: System.out.print("SOUND - target "+preferences[SOUNDLEVEL]);
			setSound(preferences[SOUNDLEVEL]);
			break;
			
			case BRIGHTNESS: System.out.print("BRIGHTNESS - target "+preferences[BRIGHTNESS]);
			break;
		
		
		}
		
	}
	
	//sets ring sound
	private void setSound(int level) {
		audioManager.setStreamVolume(AudioManager.STREAM_RING, level, AudioManager.FLAG_ALLOW_RINGER_MODES|AudioManager.FLAG_PLAY_SOUND);
	}
	
	/*compares the argument with the BSSID of the profile
	Returns true if they are equal
	*/
	public boolean compareBSSID(String BSSID) {
		return (BSSID.equals(this.BSSID));
	}
	
	public String toString() {
		return ESSID;
	}
	
	public int[] getPref() {
		return preferences;
	}
	

}
