package com.LTH.aprofile;

public class Profile {
	// WiFi identifiers
	private String ESSID; //ex. "NETGEAR"
	private String BSSID; //ex. "00:11:22:33:44:55"
	
	//CONSTANTS
	private final int SOUNDLEVEL = 0;
	private final int BRIGHTNESS = 1;
		
	// preferences
	private int preferences[] = new int[2];
	
	
	public Profile(String ESSID, String BSSID, int soundLevel, int brightness) {
		this.ESSID = ESSID;
		this.BSSID = BSSID;
		preferences[SOUNDLEVEL] = soundLevel;
		preferences[BRIGHTNESS] = brightness;
		
	}
	
	//loads all preferences
	public void loadPref() {
		int[] array = new int[preferences.length];
		
		for (int i = 0; i < preferences.length; i++)
			array[i] = 1;
		
		loadPref(array);
		
	}
	
	/*loads chosen preferences
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
			break;
			
			case BRIGHTNESS: System.out.print("BRIGHTNESS - target "+preferences[BRIGHTNESS]);
			break;
		
		
		}
		
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
	

}
