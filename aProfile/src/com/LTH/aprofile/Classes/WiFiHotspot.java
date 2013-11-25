package com.LTH.aprofile.Classes;

public class WiFiHotspot {
	private String mac;
	private String name;

	public WiFiHotspot(String name, String mac) {
		this.name = name;
		this.mac = mac;
	}

	public String getName() {
		return name;
	}

	//override check of equals to only mac-address
	@Override
	public boolean equals(Object o) {
		return mac.compareTo(((WiFiHotspot) o).mac) == 0;

	}
	
	@Override 
	public int hashCode() {
        return mac.hashCode();
    }
}
