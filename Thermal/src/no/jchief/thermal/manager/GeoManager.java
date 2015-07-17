package no.jchief.thermal.manager;

import java.util.HashMap;
import java.util.Set;

import no.jchief.thermal.ThermalMain;
import no.jchief.thermal.component.AbstractComponent;
import no.jchief.thermal.component.GeoComponent;
import no.jchief.thermal.entity.Entity;
import no.jchief.thermal.event.AbstractComponentDeltaEvent;
import no.jchief.thermal.event.GeoComponentDeltaEvent;
import no.jchief.thermal.exception.InvalidComponentActionException;
import no.jchief.thermal.exception.InvalidDeltaEventException;

public class GeoManager implements ComponentManager {



	private HashMap<Entity, GeoComponent> geoEntities;
//	private ThermalMain th; 
	
	
	
	public GeoManager(ThermalMain tm) {
		super();
//		this.th = tm;
//		GameEventSystem ges = (GameEventSystem) tm.getSystems().get(GameEventSystem.class);
//		ges.registerListener(InputEvent.class, this);
		geoEntities = new HashMap<Entity, GeoComponent>();
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public void addComponent(Entity entity, AbstractComponent geoComponent) throws InvalidComponentActionException {
		GeoComponent gc = (GeoComponent)geoComponent;
		entity.addComponent(gc);
		if ( geoEntities.containsKey( entity ))
			throw new InvalidComponentActionException("GeoManager: Entity already has this component.");
		geoEntities.put(entity, gc);
	}

	@Override
	public Set<Entity> getManagedEntities() {
		return geoEntities.keySet();
	}

	@Override
	public void applyChange(Entity e, AbstractComponentDeltaEvent cce) throws InvalidDeltaEventException {
		if ( ! cce.getClass().isInstance( GeoComponentDeltaEvent.class )) {
			throw new InvalidDeltaEventException("GeoManager cannot apply changes of ComponentDelta class " + cce.getClass() );
		}
		
	}


	@Override
	public AbstractComponent getComponentByEntityId(int id) {
		for( Entity e : geoEntities.keySet()) {
			if ( id == e.entityId ) {
				return geoEntities.get( e );
			}
		}
		return null;
	}



	

}
