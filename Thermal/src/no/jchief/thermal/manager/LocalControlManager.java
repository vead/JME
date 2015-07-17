package no.jchief.thermal.manager;

import java.util.HashMap;
import java.util.Set;

import no.jchief.thermal.ThermalMain;
import no.jchief.thermal.component.AbstractComponent;
import no.jchief.thermal.component.GeoComponent;
import no.jchief.thermal.entity.Entity;
import no.jchief.thermal.event.AbstractComponentDeltaEvent;
import no.jchief.thermal.event.AbstractGameEvent;
import no.jchief.thermal.event.InputEvent;
import no.jchief.thermal.exception.InvalidComponentActionException;
import no.jchief.thermal.exception.InvalidDeltaEventException;
import no.jchief.thermal.system.sysinterface.GameEventListener;

public class LocalControlManager implements ComponentManager, GameEventListener {

	ThermalMain tm;

	private HashMap<Entity, LocalCon> localControlEntities;
	
	public LocalControlManager(ThermalMain tm) {
		super();
		this.tm = tm;

		localControlEntities = new HashMap<Entity, GeoComponent>();
	}

	@Override
	public Set<Entity> getManagedEntities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addComponent(Entity e, AbstractComponent c)
			throws InvalidComponentActionException {
		
		
	}

	@Override
	public void applyChange(Entity e, AbstractComponentDeltaEvent cce)
			throws InvalidDeltaEventException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processGameEvent(AbstractGameEvent ge) {
		InputEvent ie = (InputEvent)ge;
		
		int press = ie.pressed ? 1 : 0;

		switch (ie.type) {
			case LEFT :	this.setDirection(SIDE, -press); break;
			case RIGHT :	this.setDirection(SIDE, press); break;
			case UP :	this.setDirection(FWD, -press); break;
			case DOWN :	this.setDirection(FWD, press); break;
			case ROT_CW :	this.setDirection(ROTATE, -press); break;
			case ROT_CCW :	this.setDirection(ROTATE, press); break;
			case TILTFWD :	this.setDirection(TILT, press); break;
			case TILTBACK :	this.setDirection(TILT, -press); break;
			case IN :	this.setDirection(DISTANCE, -press); break;
			case OUT :	this.setDirection(DISTANCE, press); break;
			default: break;
		}
	}

	@Override
	public AbstractComponent getComponentByEntityId(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<? extends AbstractComponent> getManagedComponents() {
		
		return null;
	}

}
