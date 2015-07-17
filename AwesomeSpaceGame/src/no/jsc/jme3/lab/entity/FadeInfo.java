package no.jsc.jme3.lab.entity;

import com.jme3.scene.Geometry;

public class FadeInfo {
	private float creationTime;
	private float totalLifetime;
	private float remaining;
	private Geometry geo;
	
	public FadeInfo(float creationTime, float totalLifetime, Geometry geo) {
		super();
		this.creationTime = creationTime;
		this.totalLifetime = totalLifetime;
//		this.remaining = remaining;
		this.geo = geo;
	}

	public float getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(float creationTime) {
		this.creationTime = creationTime;
	}

	public float getTotalLifetime() {
		return totalLifetime;
	}

	public void setTotalLifetime(float totalLifetime) {
		this.totalLifetime = totalLifetime;
	}

	public float getRemaining() {
		return remaining;
	}

	public void setRemaining(float remaining) {
		this.remaining = remaining;
	}

	public Geometry getGeo() {
		return geo;
	}

	public void setGeo(Geometry geo) {
		this.geo = geo;
	}


	
	

}
