package no.jchief.thermal.entity;

import java.util.Vector;

import no.jchief.thermal.component.AbstractComponent;

public class Entity {
	public int entityId;
	public Vector<AbstractComponent> components;
	
	public Entity(int entityId) {
		super();
		this.entityId = entityId;
		components = new Vector<AbstractComponent>();
	}
	
	public void addComponent(AbstractComponent c ) {
		components.add( c );
	}
}
