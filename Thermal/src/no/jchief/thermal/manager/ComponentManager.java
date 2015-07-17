package no.jchief.thermal.manager;

import java.util.Set;

import no.jchief.thermal.component.AbstractComponent;
import no.jchief.thermal.entity.Entity;
import no.jchief.thermal.event.AbstractComponentDeltaEvent;
import no.jchief.thermal.exception.InvalidComponentActionException;
import no.jchief.thermal.exception.InvalidDeltaEventException;

public interface ComponentManager {
	public Set<Entity> getManagedEntities();
	public Set<? extends AbstractComponent> getManagedComponents();
	public void addComponent(Entity e, AbstractComponent c) throws InvalidComponentActionException;
//	public void registerAsListener();
//	public void processEvent(AbstractGameEvent ge);
	public void applyChange(Entity e, AbstractComponentDeltaEvent cce ) throws InvalidDeltaEventException;
	public AbstractComponent getComponentByEntityId(int id);
}
