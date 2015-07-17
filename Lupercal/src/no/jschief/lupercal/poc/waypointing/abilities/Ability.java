package no.jschief.lupercal.poc.waypointing.abilities;


import no.jschief.lupercal.poc.waypointing.WaypointDiscovery;
import no.jschief.lupercal.poc.waypointing.interfaces.Actionable;

public abstract class Ability implements Actionable {
	String name;
	boolean needsUpdate;
	WaypointDiscovery wd;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

//	public WaypointDiscovery getWd() {
//		return wd;
//	}
//
//	public void setWd(WaypointDiscovery wd) {
//		this.wd = wd;
//	}

	public boolean isNeedsUpdate() {
		return needsUpdate;
	}

	public void setNeedsUpdate(boolean needsUpdate) {
		this.needsUpdate = needsUpdate;
	}
	
	

	
	
}
