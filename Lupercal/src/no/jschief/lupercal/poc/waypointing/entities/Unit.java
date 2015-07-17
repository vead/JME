package no.jschief.lupercal.poc.waypointing.entities;

import com.jme3.math.Vector3f;

public class Unit {
	private String name;
	
	private Vector3f position;
	private Vector3f velocity;
	private Vector3f goal;
	private Vector3f steerAmount;
	
	public Unit( String name, Vector3f position ) {
		this.name = name;
		this.position = position;
		
		this.velocity = Vector3f.ZERO;
		this.goal = Vector3f.ZERO;
		this.steerAmount = Vector3f.ZERO;
	}
	
	
}
